package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import org.jetbrains.annotations.NotNull;

public class MCCubeRenderer implements IICubeRenderer {

    @NotNull
    private final gregtech.api.render.ICubeRenderer inner;

    public MCCubeRenderer(@NotNull gregtech.api.render.ICubeRenderer delegate) {
        this.inner = delegate;
    }

    @NotNull
    @Override
    public gregtech.api.render.ICubeRenderer getInternal() {
        return inner;
    }

}
