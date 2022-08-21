package eutros.multiblocktweaker;

import gregtech.api.GTValues;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = MultiblockTweaker.MOD_ID,
        name = "Multiblock Tweaker",
        version = "@GRADLE:VERSION@",
        dependencies = GTValues.MOD_VERSION_DEP)
public class MultiblockTweaker {

    public static final String MOD_ID = "multiblocktweaker";

    public MultiblockTweaker() {
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {
    }

}
