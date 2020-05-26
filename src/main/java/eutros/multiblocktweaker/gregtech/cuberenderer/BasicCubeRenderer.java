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

public class BasicCubeRenderer implements ICubeRenderer {

    private ResourceLocation loc;
    private TextureAtlasSprite sprite = null;

    public BasicCubeRenderer(ResourceLocation loc) {
        this.loc = loc;
        if(MapHolder.map != null) {
            sprite = MapHolder.map.getAtlasSprite(loc.toString());
        } else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public BasicCubeRenderer(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public TextureAtlasSprite getParticleSprite() {
        return sprite;
    }

    @Override // why why why why why
    public void render(CCRenderState state, Matrix4 translate, IVertexOperation[] ops, Cuboid6 cuboid) {
        for(EnumFacing face : EnumFacing.values()) {
            Textures.renderFace(state, translate, ops, face, cuboid, sprite);
        }
    }

    @SubscribeEvent
    public void preStitch(TextureStitchEvent.Pre evt) {
        if(sprite == null)
            sprite = evt.getMap().registerSprite(loc);
    }

}
