package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ZenClass("mods.gregtech.recipe.IMultipleTankHandler")
@ZenRegister
public interface IIMultipleTankHandler {

    @ZenMethod
    IIFluidTank[] getFluidTanks();

    @ZenMethod
    int getTanks();

    @ZenMethod
    IIFluidTank getTankAt(int i);

    @ZenMethod
    int fill(ILiquidStack fluidStack, boolean b);

    @Nullable
    @ZenMethod
    ILiquidStack drain(ILiquidStack fluidStack, boolean b);

    @Nullable
    @ZenMethod
    ILiquidStack drain(int i, boolean b);

}
