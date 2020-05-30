package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ZenClass("mods.forge.fluids.IFluidTank")
@ZenRegister
public interface IIFluidTank {

    @Nullable
    @ZenMethod
    ILiquidStack getFluid();

    @ZenMethod
    int getFluidAmount();

    @ZenMethod
    int getCapacity();

    @ZenMethod
    int fill(ILiquidStack fluidStack, boolean b);

    @Nullable
    @ZenMethod
    ILiquidStack drain(int i, boolean b);

}
