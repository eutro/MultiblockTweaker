package eutros.multiblocktweaker.gregtech.cuberenderer;

import eutros.multiblocktweaker.MultiblockTweaker;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MultiblockTweaker.MOD_ID)
public class MapHolder {

    public static TextureMap map = null;

    @SubscribeEvent
    public static void postStitch(TextureStitchEvent.Post evt) {
        map = evt.getMap();
    }

}
