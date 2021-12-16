package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCSound;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Can be used for Sound.
 *
 * @zenClass mods.gregtech.ISound
 */
@ZenClass("mods.gregtech.ISound")
@ZenRegister
public interface ISound {
    SoundEvent getInternal();

    @ZenMethod
    @Nullable
    static ISound byName(String name) {
        ResourceLocation loc = new ResourceLocation(name);
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(loc);
        if (sound != null)
            return new MCSound(sound);
        return null;
    }

    /**
     * Register and Get the sound at a location.
     * <p>
     * You will most likely wish to define this in a script with {@code #loader preinit}, so the texture actually gets loaded.
     *
     * @param soundLocation The full location of the sound.
     * @return An {@link ISound} of the given sound.
     */
    @ZenMethod
    static ISound registerSound(String soundLocation) {
        ResourceLocation location = new ResourceLocation(soundLocation);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return new MCSound(event);
    }
}
