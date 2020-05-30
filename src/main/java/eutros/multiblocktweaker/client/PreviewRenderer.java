package eutros.multiblocktweaker.client;

import eutros.multiblocktweaker.jei.MultiblockTweakerJEIPlugin;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.render.scene.WorldSceneRenderer;
import gregtech.common.blocks.MetaBlocks;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
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
import org.apache.commons.lang3.reflect.FieldUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class PreviewRenderer {

    public static final PreviewRenderer INSTANCE = new PreviewRenderer();
    private int opList = -1;

    private PreviewRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Nullable
    private WorldSceneRenderer renderer = null;

    public BlockPos targetPos = BlockPos.ORIGIN;
    public BlockPos controllerPos = BlockPos.ORIGIN;
    public Rotation rotatePreviewBy = Rotation.NONE;

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

        float partialTicks = mc.getRenderPartialTicks();

        Entity entity = mc.getRenderViewEntity();
        if(entity == null) entity = mc.player;

        double tx = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
        double ty = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
        double tz = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-tx, -ty, -tz);
        GlStateManager.callList(opList);
        GlStateManager.popMatrix();
    }

    private void refreshOpList() {
        if(opList != -1) {
            GlStateManager.glDeleteLists(opList, 1);
            opList = -1;
        }

        Optional<List<BlockPos>> renderedBlocks = getRenderedBlocks();
        if(!renderedBlocks.isPresent()) return;

        opList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(opList, GL11.GL_COMPILE);

        Minecraft mc = Minecraft.getMinecraft();

        BlockRenderLayer oldLayer = MinecraftForgeClient.getRenderLayer();
        BlockRendererDispatcher brd = mc.blockRenderDispatcher;
        BlockModelRenderer mr = brd.getBlockModelRenderer();
        WorldSceneRenderer.TrackedDummyWorld world = renderer.world;

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buff = tes.getBuffer();

        GlStateManager.translate(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        GlStateManager.rotate(rotatePreviewBy.ordinal() * 90, 0, -1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);

        IBakedModel missingNo = brd.getBlockModelShapes().getModelManager().getMissingModel();

        for(BlockPos pos : renderedBlocks.get()) {
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

    @SuppressWarnings("unchecked")
    private Optional<List<BlockPos>> getRenderedBlocks() {
        try {
            return Optional.ofNullable((List<BlockPos>) FieldUtils.getField(WorldSceneRenderer.class,
                    "renderedBlocks",
                    true).get(renderer));
        } catch(IllegalAccessException | ClassCastException | NullPointerException e) {
            return Optional.empty();
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

        Optional<List<BlockPos>> r = getRenderedBlocks();

        controllerPos = BlockPos.ORIGIN;
        if(r.isPresent()) {
            for(BlockPos blockPos : r.get()) {
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

        return true;
    }

}
