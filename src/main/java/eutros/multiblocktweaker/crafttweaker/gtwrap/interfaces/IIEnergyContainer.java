package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IFacing;
import gregtech.api.capability.IEnergyContainer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Used for interacting with IEnergyContainer.
 *
 * @zenClass mods.gregtech.energy.IEnergyContainer
 */
@ZenClass("mods.gregtech.energy.IEnergyContainer")
@ZenRegister
public interface IIEnergyContainer {

    IEnergyContainer getInternal();

    /**
     * This method is basically {@link #changeEnergy(long)}, but it also handles amperes.
     * This method should always be used when energy is passed between blocks.
     *
     * @param enumFacing given side
     * @param voltage  amount of energy packets (energy to add / input voltage)
     * @param amperage packet size (energy to add / input amperage)
     * @return amount of used amperes. 0 if not accepted anything.
     */
    @ZenMethod
    long acceptEnergyFromNetwork(IFacing enumFacing, long voltage, long amperage);

    /**
     *
     * @param enumFacing given side
     * @return if this container accepts energy from the given side
     */
    @ZenMethod
    boolean inputsEnergy(IFacing enumFacing);

    /**
     *
     * @param side given side
     * @return if this container can output energy to the given side
     */
    @ZenMethod
    boolean outputsEnergy(IFacing side);

    /**
     * This changes the amount stored.
     * <b>This should only be used internally</b> (f.e. draining while working or filling while generating).
     * For transfer between blocks use {@link #acceptEnergyFromNetwork(IFacing, long, long)}!!!
     *
     * @param differenceAmount amount of energy to add more than 0 or remove less than 0
     * @return amount of energy added or removed
     */
    @ZenMethod
    long changeEnergy(long differenceAmount);

    /**
     * Adds specified amount of energy to this energy container
     *
     * @param energyToAdd amount of energy to add
     * @return amount of energy added
     */
    @ZenMethod
    long addEnergy(long energyToAdd);

    /**
     * Removes specified amount of energy from this energy container
     *
     * @param energyToRemove amount of energy to remove
     * @return amount of energy removed
     */
    @ZenMethod
    long removeEnergy(long energyToRemove);

    /**
     * @return the maximum amount of energy that can be inserted
     */
    @ZenMethod
    @ZenGetter("energyCanBeInserted")
    long getEnergyCanBeInserted();

    /**
     * @return amount of currently stored energy
     */
    @ZenMethod
    @ZenGetter("energyStored")
    long getEnergyStored();

    /**
     * @return maximum amount of storable energy
     */
    @ZenMethod
    @ZenGetter("energyCapacity")
    long getEnergyCapacity();

    /**
     * @return maximum amount of outputable energy packets per tick
     */
    @ZenMethod
    @ZenGetter("outputAmperage")
    long getOutputAmperage();

    /**
     * @return output energy packet size
     */
    @ZenMethod
    @ZenGetter("outputVoltage")
    long getOutputVoltage();

    /**
     * @return maximum amount of receivable energy packets per tick
     */
    @ZenMethod
    @ZenGetter("inputAmperage")
    long getInputAmperage();

    /**
     * @return output energy packet size
     * Overflowing this value will explode machine.
     */
    @ZenMethod
    @ZenGetter("inputVoltage")
    long getInputVoltage();

    /**
     * @return input eu/s
     */
    @ZenMethod
    @ZenGetter("inputPerSec")
    long getInputPerSec();

    /**
     * @return output eu/s
     */
    @ZenMethod
    @ZenGetter("outputPerSec")
    long getOutputPerSec();

    /**
     * @return true if information like energy capacity should be hidden from TOP.
     * Useful for cables
     */
    @ZenMethod
    @ZenGetter("isOneProbeHidden")
    boolean isOneProbeHidden();

}
