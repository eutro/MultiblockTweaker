package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.data.IData;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIEnergyContainer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIItemHandlerModifiable;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultipleTankHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import org.jetbrains.annotations.NotNull;

public class MCControllerTile extends MCMetaTileEntity implements IControllerTile {

    public MCControllerTile(@NotNull TileControllerCustom inner) {
        super(inner);
    }

    @Override
    @NotNull
    public TileControllerCustom getInternal() {
        return (TileControllerCustom) super.getInternal();
    }

    @Override
    public CustomMultiblock getMultiblock() {
        return getInternal().multiblock;
    }

    @Override
    public IIEnergyContainer getEnergyContainer() {
        return new MCIEnergyContainer(getInternal().getEnergyContainer());
    }


    @Override
    public IIItemHandlerModifiable getInputInventory() {
        return new MCIItemHandlerModifiable(getInternal().getInputInventory());
    }

    @Override
    public IIItemHandlerModifiable getOutputInventory() {
        return new MCIItemHandlerModifiable(getInternal().getOutputInventory());
    }

    @Override
    public IIMultipleTankHandler getInputFluidInventory() {
        return new MCIMultipleTankHandler(getInternal().getInputFluidInventory());
    }

    @Override
    public IIMultipleTankHandler getOutputFluidInventory() {
        return new MCIMultipleTankHandler(getInternal().getOutputFluidInventory());
    }

    @Override
    public void invalidateStructure() {
        getInternal().invalidateStructure();
    }

    @Override
    public void update() {
        getInternal().update();
    }

    @Override
    public Object[] getAbilities(IMultiblockAbility ability) {
        return getInternal().getAbilities(ability.getInternal()).toArray();
    }

    @Override
    public boolean isStructureFormed() {
        return getInternal().isStructureFormed();
    }

    @Override
    public IData getExtraData() {
        return getInternal().persistentData;
    }

    @Override
    public CTTraceabilityPredicate autoAbilities() {
        return new CTTraceabilityPredicate(getInternal().autoAbilities());
    }

    @Override
    public CTTraceabilityPredicate autoAbilities(boolean checkEnergyIn,
                                                 boolean checkMaintainer,
                                                 boolean checkItemIn,
                                                 boolean checkItemOut,
                                                 boolean checkFluidIn,
                                                 boolean checkFluidOut,
                                                 boolean checkMuffler) {
        return new CTTraceabilityPredicate(
                getInternal().autoAbilities(checkEnergyIn,
                        checkMaintainer,
                        checkItemIn,
                        checkItemOut,
                        checkFluidIn,
                        checkFluidOut,
                        checkMuffler));
    }

    @Override
    public CTTraceabilityPredicate selfPredicate() {
        return new CTTraceabilityPredicate(getInternal().selfPredicate());
    }

    @Override
    public void setExtraData(IData data) {
        getInternal().persistentData = data;
    }

}
