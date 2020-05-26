package eutros.multiblocktweaker.gregtech.cuberenderer;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SidedCubeRenderer implements ICubeRenderer {

    private final Map<EnumFacing, ResourceLocation> sides;
    private Map<EnumFacing, TextureAtlasSprite> sprites;

    public SidedCubeRenderer(Map<EnumFacing, ResourceLocation> sides) {
        this.sides = sides;
        if(MapHolder.map != null) {
            sprites = sides.keySet().stream()
                    .collect(Collectors.toMap(Function.identity(),
                    r -> MapHolder.map.getAtlasSprite(sides.get(r).toString())));
        } else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void preStitch(TextureStitchEvent.Pre evt) {
        sprites = sides.keySet().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        f -> evt.getMap().registerSprite(sides.get(f))));
    }

    @Override
    public TextureAtlasSprite getParticleSprite() {
        return sprites.get(EnumFacing.UP);
    }

    @Override
    public void render(CCRenderState state, Matrix4 translate, IVertexOperation[] ops, Cuboid6 cuboid) {
        for(EnumFacing side : EnumFacing.values()) {
            Textures.renderFace(state, translate, ops, side, cuboid, sprites.get(side));
        }
    }

}
