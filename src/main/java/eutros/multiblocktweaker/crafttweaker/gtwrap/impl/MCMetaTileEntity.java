package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import org.jetbrains.annotations.NotNull;

public class MCMetaTileEntity implements IMetaTileEntity {

    @NotNull
    private final MetaTileEntity inner;

    public MCMetaTileEntity(@NotNull MetaTileEntity inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public MetaTileEntity getInternal() {
        return inner;
    }

}
