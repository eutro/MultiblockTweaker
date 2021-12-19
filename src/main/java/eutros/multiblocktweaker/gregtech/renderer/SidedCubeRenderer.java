package eutros.multiblocktweaker.gregtech.renderer;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.google.common.base.Preconditions;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import gregtech.api.gui.resources.ResourceHelper;
import gregtech.client.renderer.cclop.LightMapOperation;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.common.ConfigHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.EnumMap;
import java.util.Map;

public class SidedCubeRenderer implements IICubeRenderer {

    private Map<EnumFacing, String> paths;
    @SideOnly(Side.CLIENT)
    private Map<EnumFacing, TextureAtlasSprite> sprites;
    @SideOnly(Side.CLIENT)
    private Map<EnumFacing, TextureAtlasSprite> spritesEmissive;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite particles;

    public SidedCubeRenderer(String key, Map<EnumFacing, String> paths) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.paths = paths;
            this.sprites = new EnumMap<>(EnumFacing.class);
            this.spritesEmissive = new EnumMap<>(EnumFacing.class);
        }
        Textures.CUBE_RENDERER_REGISTRY.put(key, this);
        Textures.iconRegisters.add(this);
    }

    public static <T> EnumMap<EnumFacing, T> fillBlanks(Map<EnumFacing, T> map) {
        Preconditions.checkNotNull(
                map.get(EnumFacing.UP),
                "UP side has no texture! " +
                "Consider using mods.gregtech.multiblock.Builder#withTexture to specify the texture explicitly."
        );
        EnumMap<EnumFacing, T> retMap = new EnumMap<>(map);

        retMap.computeIfAbsent(EnumFacing.DOWN, k -> retMap.get(EnumFacing.UP));

        retMap.computeIfAbsent(EnumFacing.NORTH,
                a -> retMap.computeIfAbsent(EnumFacing.EAST,
                        b -> retMap.computeIfAbsent(EnumFacing.SOUTH,
                                c -> retMap.computeIfAbsent(EnumFacing.WEST, k -> retMap.get(EnumFacing.UP)))));

        retMap.computeIfAbsent(EnumFacing.WEST, k -> retMap.get(EnumFacing.NORTH));
        retMap.computeIfAbsent(EnumFacing.SOUTH, k -> retMap.get(EnumFacing.NORTH));
        retMap.computeIfAbsent(EnumFacing.EAST, k -> retMap.get(EnumFacing.NORTH));
        return retMap;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleSprite() {
        return particles;
    }

    @Override
    public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds, EnumFacing side, boolean b, boolean b1) {
        Textures.renderFace(renderState, translation, pipeline, side, bounds, sprites.get(side), BlockRenderLayer.CUTOUT_MIPPED);
        if (spritesEmissive.get(side) != null) {
            if (ConfigHolder.client.machinesEmissiveTextures) {
                IVertexOperation[] lightPipeline = ArrayUtils.add(pipeline, new LightMapOperation(240, 240));
                Textures.renderFace(renderState, translation, lightPipeline, side, bounds, spritesEmissive.get(side), BloomEffectUtil.getRealBloomLayer());
            } else Textures.renderFace(renderState, translation, pipeline, side, bounds, spritesEmissive.get(side), BlockRenderLayer.CUTOUT_MIPPED);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(CCRenderState state, Matrix4 translate, IVertexOperation[] ops, Cuboid6 cuboid) {
        for (EnumFacing side : EnumFacing.values()) {
            renderSided(side, cuboid, state, ops, translate);
        }
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        for (EnumFacing facing : EnumFacing.values()) {
            sprites.put(facing, textureMap.registerSprite(new ResourceLocation(paths.get(facing))));
            ResourceLocation emissiveLocation = new ResourceLocation(paths.get(facing) + "_emissive");
            if (ResourceHelper.isTextureExist(emissiveLocation)) {
                spritesEmissive.put(facing, textureMap.registerSprite(emissiveLocation));
            }
        }
        particles = sprites.get(EnumFacing.UP);
    }
}
