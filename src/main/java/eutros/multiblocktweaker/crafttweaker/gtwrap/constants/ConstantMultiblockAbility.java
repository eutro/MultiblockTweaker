package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.capability.IMaintenanceHatch;
import gregtech.api.capability.IMufflerHatch;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityRotorHolder;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.multiblock.MultiblockAbility")
@ZenRegister
public class ConstantMultiblockAbility {

    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility EXPORT_ITEMS = new MCMultiblockAbility<>(MultiblockAbility.EXPORT_ITEMS);
    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility IMPORT_ITEMS = new MCMultiblockAbility<>(MultiblockAbility.IMPORT_ITEMS);
    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility EXPORT_FLUIDS = new MCMultiblockAbility<>(MultiblockAbility.EXPORT_FLUIDS);
    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility IMPORT_FLUIDS = new MCMultiblockAbility<>(MultiblockAbility.IMPORT_FLUIDS);
    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility INPUT_ENERGY = new MCMultiblockAbility<>(MultiblockAbility.INPUT_ENERGY);
    /**
     *
     */
    @ZenProperty
    public static final IMultiblockAbility OUTPUT_ENERGY = new MCMultiblockAbility<>(MultiblockAbility.OUTPUT_ENERGY);

    @ZenProperty
    public static final IMultiblockAbility ABILITY_ROTOR_HOLDER = new MCMultiblockAbility<>(MultiblockAbility.ABILITY_ROTOR_HOLDER);

    @ZenProperty
    public static final IMultiblockAbility PUMP_FLUID_HATCH = new MCMultiblockAbility<>(MultiblockAbility.PUMP_FLUID_HATCH);

    @ZenProperty
    public static final IMultiblockAbility STEAM = new MCMultiblockAbility<>(MultiblockAbility.STEAM);
    @ZenProperty
    public static final IMultiblockAbility STEAM_IMPORT_ITEMS = new MCMultiblockAbility<>(MultiblockAbility.STEAM_IMPORT_ITEMS);
    @ZenProperty
    public static final IMultiblockAbility STEAM_EXPORT_ITEMS = new MCMultiblockAbility<>(MultiblockAbility.STEAM_EXPORT_ITEMS);

    @ZenProperty
    public static final IMultiblockAbility MAINTENANCE_HATCH = new MCMultiblockAbility<>(MultiblockAbility.MAINTENANCE_HATCH);
    @ZenProperty
    public static final IMultiblockAbility MUFFLER_HATCH = new MCMultiblockAbility<>(MultiblockAbility.MUFFLER_HATCH);

}
