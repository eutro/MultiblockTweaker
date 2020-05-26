package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;

public class MCMultiblockAbility<T> implements IMultiblockAbility {

    private final MultiblockAbility<T> inner;

    public MCMultiblockAbility(MultiblockAbility<T> inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public MultiblockAbility<T> getInternal() {
        return inner;
    }

}
