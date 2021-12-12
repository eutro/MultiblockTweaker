package eutros.multiblocktweaker.gregtech.renderer;

import codechicken.lib.render.CCQuad;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import gregtech.client.utils.AdvCCRSConsumer;
import gregtech.client.utils.FacadeBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

import static gregtech.client.renderer.handler.FacadeRenderer.sliceQuads;

public class IBlockStateRenderer implements IICubeRenderer {

    private static final ThreadLocal<VertexLighterFlat> lighterFlat = ThreadLocal.withInitial(() -> new VertexLighterFlat(Minecraft.getMinecraft().getBlockColors()));
    private static final ThreadLocal<VertexLighterFlat> lighterSmooth = ThreadLocal.withInitial(() -> new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()));

    private final IBlockState state;

    public IBlockStateRenderer(IBlockState state) {
        this.state = state;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleSprite() {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture();
    }

    @Override
    public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds, EnumFacing face, boolean b, boolean b1) {
        BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
        if (layer == null) { // item

        } else if (!state.getBlock().canRenderInLayer(state, layer)) {
            return ;
        }

        BlockPos pos = new BlockPos(translation.m03, translation.m13, translation.m23);
        IBlockAccess coverAccess = new FacadeBlockAccess(Minecraft.getMinecraft().world, pos, face, state);
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        IBlockState state = this.state;
        try {
            state = this.state.getBlock().getExtendedState(state, coverAccess, pos);
        } catch (Exception ignored) {
        }
        long posRand = net.minecraft.util.math.MathHelper.getPositionRandom(pos);
        List<BakedQuad> bakedQuads = new LinkedList<>(model.getQuads(state, null, posRand));

        for (EnumFacing face2 : EnumFacing.VALUES) {
            bakedQuads.addAll(model.getQuads(state, face2, posRand));
        }

        List<CCQuad> quads = CCQuad.fromArray(bakedQuads);
        quads = sliceQuads(quads, face.ordinal(), bounds);

        if (!quads.isEmpty()) {
            boolean renderAO = Minecraft.isAmbientOcclusionEnabled() && state.getLightValue(coverAccess, pos) == 0 && model.isAmbientOcclusion();
            VertexLighterFlat lighter = renderAO ? lighterSmooth.get() : lighterFlat.get();
            AdvCCRSConsumer consumer = new AdvCCRSConsumer(renderState);
            lighter.setParent(consumer);
            consumer.setTranslation(translation);
            if (!quads.isEmpty()) {
                lighter.setWorld(coverAccess);
                lighter.setState(state);
                lighter.setBlockPos(pos);
                lighter.updateBlockInfo();
                for (CCQuad quad : quads) {
                    quad.pipe(lighter);
                }
            }
        }
    }
}
