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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BasicCubeRenderer implements ICubeRenderer {

    private ResourceLocation loc;
    private TextureAtlasSprite sprite = null;

    public BasicCubeRenderer(ResourceLocation loc) {
        if (FMLCommonHandler.instance().getSide().isServer())
            return;
        this.loc = loc;
        if (MapHolder.map != null) {
            sprite = MapHolder.map.getAtlasSprite(loc.toString());
        } else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleSprite() {
        return sprite;
    }

    @SideOnly(Side.CLIENT)
    @Override // why why why why why
    public void render(CCRenderState state, Matrix4 translate, IVertexOperation[] ops, Cuboid6 cuboid) {
        for (EnumFacing face : EnumFacing.values()) {
            Textures.renderFace(state, translate, ops, face, cuboid, sprite);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void preStitch(TextureStitchEvent.Pre evt) {
        if (sprite == null)
            sprite = evt.getMap().registerSprite(loc);
    }

}
