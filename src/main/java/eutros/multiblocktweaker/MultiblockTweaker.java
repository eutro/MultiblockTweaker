package eutros.multiblocktweaker;

import eutros.multiblocktweaker.client.PreviewHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = MultiblockTweaker.MOD_ID,
        name = "Multiblock Tweaker",
        version = "@GRADLE:VERSION@",
        dependencies = "required-after:gregtech@[2.0,);")
public class MultiblockTweaker {

    public static final String MOD_ID = "multiblocktweaker";

    public MultiblockTweaker() {
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {
        if (evt.getSide().isClient())
            PreviewHandler.init();
    }

}
