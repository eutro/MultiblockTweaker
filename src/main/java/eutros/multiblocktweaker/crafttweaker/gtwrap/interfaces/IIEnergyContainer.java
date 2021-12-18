package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IFacing;
import gregtech.api.capability.IEnergyContainer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.energy.IEnergyContainer")
@ZenRegister
public interface IIEnergyContainer {

    IEnergyContainer getInternal();

    /**
     *
     */
    @ZenMethod
    long acceptEnergyFromNetwork(IFacing enumFacing, long voltage, long amperage);

    /**
     *
     */
    @ZenMethod
    boolean inputsEnergy(IFacing enumFacing);

    /**
     *
     */
    @ZenMethod
    boolean outputsEnergy(IFacing side);

    /**
     *
     */
    @ZenMethod
    long changeEnergy(long differenceAmount);

    /**
     *
     */
    @ZenMethod
    long addEnergy(long energyToAdd);

    /**
     *
     */
    @ZenMethod
    long removeEnergy(long energyToRemove);

    /**
     *
     */
    @ZenMethod
    @ZenGetter("energyCanBeInserted")
    long getEnergyCanBeInserted();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("energyStored")
    long getEnergyStored();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("energyCapacity")
    long getEnergyCapacity();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("outputAmperage")
    long getOutputAmperage();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("outputVoltage")
    long getOutputVoltage();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("inputAmperage")
    long getInputAmperage();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("inputVoltage")
    long getInputVoltage();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("inputPerSec")
    long getInputPerSec();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("outputPerSec")
    long getOutputPerSec();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("isOneProbeHidden")
    boolean isOneProbeHidden();

}
