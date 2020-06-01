package eutros.multiblocktweaker.client;

import com.google.common.collect.ImmutableList;
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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static eutros.multiblocktweaker.client.ClientTickHandler.partialTicks;

@SideOnly(Side.CLIENT)
public class PreviewRenderer {

    private PreviewRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private int baseY = 0;
    private int frame = 0;
    private int opList = -1;

    @Nullable
    private WorldSceneRenderer renderer = null;
    @Nullable
    private BlockPos errorHighlight;
    private List<BlockPos> renderedBlocks;

    public BlockPos targetPos = BlockPos.ORIGIN;
    public BlockPos controllerPos = BlockPos.ORIGIN;
    public Rotation rotatePreviewBy = Rotation.NONE;
    private int layerIndex = -1;

    public static final PreviewRenderer INSTANCE = new PreviewRenderer();

    @Nullable
    private static WorldSceneRenderer getRenderer(ItemStack stack) {
        IRecipeRegistry rr = MultiblockTweakerJEIPlugin.runtime.getRecipeRegistry();

        IFocus<ItemStack> focus = rr.createFocus(IFocus.Mode.INPUT, stack);

        //noinspection unchecked
        return rr.getRecipeCategories(focus)
                .stream()
                .map(c -> (IRecipeCategory<IRecipeWrapper>) c)
                .map(c -> rr.getRecipeWrappers(c, focus))
                .flatMap(List::stream)
                .filter(MultiblockInfoRecipeWrapper.class::isInstance)
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
            reset();
            return;
        }

        frame++;

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

    private void reset() {
        renderer = null;
        targetPos = BlockPos.ORIGIN;
        layerIndex = -1;
        removeOpList();
    }

    private List<BlockPos> filterLayer(List<BlockPos> blocks) {
        if(layerIndex == -1)
            return blocks;

        List<BlockPos> list = new ArrayList<>();
        for(BlockPos pos : blocks) {
            if(pos.getY() == baseY + layerIndex) {
                list.add(pos);
            }
        }

        if(list.isEmpty()) {
            layerIndex = -1;
            setErroneousBlockPos();
            return blocks;
        }

        return list;
    }

    private void refreshOpList() {
        removeOpList();

        List<BlockPos> renderedBlocks = filterLayer(this.renderedBlocks);

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

        TargetBlockAccess targetBA = new TargetBlockAccess(world, BlockPos.ORIGIN);

        for(BlockPos pos : renderedBlocks) {
            targetBA.setPos(pos);

            GlStateManager.pushMatrix();

            BlockPos tPos = pos.subtract(controllerPos);
            GlStateManager.translate(tPos.getX(), tPos.getY(), tPos.getZ());
            GlStateManager.translate(0.125, 0.125, 0.125);
            GlStateManager.scale(0.75, 0.75, 0.75);

            IBlockState state = world.getBlockState(pos);
            drawModel(tes, buff, state, () -> brd.renderBlock(state, BlockPos.ORIGIN, targetBA, buff));

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        ForgeHooksClient.setRenderLayer(oldLayer);

        GlStateManager.glEndList();
    }

    private void removeOpList() {
        if(opList != -1) {
            GlStateManager.glDeleteLists(opList, 1);
            opList = -1;
        }
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

    public boolean onUse(World world, BlockPos pos, boolean isRightClick) {
        if(pos.equals(targetPos)) {
            layerIndex += isRightClick ? 1 : -1;
            if(layerIndex < -1) {
                reset();
            } else {
                setErroneousBlockPos();
                refreshOpList();
            }
            return true;
        }

        if(!isRightClick) return false;

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

        renderedBlocks = ImmutableList.copyOf(ReflectionHelper.<List<BlockPos>, WorldSceneRenderer>getPrivate(WorldSceneRenderer.class, "renderedBlocks", renderer));

        controllerPos = BlockPos.ORIGIN;
        if(renderedBlocks != null) {
            baseY = Collections.min(renderedBlocks, Comparator.comparing(BlockPos::getY)).getY();
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

        setErroneousBlockPos();

        return true;
    }

    private void highlightErrors() {
        if(frame % 20 == 0)
            setErroneousBlockPos();

        if(errorHighlight == null) return;

        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        RenderHelper.drawColouredAABB(new AxisAlignedBB(errorHighlight), 1, 0, 0, 0.5F);
        GlStateManager.enableDepth();

        GlStateManager.popMatrix();
    }

    private void setErroneousBlockPos() {
        Minecraft mc = Minecraft.getMinecraft();
        MetaTileEntity mte = BlockMachine.getMetaTileEntity(mc.world, targetPos);
        if(!(mte instanceof MultiblockControllerBase)) {
            return;
        }
        MultiblockControllerBase te = (MultiblockControllerBase) mte;
        BlockPattern pattern = ReflectionHelper.getPrivate(MultiblockControllerBase.class, "structurePattern", te);
        if(pattern == null) return;

        errorHighlight = getErroneousAABB(pattern);

        if(errorHighlight != null && errorHighlight != null &&
                !(layerIndex == -1 ||
                        baseY + layerIndex == errorHighlight.getY())) {
            errorHighlight = null;
        }
    }

    @Nullable
    private BlockPos getErroneousAABB(BlockPattern pattern) {
        WorldClient world = Minecraft.getMinecraft().world;
        MetaTileEntity te = BlockMachine.getMetaTileEntity(world, targetPos);

        if(((MultiblockControllerBase) te).isStructureFormed()) {
            reset();
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
                        return BlockPos.ORIGIN; // Highlight the controller.
                    }
                }
                //Repetitions out of range
                if(r < aisleRepetitions[c][0]) {
                    return blockPos.subtract(targetPos); // Highlight last tested block.
                }
            }

            return BlockPos.ORIGIN; // Highlight the controller.
        }
    }

}
