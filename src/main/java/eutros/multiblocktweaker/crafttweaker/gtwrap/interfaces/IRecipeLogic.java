package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.recipe.IRecipeLogic")
@ZenRegister
public interface IRecipeLogic {

    @ZenMethod
    @ZenGetter("previousRecipe")
    IRecipe getPreviousRecipe();

    @ZenMethod("getMetaTileEntity")
    IMetaTileEntity getMetaTile();

    @ZenMethod
    boolean isWorkingEnabled();

    @ZenMethod
    void setWorkingEnabled(boolean val);

    @ZenMethod
    int getProgress();

    @ZenMethod
    int getMaxProgress();

    @ZenMethod
    boolean isActive();

    @ZenMethod
    long getEnergyStored();

    @ZenMethod
    long getEnergyCapacity();

    @ZenMethod
    boolean drawEnergy(int recipeEUt);

    @ZenMethod
    long getMaxVoltage();

    @ZenMethod("getInputInventory")
    IIItemHandlerModifiable getInInventory();

    @ZenMethod("getOutputInventory")
    IIItemHandlerModifiable getOutInventory();

    @ZenMethod("getInputTank")
    IIMultipleTankHandler getInTank();

    @ZenMethod("getOutputTank")
    IIMultipleTankHandler getOutTank();

}
