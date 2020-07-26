package eutros.multiblocktweaker.client;

import eutros.multiblocktweaker.helper.ReflectionHelper;
import eutros.multiblocktweaker.jei.MultiblockTweakerJEIPlugin;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.render.scene.WorldSceneRenderer;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
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
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eutros.multiblocktweaker.client.ClientTickHandler.partialTicks;

@SideOnly(Side.CLIENT)
public class PreviewRenderer {

    private PreviewRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static BlockPos DEFAULT_POS = BlockPos.ORIGIN.offset(EnumFacing.DOWN);

    private int frame = 0;
    private int opList = -1;

    private int minY;
    private int maxY;

    @Nullable
    private WorldSceneRenderer renderer = null;
    @Nullable
    private BlockPos errorHighlight;
    private List<BlockPos> renderedBlocks;

    public BlockPos targetPos = DEFAULT_POS;
    public BlockPos controllerPos = DEFAULT_POS;
    public Rotation rotatePreviewBy = Rotation.NONE;
    private int layerIndex = -1;

    public static final PreviewRenderer INSTANCE = new PreviewRenderer();

    @Nullable
    @SuppressWarnings("unchecked")
    private static WorldSceneRenderer getRenderer(ItemStack stack) {
        IRecipeRegistry rr = MultiblockTweakerJEIPlugin.runtime.getRecipeRegistry();

        IFocus<ItemStack> focus = rr.createFocus(IFocus.Mode.INPUT, stack);

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

        if(PreviewHandler.getMetaController(mc.world, targetPos) == null) {
            reset();
            return;
        }

        frame++;

        Entity entity = mc.getRenderViewEntity();
        if(entity == null) entity = mc.player;

        double tx = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
        double ty = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
        double tz = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.color(1F, 1F, 1F, 1F);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-tx, -ty, -tz);
        GlStateManager.translate(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        GlStateManager.enableBlend();
        GlStateManager.callList(opList);
        highlightErrors();
        GlStateManager.popMatrix();

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    private void reset() {
        renderer = null;
        targetPos = DEFAULT_POS;
        layerIndex = -1;
        removeOpList();
    }

    private List<BlockPos> filterLayer(List<BlockPos> blocks) {
        if(layerIndex == -1)
            return blocks;

        if(layerIndex > maxY - minY) {
            reset();
            return Collections.emptyList();
        }

        List<BlockPos> list = new ArrayList<>();
        for(BlockPos pos : blocks) {
            if(pos.getY() == minY + layerIndex) {
                list.add(pos);
            }
        }

        return list;
    }

    private void refreshOpList() {
        removeOpList();

        if(renderer == null) return;

        List<BlockPos> renderedBlocks = filterLayer(this.renderedBlocks);

        if(renderedBlocks.isEmpty()) return;

        opList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(opList, GL11.GL_COMPILE);

        Minecraft mc = Minecraft.getMinecraft();

        BlockRenderLayer oldLayer = MinecraftForgeClient.getRenderLayer();
        BlockRendererDispatcher brd = mc.blockRenderDispatcher;
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

            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            IBlockState state = world.getBlockState(pos);
            for(BlockRenderLayer brl : BlockRenderLayer.values()) {
                if(state.getBlock().canRenderInLayer(state, brl)) {
                    ForgeHooksClient.setRenderLayer(brl);
                    brd.renderBlock(state, BlockPos.ORIGIN, targetBA, buff);
                }
            }
            tes.draw();

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

    public boolean onUse(World world, BlockPos pos, boolean isRightClick) {
        if(pos.equals(targetPos)) {
            layerIndex += isRightClick ? 1 : -1;
            if(layerIndex < -1) {
                reset();
            } else {
                checkErrors();
                refreshOpList();
            }
            return true;
        }

        if(!isRightClick) return false;

        MultiblockControllerBase te = PreviewHandler.getMetaController(world, pos);
        if(te == null) return false;

        WorldSceneRenderer tempRenderer = getRenderer(te.getStackForm());
        if(tempRenderer == null) return false;

        reset();
        renderer = tempRenderer;

        targetPos = pos;
        EnumFacing facing, previewFacing;
        previewFacing = facing = te.getFrontFacing();

        renderedBlocks = ReflectionHelper.getPrivate(WorldSceneRenderer.class, "renderedBlocks", renderer);

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

            minY = Integer.MAX_VALUE;
            maxY = Integer.MIN_VALUE;
            for(BlockPos blockPos : renderedBlocks) {
                int y = blockPos.getY();
                if(y < minY) {
                    minY = y;
                }
                if(y > maxY) {
                    maxY = y;
                }
            }
        }

        rotatePreviewBy = Rotation.values()[(4 + facing.getHorizontalIndex() - previewFacing.getHorizontalIndex()) % 4];
        refreshOpList();

        checkErrors();

        return true;
    }

    private void highlightErrors() {
        if(frame % 20 == 0)
            checkErrors();

        if(errorHighlight == null) return;

        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        RenderHelper.drawColouredAABB(new AxisAlignedBB(errorHighlight), 1, 0, 0, 0.5F);
        GlStateManager.enableDepth();

        GlStateManager.popMatrix();
    }

    private void checkErrors() {
        Minecraft mc = Minecraft.getMinecraft();
        MultiblockControllerBase te = PreviewHandler.getMetaController(mc.world, targetPos);
        if(te == null) return;
        BlockPattern pattern = ReflectionHelper.getPrivate(MultiblockControllerBase.class, "structurePattern", te);
        if(pattern == null) return;

        errorHighlight = getErroneousPos(pattern);
    }

    @Nullable
    private BlockPos getErroneousPos(BlockPattern pattern) {
        WorldClient world = Minecraft.getMinecraft().world;
        MetaTileEntity te = BlockMachine.getMetaTileEntity(world, targetPos);

        if(((MultiblockControllerBase) te).isStructureFormed()) {
            reset();
            return null;
        }

        PatternMatchContext ctx = pattern.checkPatternAt(world, targetPos, te.getFrontFacing().getOpposite());
        if(ctx == null) {
            BlockPos lastPos = ReflectionHelper.getPrivate(BlockPattern.class, "blockPos", pattern);
            if(lastPos != null) {
                return lastPos.subtract(targetPos);
            }
        }

        return BlockPos.ORIGIN;
    }

}
