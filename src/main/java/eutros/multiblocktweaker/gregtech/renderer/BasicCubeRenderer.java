package eutros.multiblocktweaker.gregtech.renderer;

import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCICubeRenderer;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BasicCubeRenderer extends MCICubeRenderer {
    
    public BasicCubeRenderer(ICubeRenderer cubeRenderer) {
        super(cubeRenderer);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void preStitch(TextureStitchEvent.Pre evt) {
        cube.registerIcons(evt.getMap());
    }
}
