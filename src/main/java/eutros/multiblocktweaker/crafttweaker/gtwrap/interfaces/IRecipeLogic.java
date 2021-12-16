package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.HeatingCoilRecipeLogic;
import gregtech.api.recipes.RecipeMap;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import java.util.List;

/**
 * Used to perform the recipe logic.
 *
 * @zenClass mods.gregtech.recipe.IRecipeLogic
 */
@ZenClass("mods.gregtech.recipe.IRecipeLogic")
@ZenRegister
public interface IRecipeLogic {

    @ZenMethod
    static int[] heatingCoilOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        return HeatingCoilRecipeLogic.heatingCoilOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, currentTemp, recipeRequiredTemp);
    }

    @ZenMethod
    static int[] standardOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, double durationDivisor, double voltageMultiplier, int maxOverclocks) {
        return AbstractRecipeLogic.standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, durationDivisor, voltageMultiplier, maxOverclocks);
    }

    @ZenMethod
    @ZenGetter("lastRecipeIndex")
    int getLastRecipeIndex();

    @ZenMethod
    @ZenSetter("lastRecipeIndex")
    void setLstRecipeIndex(int index);

    @ZenMethod
    @ZenGetter("previousRecipe")
    IRecipe getPreviousIRecipe();

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
    long getEnergyInputPerSecond();

    @ZenMethod
    long getEnergyStored();

    @ZenMethod
    long getEnergyCapacity();

    @ZenMethod
    boolean drawEnergy(int recipeEUt);

    @ZenMethod
    void update();

    @ZenMethod
    void invalidate();

    @ZenMethod
    void trySearchNewRecipe();

    @ZenMethod
    void trySearchNewRecipeCombined();

    @ZenMethod
    void trySearchNewRecipeDistinct();

    @ZenMethod
    void invalidateInputs();

    @ZenMethod
    boolean checkPreviousRecipeDistinct(IIItemHandlerModifiable previousBus);

    @ZenMethod
    boolean prepareRecipeDistinct(IRecipe recipe);

    @ZenMethod
    void performMaintenanceMufflerOperations();

    @ZenMethod
    void forceRecipeRecheck();

    @ZenMethod
    RecipeMap<?> getRecipeMap();

    @ZenMethod
    long getMaxVoltage();

    @ZenGetter("currentDistinctInputBus")
    IIItemHandlerModifiable getCurrentDistinctInputBus();

    @ZenGetter("invalidatedInputList")
    List<IIItemHandlerModifiable> getInvalidatedInputList();

    @ZenGetter("inputBus")
    List<IIItemHandlerModifiable> getInputBus();

    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInInventory();

    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutInventory();

    @ZenGetter("inputTank")
    IIMultipleTankHandler getInTank();

    @ZenGetter("outputTank")
    IIMultipleTankHandler getOutTank();

    @ZenGetter("evergyHatchr")
    IIEnergyContainer getEnergyHatch();

}
