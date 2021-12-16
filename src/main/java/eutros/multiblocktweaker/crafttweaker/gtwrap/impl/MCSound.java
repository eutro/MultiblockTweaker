package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ISound;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class MCSound implements ISound {

    @NotNull
    private final SoundEvent inner;

    public MCSound(@NotNull SoundEvent inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public SoundEvent getInternal() {
        return inner;
    }

}
