package eutros.multiblocktweaker.gregtech.renderer;

import codechicken.lib.render.CCQuad;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import gregtech.client.renderer.CubeRendererState;
import gregtech.client.renderer.handler.FacadeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.AdvCCRSConsumer;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.client.utils.FacadeBlockAccess;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IBlockStateRenderer implements IICubeRenderer {
    private static final Map<IBlockState, IBlockStateRenderer> CACHE = new Object2ObjectOpenHashMap<>();
    public final IBlockState state;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite particle;
    @SideOnly(Side.CLIENT)
    EnumMap<EnumFacing, List<CCQuad>> itemQuads;

    private IBlockStateRenderer(IBlockState state) {
        this.state = state;
    }

    public static IBlockStateRenderer create(IBlockState state) {
        return CACHE.computeIfAbsent(state, IBlockStateRenderer::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleSprite() {
        return particle == null ? particle = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture() : particle;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds, EnumFacing face, boolean b, boolean b1) {
        CubeRendererState rendererState = Textures.RENDER_STATE.get();
        if (rendererState == null || rendererState.layer == null) { // item
            if (itemQuads == null) {
                itemQuads = new EnumMap<>(EnumFacing.class);
                Minecraft minecraft = Minecraft.getMinecraft();
                RenderItem renderItem = minecraft.getRenderItem();
                ItemStack renderStack = new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().damageDropped(state));
                IBakedModel itemModel = renderItem.getItemModelWithOverrides(renderStack, null, null);
                for (EnumFacing side : EnumFacing.values()) {
                    List<BakedQuad> quads = new ArrayList<>();
                    if (side == EnumFacing.UP) {
                        quads.addAll(itemModel.getQuads(null, null, 0));
                    }
                    quads.addAll(itemModel.getQuads(null, side, 0));
                    itemQuads.put(side, FacadeRenderer.applyItemTint(CCQuad.fromArray(quads), renderStack));
                }
            }
            AdvCCRSConsumer consumer = new AdvCCRSConsumer(renderState);
            for (CCQuad quad : itemQuads.get(face)) {
                quad.pipe(consumer);
            }
            return;
        }
        BlockRenderLayer[] layers;
        if (rendererState.layer == BlockRenderLayer.CUTOUT_MIPPED) {
            layers = new BlockRenderLayer[]{BlockRenderLayer.SOLID, BlockRenderLayer.CUTOUT_MIPPED, BlockRenderLayer.CUTOUT};
        } else if (rendererState.layer == BlockRenderLayer.TRANSLUCENT) {
            layers = new BlockRenderLayer[]{BlockRenderLayer.TRANSLUCENT};
        } else if (rendererState.layer == BloomEffectUtil.BLOOM) {
            layers = new BlockRenderLayer[]{BloomEffectUtil.BLOOM};
        } else layers = new BlockRenderLayer[0];
        for (BlockRenderLayer layer : layers) {
            if (state.getBlock().canRenderInLayer(state, layer)) { // block
                BlockPos pos = new BlockPos(translation.m03, translation.m13, translation.m23);
                if (!state.shouldSideBeRendered(rendererState.world, pos, face)) return;
                IBlockAccess coverAccess = new FacadeBlockAccess(rendererState.world, pos, face, state);
                BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
                IBlockState state = this.state;
                try {
                    state = state.getActualState(coverAccess, pos);
                } catch (Exception ignored) { }
                IBakedModel model = brd.getModelForState(state);

                try {
                    state = state.getBlock().getExtendedState(state, coverAccess, pos);
                } catch (Exception ignored) { }

                long posRand = net.minecraft.util.math.MathHelper.getPositionRandom(pos);
                List<BakedQuad> bakedQuads = new LinkedList<>(model.getQuads(state, null, posRand));

                bakedQuads.addAll(model.getQuads(state, face, posRand));

                List<CCQuad> quads = CCQuad.fromArray(bakedQuads);

                if (!quads.isEmpty()) {
                    VertexLighterFlat lighter = FacadeRenderer.setupLighter(renderState, translation, state, coverAccess, pos, model);
                    FacadeRenderer.renderBlockQuads(lighter, coverAccess, state, quads, pos);
                }
                return;
            }
        }

    }

}
