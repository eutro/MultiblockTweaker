package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.FluidKey;
import gregtech.api.util.GTHashMaps;
import gregtech.api.util.OverlayedFluidHandler;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used for interacting with multiple fluid tanks.
 *
 * @zenClass mods.gregtech.fluids.IMultipleTankHandler
 */
@ZenClass("mods.gregtech.fluids.IMultipleTankHandler")
@IterableSimple("mods.forge.fluids.IFluidTank")
@ZenRegister
public interface IIMultipleTankHandler extends Iterable<IIFluidTank> {

    IMultipleTankHandler getInner();

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
     * @param doFill     Whether the filling should actually be done.
     * @return How much of the fluid was inserted.
     */
    @ZenMethod
    int fill(ILiquidStack fluidStack, boolean doFill);

    /**
     * Try to drain the tank of a fluid.
     *
     * @param fluidStack The fluid to drain.
     * @param doDrain    Whether the draining should actually be done.
     * @return How much of the fluid was drained.
     */
    @Nullable
    @ZenMethod
    ILiquidStack drain(ILiquidStack fluidStack, boolean doDrain);

    /**
     * Try to drain the tank of fluid.
     *
     * @param maxDrain How much fluid to drain.
     * @param doDrain  Whether the draining should actually be done.
     * @return How much of the fluid was drained.
     */
    @Nullable
    @ZenMethod
    ILiquidStack drain(int maxDrain, boolean doDrain);

    /**
     * Simulates the insertion of fluid into a target fluid handler, then optionally performs the insertion.
     * <br /><br />
     * Simulating will not modify any of the input parameters. Insertion will either succeed completely, or fail
     * without modifying anything.
     * This method should be called with {@code simulate} {@code true} first, then {@code simulate} {@code false},
     * only if it returned {@code true}.
     *
     * @param simulate     whether to simulate ({@code true}) or actually perform the insertion ({@code false})
     * @param fluidStacks  the items to insert into {@code fluidHandler}.
     * @return {@code true} if the insertion succeeded, {@code false} otherwise.
     */
    @ZenMethod
    default boolean addFluids(boolean simulate, List<ILiquidStack> fluidStacks) {
        IMultipleTankHandler handler = this.getInner();
        List<FluidStack> fluids = fluidStacks.stream().map(CraftTweakerMC::getLiquidStack).collect(Collectors.toList());
        if (simulate) {
            OverlayedFluidHandler overlayedFluidHandler = new OverlayedFluidHandler(handler);
            HashMap<FluidKey, Integer> fluidKeyMap = GTHashMaps.fromFluidCollection(fluids);
            for (Map.Entry<FluidKey, Integer> entry : fluidKeyMap.entrySet()) {
                int amountToInsert = entry.getValue();
                int inserted = overlayedFluidHandler.insertStackedFluidKey(entry.getKey(), amountToInsert);
                if (inserted != amountToInsert) {
                    return false;
                }
            }
            return true;
        }

        fluids.forEach(fluidStack -> handler.fill(fluidStack, true));
        return true;
    }
}
