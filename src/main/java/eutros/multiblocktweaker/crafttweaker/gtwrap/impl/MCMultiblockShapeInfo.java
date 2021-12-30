package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockShapeInfo;
import gregtech.api.pattern.MultiblockShapeInfo;
import org.jetbrains.annotations.NotNull;

public class MCMultiblockShapeInfo implements IMultiblockShapeInfo {

    @NotNull
    private final MultiblockShapeInfo inner;

    public MCMultiblockShapeInfo(@NotNull MultiblockShapeInfo inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public MultiblockShapeInfo getInternal() {
        return inner;
    }

}
