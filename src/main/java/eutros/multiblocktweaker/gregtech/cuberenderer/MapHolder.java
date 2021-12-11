package eutros.multiblocktweaker.gregtech.cuberenderer;

import eutros.multiblocktweaker.MultiblockTweaker;
import gregtech.api.GTValues;
import gregtech.api.gui.resources.ResourceHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

@Mod.EventBusSubscriber(modid = MultiblockTweaker.MOD_ID, value = Side.CLIENT)
public class MapHolder {

    public static TextureMap map = null;

    @SubscribeEvent
    public static void postStitch(TextureStitchEvent.Post evt) {
        map = evt.getMap();
    }

    public static boolean isTextureExist(ResourceLocation textureResource) {
        InputStream inputstream = ResourceHelper.class.getResourceAsStream(String.format("/assets/%s/textures/%s.png", textureResource.getNamespace(), textureResource.getPath()));
        if(inputstream == null) {
            return false;
        }
        IOUtils.closeQuietly(inputstream);
        return true;
    }
}
