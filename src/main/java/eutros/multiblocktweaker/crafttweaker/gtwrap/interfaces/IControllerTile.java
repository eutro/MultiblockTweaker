package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.IControllerTile")
@ZenRegister
public interface IControllerTile extends IMetaTileEntity {

    @Override
    @NotNull TileControllerCustom getInternal();

    /**
     * @return The {@link CustomMultiblock} that this is the controller for.
     */
    @ZenMethod
    @ZenGetter("multiblock")
    CustomMultiblock getMultiblock();

    @ZenMethod
    @ZenGetter("energyContainer")
    IIEnergyContainer getEnergyContainer();

    @ZenMethod
    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInputInventory();

    @ZenMethod
    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutputInventory();

    @ZenMethod
    @ZenGetter("inputFluidInventory")
    IIMultipleTankHandler getInputFluidInventory();

    @ZenMethod
    @ZenGetter("outputFluidInventory")
    IIMultipleTankHandler getOutputFluidInventory();

    @ZenMethod
    void invalidateStructure();

    @ZenMethod
    void update();

    @ZenMethod
    Object[] getAbilities(IMultiblockAbility ability);

    @ZenMethod
    boolean isStructureFormed();

}
