package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.pattern.TraceabilityPredicate;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

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

    /**
     * Store extra data for retrieval with {@link #getExtraData()}
     * <p>
     * This will be stored in the tile's NBT when the world is saved,
     * so will persist even through restarts.
     *
     * @param data The extra data to store on the controller.
     * @zenSetter extraData
     */
    @ZenMethod
    @ZenSetter("extraData")
    void setExtraData(IData data);

    /**
     * Retrieve extra data stored with {@link #setExtraData(IData)}
     *
     * @return The extra data stored on this controller.
     */
    @ZenMethod
    @ZenGetter("extraData")
    IData getExtraData();

    @ZenMethod
    @ZenGetter("autoAbilities")
    CTTraceabilityPredicate autoAbilities();

    @ZenMethod
    CTTraceabilityPredicate autoAbilities(boolean checkEnergyIn,
                                        boolean checkMaintainer,
                                        boolean checkItemIn,
                                        boolean checkItemOut,
                                        boolean checkFluidIn,
                                        boolean checkFluidOut,
                                        boolean checkMuffler);
    @ZenMethod
    @ZenGetter("selfPredicate")
    CTTraceabilityPredicate selfPredicate();
}
