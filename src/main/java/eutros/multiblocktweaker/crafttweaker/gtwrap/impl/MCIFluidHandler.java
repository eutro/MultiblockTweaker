package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIFluidHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIFluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class MCIFluidHandler implements IIFluidHandler {

    private final IFluidHandler inner;

    public MCIFluidHandler(IFluidHandler inner) {
        this.inner = inner;
    }


    @Override
    public int fill(ILiquidStack fluidStack, boolean doFill) {
        return inner.fill(CraftTweakerMC.getLiquidStack(fluidStack), doFill);
    }

    @Override
    public ILiquidStack drain(ILiquidStack resource, boolean doDrain) {
        return CraftTweakerMC.getILiquidStack(inner.drain(CraftTweakerMC.getLiquidStack(resource), doDrain));
    }

    @Override
    @Nullable
    public ILiquidStack drain(int maxDrain, boolean doDrain) {
        return CraftTweakerMC.getILiquidStack(inner.drain(maxDrain, doDrain));
    }

}
