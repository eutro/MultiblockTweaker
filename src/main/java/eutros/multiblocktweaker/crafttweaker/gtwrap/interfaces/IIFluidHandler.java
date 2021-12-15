package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * Used for interacting with fluid tanks.
 *
 * @zenClass mods.forge.fluids.IFluidTank
 */
@ZenClass("mods.forge.fluids.IIFluidHandler")
@ZenRegister
public interface IIFluidHandler {

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param doFill   If false, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    int fill(ILiquidStack resource, boolean doFill);

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Nullable
    ILiquidStack drain(ILiquidStack resource, boolean doDrain);

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p/>
     * This method is not Fluid-sensitive.
     *
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Nullable
    ILiquidStack drain(int maxDrain, boolean doDrain);

}
