package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * Used for interacting with fluid tanks.
 */
@ZenClass("mods.forge.fluids.IFluidTank")
@ZenRegister
public interface IIFluidTank {

    /**
     * @return The fluid in this tank.
     */
    @Nullable
    @ZenMethod
    ILiquidStack getFluid();

    /**
     * @return How much fluid is in this tank, in mB.
     */
    @ZenMethod
    int getFluidAmount();

    /**
     * @return How much fluid can fit in this tank, in mB.
     */
    @ZenMethod
    int getCapacity();

    /**
     * Try to fill this tank.
     *
     * @param fluidStack The {@link ILiquidStack} to try and fill the tank with.
     * @param doFill     {@code true} to actually do the filling, {@code false} to only simulate.
     * @return How much fluid was inserted, in mB.
     */
    @ZenMethod
    int fill(ILiquidStack fluidStack, boolean doFill);

    /**
     * Try to drain this tank.
     *
     * @param maxDrain How much should be drained at most, in mB.
     * @param doDrain  {@code true} to actually do the draining, {@code false} to only simulate.
     * @return An {@link ILiquidStack} representing what was drained, {@code null} if there was nothing drained.
     */
    @Nullable
    @ZenMethod
    ILiquidStack drain(int maxDrain, boolean doDrain);

}
