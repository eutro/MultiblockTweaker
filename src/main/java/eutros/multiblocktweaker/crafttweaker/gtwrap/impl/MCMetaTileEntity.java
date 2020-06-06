package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
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

    public IWorld getWorld() {
        return CraftTweakerMC.getIWorld(inner.getWorld());
    }

    public IBlockPos getPos() {
        return CraftTweakerMC.getIBlockPos(inner.getPos());
    }

    public long getTimer() {
        return inner.getTimer();
    }

    public String getMetaName() {
        return inner.getMetaName();
    }

    public String getMetaFullName() {
        return inner.getMetaFullName();
    }

    public IFacing getFrontFacing() {
        return CraftTweakerMC.getIFacing(inner.getFrontFacing());
    }

}
