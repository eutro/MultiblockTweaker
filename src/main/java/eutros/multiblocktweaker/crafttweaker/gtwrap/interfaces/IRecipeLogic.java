package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

/**
 * Used to perform the recipe logic.
 *
 * @zenClass mods.gregtech.recipe.IRecipeLogic
 */
@ZenClass("mods.gregtech.recipe.IRecipeLogic")
@ZenRegister
public interface IRecipeLogic {

    @ZenMethod
    @ZenGetter("previousRecipe")
    IRecipe getPreviousRecipe();

    @ZenGetter("metaTileEntity")
    IControllerTile getMetaTile();

    @ZenMethod
    @ZenGetter("workingEnabled")
    boolean isWorkingEnabled();

    @ZenMethod
    @ZenSetter("workingEnabled")
    void setWorkingEnabled(boolean val);

    @ZenMethod
    @ZenGetter("recipeProgress")
    int getProgress();

    @ZenMethod
    @ZenSetter("recipeProgress")
    void setProgress(int val);

    @ZenMethod
    @ZenGetter("maxProgress")
    int getMaxProgress();

    @ZenMethod
    @ZenSetter("maxProgress")
    void setMaxProgress(int val);

    @ZenMethod
    boolean isActive();

    @ZenMethod
    void setAllowOverclocking(boolean val);

    @ZenMethod
    boolean isAllowOverclocking();

    @ZenMethod
    boolean isHasNotEnoughEnergy();

    @ZenMethod
    long getEnergyStored();

    @ZenMethod
    long getEnergyCapacity();

    @ZenMethod
    boolean drawEnergy(int recipeEUt);

    @ZenMethod
    void update();

    @ZenMethod
    void trySearchNewRecipe();

    @ZenMethod
    void forceRecipeRecheck();

    @ZenMethod
    long getMaxVoltage();

    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInInventory();

    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutInventory();

    @ZenGetter("inputTank")
    IIMultipleTankHandler getInTank();

    @ZenGetter("outputTank")
    IIMultipleTankHandler getOutTank();

}
