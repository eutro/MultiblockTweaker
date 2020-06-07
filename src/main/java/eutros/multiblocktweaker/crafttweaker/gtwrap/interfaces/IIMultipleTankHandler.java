package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * Used for interacting with multiple fluid tanks.
 */
@ZenClass("mods.gregtech.fluids.IMultipleTankHandler")
@ZenRegister
public interface IIMultipleTankHandler {

    /**
     * @return All of the fluid tanks.
     */
    @ZenMethod
    IIFluidTank[] getFluidTanks();

    /**
     * @return How many fluid tanks there are.
     */
    @ZenMethod
    int getTanks();

    /**
     * @param i The index of the tank.
     * @return The tank at the index.
     */
    @ZenMethod
    IIFluidTank getTankAt(int i);

    /**
     * Try to fill the tank with a fluid.
     *
     * @param fluidStack The fluid to fill with.
     * @param doFill Whether the filling should actually be done.
     * @return How much of the fluid was inserted.
     */
    @ZenMethod
    int fill(ILiquidStack fluidStack, boolean doFill);

    /**
     * Try to drain the tank of a fluid.
     *
     * @param fluidStack The fluid to drain.
     * @param doDrain Whether the draining should actually be done.
     * @return How much of the fluid was drained.
     */
    @Nullable
    @ZenMethod
    ILiquidStack drain(ILiquidStack fluidStack, boolean doDrain);

    /**
     * Try to drain the tank of fluid.
     *
     * @param maxDrain How much fluid to drain.
     * @param doDrain Whether the draining should actually be done.
     * @return How much of the fluid was drained.
     */
    @Nullable
    @ZenMethod
    ILiquidStack drain(int maxDrain, boolean doDrain);

}
