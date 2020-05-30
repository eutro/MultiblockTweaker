package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIFluidTank;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultipleTankHandler;
import gregtech.api.capability.IMultipleTankHandler;

import javax.annotation.Nullable;

public class MCIMultipleTankHandler implements IIMultipleTankHandler {

    private final IMultipleTankHandler inner;

    public MCIMultipleTankHandler(IMultipleTankHandler inner) {
        this.inner = inner;
    }

    @Override
    public IIFluidTank[] getFluidTanks() {
        return inner.getFluidTanks().stream().map(MCFluidTank::new).toArray(IIFluidTank[]::new);
    }

    @Override
    public int getTanks() {
        return inner.getTanks();
    }

    @Override
    public IIFluidTank getTankAt(int i) {
        return new MCFluidTank(inner.getTankAt(i));
    }

    @Override
    public int fill(ILiquidStack fluidStack, boolean b) {
        return inner.fill(CraftTweakerMC.getLiquidStack(fluidStack), b);
    }

    @Override
    @Nullable
    public ILiquidStack drain(ILiquidStack fluidStack, boolean b) {
        return CraftTweakerMC.getILiquidStack(inner.drain(CraftTweakerMC.getLiquidStack(fluidStack), b));
    }

    @Override
    @Nullable
    public ILiquidStack drain(int i, boolean b) {
        return CraftTweakerMC.getILiquidStack(inner.drain(i, b));
    }

}
