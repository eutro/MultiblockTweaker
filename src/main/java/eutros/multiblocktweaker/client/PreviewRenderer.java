package eutros.multiblocktweaker.client;

import eutros.multiblocktweaker.helper.ReflectionHelper;
import eutros.multiblocktweaker.jei.MultiblockTweakerJEIPlugin;
import gnu.trove.map.TIntObjectMap;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.render.scene.WorldSceneRenderer;
import gregtech.api.util.IntRange;
import gregtech.common.blocks.MetaBlocks;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class PreviewRenderer {

    private PreviewRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private int opList = -1;
    private int frame = 0;

    @Nullable
    private WorldSceneRenderer renderer = null;
    @Nullable
    private AxisAlignedBB errorHighlight;

    public BlockPos targetPos = BlockPos.ORIGIN;
    public BlockPos controllerPos = BlockPos.ORIGIN;
    public Rotation rotatePreviewBy = Rotation.NONE;

    public static final PreviewRenderer INSTANCE = new PreviewRenderer();

    @Nullable
    private static WorldSceneRenderer getRenderer(ItemStack stack) {
        IRecipeRegistry rr = MultiblockTweakerJEIPlugin.runtime.getRecipeRegistry();

        @SuppressWarnings("unchecked")
        IRecipeCategory<IRecipeWrapper> cat = rr.getRecipeCategory("gregtech:multiblock_info");
        List<IRecipeWrapper> recipes = rr.getRecipeWrappers(cat,
                rr.createFocus(IFocus.Mode.INPUT, stack));

        return recipes.stream().filter(MultiblockInfoRecipeWrapper.class::isInstance)
                .findFirst()
                .map(MultiblockInfoRecipeWrapper.class::cast)
                .map(MultiblockInfoRecipeWrapper::getCurrentRenderer)
                .orElse(null);
    }

    @SubscribeEvent
    public void renderModelPreview(RenderWorldLastEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();

        if(renderer == null || opList == -1) return;

        if(BlockMachine.getMetaTileEntity(mc.world, targetPos) == null) {
            renderer = null;
            targetPos = BlockPos.ORIGIN;
            return;
        }

        frame++;

        float partialTicks = mc.getRenderPartialTicks();

        Entity entity = mc.getRenderViewEntity();
        if(entity == null) entity = mc.player;

        double tx = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
        double ty = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
        double tz = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-tx, -ty, -tz);
        GlStateManager.translate(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        GlStateManager.callList(opList);
        highlightErrors();
        GlStateManager.popMatrix();
    }

    private void refreshOpList() {
        if(opList != -1) {
            GlStateManager.glDeleteLists(opList, 1);
            opList = -1;
        }

        List<BlockPos> renderedBlocks = ReflectionHelper.getPrivate(WorldSceneRenderer.class, "renderedBlocks", renderer);
        if(renderedBlocks == null) return;

        opList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(opList, GL11.GL_COMPILE);

        Minecraft mc = Minecraft.getMinecraft();

        BlockRenderLayer oldLayer = MinecraftForgeClient.getRenderLayer();
        BlockRendererDispatcher brd = mc.blockRenderDispatcher;
        BlockModelRenderer mr = brd.getBlockModelRenderer();
        WorldSceneRenderer.TrackedDummyWorld world = renderer.world;

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buff = tes.getBuffer();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(rotatePreviewBy.ordinal() * 90, 0, -1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);

        IBakedModel missingNo = brd.getBlockModelShapes().getModelManager().getMissingModel();

        for(BlockPos pos : renderedBlocks) {
            IBlockState exState, state;
            exState = state = world.getBlockState(pos);

            EnumBlockRenderType renderType = state.getRenderType();
            if(renderType == EnumBlockRenderType.INVISIBLE) continue;

            try {
                if(world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
                    state = state.getActualState(world, pos);
                }
                exState = state.getBlock().getExtendedState(state, world, pos);
            } catch(Throwable ignored) {
            }

            GlStateManager.pushMatrix();

            BlockPos tPos = pos.subtract(controllerPos);
            GlStateManager.translate(tPos.getX(), tPos.getY(), tPos.getZ());
            GlStateManager.translate(0.125, 0.125, 0.125);
            GlStateManager.scale(0.75, 0.75, 0.75);
            GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());

            IBakedModel model = brd.getModelForState(state);
            IBlockState finalState = exState;
            drawModel(tes, buff, state, (model == missingNo) ?
                                        () -> brd.renderBlock(world.getBlockState(pos), pos, world, buff) :
                                        () -> mr.renderModel(world, model, finalState, pos, buff, false, MathHelper.getPositionRandom(pos)));

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        ForgeHooksClient.setRenderLayer(oldLayer);

        GlStateManager.glEndList();
    }

    private void drawModel(Tessellator tes, BufferBuilder buff, IBlockState state, Runnable drawer) {
        for(BlockRenderLayer brl : BlockRenderLayer.values()) {
            if(state.getBlock().canRenderInLayer(state, brl)) {
                ForgeHooksClient.setRenderLayer(brl);
                buff.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                drawer.run();
                tes.draw();
            }
        }
    }

    public boolean onUse(World world, BlockPos pos) {
        if(pos.equals(targetPos)) {
            renderer = null;
            targetPos = BlockPos.ORIGIN;
            return true;
        }

        IBlockState state = world.getBlockState(pos);

        if(state.getBlock() != MetaBlocks.MACHINE) return false;

        MetaTileEntity mte = BlockMachine.getMetaTileEntity(world, pos);

        if(!(mte instanceof MultiblockControllerBase)) return false;

        MultiblockControllerBase te = (MultiblockControllerBase) mte;

        renderer = null;
        renderer = getRenderer(te.getStackForm());

        if(renderer == null) return false;

        targetPos = pos;
        EnumFacing facing, previewFacing;
        previewFacing = facing = mte.getFrontFacing();

        List<BlockPos> renderedBlocks = ReflectionHelper.getPrivate(WorldSceneRenderer.class, "renderedBlocks", renderer);

        controllerPos = BlockPos.ORIGIN;
        if(renderedBlocks != null) {
            for(BlockPos blockPos : renderedBlocks) {
                MetaTileEntity metaTE = BlockMachine.getMetaTileEntity(renderer.world, blockPos);
                if(metaTE != null && metaTE.metaTileEntityId.equals(te.metaTileEntityId)) {
                    controllerPos = blockPos;
                    previewFacing = metaTE.getFrontFacing();
                    break;
                }
            }
        }

        rotatePreviewBy = Rotation.values()[(4 + facing.getHorizontalIndex() - previewFacing.getHorizontalIndex()) % 4];
        refreshOpList();
        BlockPattern pattern = ReflectionHelper.getPrivate(MultiblockControllerBase.class, "structurePattern", te);
        if(pattern != null)
            errorHighlight = getErroneousAABB(pattern);

        return true;
    }

    private void highlightErrors() {
        Minecraft mc = Minecraft.getMinecraft();
        MetaTileEntity mte = BlockMachine.getMetaTileEntity(mc.world, targetPos);

        if(!(mte instanceof MultiblockControllerBase)) {
            return;
        }

        MultiblockControllerBase te = (MultiblockControllerBase) mte;

        BlockPattern pattern = ReflectionHelper.getPrivate(MultiblockControllerBase.class, "structurePattern", te);
        if(pattern == null) return;

        if(frame % 20 == 0)
            errorHighlight = getErroneousAABB(pattern);

        if(errorHighlight == null) return;

        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        RenderHelper.drawColouredAABB(errorHighlight, 1, 0, 0, 0.5F);
        GlStateManager.enableDepth();

        GlStateManager.popMatrix();
    }

    @Nullable
    private AxisAlignedBB getErroneousAABB(BlockPattern pattern) {
        WorldClient world = Minecraft.getMinecraft().world;
        MetaTileEntity te = BlockMachine.getMetaTileEntity(world, targetPos);

        if(((MultiblockControllerBase) te).isStructureFormed()) {
            return null;
        }

        Pair<Predicate<BlockWorldState>, IntRange>[] countMatches = ReflectionHelper.getPrivate(BlockPattern.class, "countMatches", pattern);
        int[] centerOffset = ReflectionHelper.getPrivate(BlockPattern.class, "centerOffset", pattern);
        int[][] aisleRepetitions = ReflectionHelper.getPrivate(BlockPattern.class, "aisleRepetitions", pattern);
        Predicate<BlockWorldState>[][][] blockMatches = ReflectionHelper.getPrivate(BlockPattern.class, "blockMatches", pattern);
        TIntObjectMap<Predicate<PatternMatchContext>> layerMatchers = ReflectionHelper.getPrivate(BlockPattern.class, "layerMatchers", pattern);
        Predicate<PatternMatchContext>[] validators = ReflectionHelper.getPrivate(BlockPattern.class, "validators", pattern);

        if(countMatches == null ||
                centerOffset == null ||
                aisleRepetitions == null ||
                blockMatches == null ||
                layerMatchers == null ||
                validators == null) return null;

        BlockWorldState worldState = new BlockWorldState();
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        EnumFacing facing = te.getFrontFacing().getOpposite();

        //noinspection MismatchedReadAndWriteOfArray ???
        int[] countMatchesCache = new int[countMatches.length];
        boolean findFirstAisle = false;
        int minX = -centerOffset[0];
        int minZ = -centerOffset[4];
        int minY = -centerOffset[1];

        PatternMatchContext matchContext = new PatternMatchContext();
        PatternMatchContext layerContext = new PatternMatchContext();

        /**
         * @see BlockPattern#checkPatternAt(World, BlockPos, EnumFacing)
         */
        {
            //Checking aisles
            for(int c = 0, z = minZ++, r; c < pattern.getFingerLength(); c++) {
                //Checking repeatable slices
                loop:
                for(r = 0; (findFirstAisle ? r < aisleRepetitions[c][1] : z <= -centerOffset[3]); r++) {
                    //Checking single slice
                    layerContext.reset();

                    for(int b = 0, y = minY; b < pattern.getThumbLength(); b++, y++) {
                        for(int a = 0, x = minX; a < pattern.getPalmLength(); a++, x++) {
                            Predicate<BlockWorldState> predicate = blockMatches[c][b][a];
                            if(ReflectionHelper.callMethod(BlockPattern.class, "setActualRelativeOffset",
                                    pattern, BlockPos.MutableBlockPos.class, int.class, int.class, int.class, EnumFacing.class,
                                    blockPos, x, y, z, facing) == null) return null;
                            minX = -centerOffset[0];
                            minY = -centerOffset[1];
                            blockPos.setPos(blockPos.getX() + targetPos.getX(), blockPos.getY() + targetPos.getY(), blockPos.getZ() + targetPos.getZ());
                            worldState.update(world, blockPos, matchContext, layerContext);

                            if(!predicate.test(worldState)) {
                                if(findFirstAisle) {
                                    if(r < aisleRepetitions[c][0]) {//retreat to see if the first aisle can start later
                                        r = c = 0;
                                        z = minZ++;
                                        matchContext.reset();
                                        findFirstAisle = false;
                                    }
                                } else {
                                    z++;//continue searching for the first aisle
                                }
                                continue loop;
                            }
                            for(int i = 0; i < countMatchesCache.length; i++) {
                                if(countMatches[i].getLeft().test(worldState)) {
                                    countMatchesCache[i]++;
                                }
                            }
                        }
                    }
                    findFirstAisle = true;
                    z++;

                    //Check layer-local matcher predicate
                    Predicate<PatternMatchContext> layerPredicate = layerMatchers.get(c);
                    if(layerPredicate != null && !layerPredicate.test(layerContext)) {
                        return new AxisAlignedBB(BlockPos.ORIGIN); // Highlight the controller.
                    }
                }
                //Repetitions out of range
                if(r < aisleRepetitions[c][0]) {
                    return new AxisAlignedBB(blockPos.subtract(targetPos)); // Highlight last tested block.
                }
            }

            return new AxisAlignedBB(BlockPos.ORIGIN); // Highlight the controller.
        }
    }

}
