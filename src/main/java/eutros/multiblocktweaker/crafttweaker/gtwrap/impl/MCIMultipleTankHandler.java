package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIFluidTank;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultipleTankHandler;
import gregtech.api.capability.IMultipleTankHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;

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
    public int fill(ILiquidStack fluidStack, boolean doFill) {
        return inner.fill(CraftTweakerMC.getLiquidStack(fluidStack), doFill);
    }

    @Override
    @Nullable
    public ILiquidStack drain(ILiquidStack fluidStack, boolean doDrain) {
        return CraftTweakerMC.getILiquidStack(inner.drain(CraftTweakerMC.getLiquidStack(fluidStack), doDrain));
    }

    @Override
    @Nullable
    public ILiquidStack drain(int maxDrain, boolean doDrain) {
        return CraftTweakerMC.getILiquidStack(inner.drain(maxDrain, doDrain));
    }

    @NotNull
    @Override
    public Iterator<IIFluidTank> iterator() {
        return inner.getFluidTanks().stream()
                .map(MCFluidTank::new)
                .map(IIFluidTank.class::cast)
                .iterator();
    }

}
