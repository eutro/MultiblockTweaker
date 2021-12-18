package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
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

    /**
     *
     */
    @ZenMethod
    static int[] heatingCoilOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        return HeatingCoilRecipeLogic.heatingCoilOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, currentTemp, recipeRequiredTemp);
    }

    /**
     *
     */
    @ZenMethod
    static int[] standardOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, double durationDivisor, double voltageMultiplier, int maxOverclocks) {
        return AbstractRecipeLogic.standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, durationDivisor, voltageMultiplier, maxOverclocks);
    }

    // super method
    /**
     * call the method of the parent class.
     */
    @ZenMethod
    void superSetupRecipe(IRecipe recipe);

    /**
     * call the method of the parent class.
     */
    @ZenMethod
    void superCompleteRecipe();

    /**
     * call the method of the parent class.
     */
    @ZenMethod
    void superUpdateWorkable();

    /////////

    /**
     *
     */
    @ZenGetter
    int parallelRecipesPerformed();

    /**
     *
     */
    @ZenSetter
    void ParallelRecipesPerformed(int amount);

    /**
     *
     */
    @ZenGetter
    long overclockVoltage();

    /**
     *
     */
    @ZenSetter
    void overclockVoltage(long overclockVoltage);

    /**
     *
     */
    @ZenGetter
    int progressTime();

    /**
     *
     */
    @ZenSetter
    void progressTime(int progressTime);

    /**
     *
     */
    @ZenGetter
    int maxProgressTime();

    /**
     *
     */
    @ZenSetter
    void maxProgressTime(int maxProgressTime);

    /**
     *
     */
    @ZenGetter
    int recipeEUt();

    /**
     *
     */
    @ZenSetter
    void recipeEUt(int eut);

    /**
     *
     */
    @ZenGetter
    ILiquidStack[] fluidOutputs();

    /**
     *
     */
    @ZenSetter
    void fluidOutputs(ILiquidStack[] fluidOutputs);

    /**
     *
     */
    @ZenGetter
    IItemStack[] itemOutputs();

    /**
     *
     */
    @ZenSetter
    void itemOutputs(IItemStack[] itemOutputs);

    /**
     *
     */
    @ZenGetter
    boolean wasActiveAndNeedsUpdate();

    /**
     *
     */
    @ZenSetter
    void wasActiveAndNeedsUpdate(boolean wasActiveAndNeedsUpdate);

    /**
     *
     */
    @ZenGetter
    boolean isOutputsFull();

    /**
     *
     */
    @ZenSetter
    void isOutputsFull(boolean isOutputsFull);

    /**
     *
     */
    @ZenGetter
    boolean invalidInputsForRecipes();

    /**
     *
     */
    @ZenSetter
    void invalidInputsForRecipes(boolean invalidInputsForRecipes);

    /**
     *
     */
    @ZenGetter("lastRecipeIndex")
    int getLastRecipeIndex();

    /**
     *
     */
    @ZenSetter("lastRecipeIndex")
    void setLstRecipeIndex(int index);

    /**
     *
     */
    @ZenGetter("previousRecipe")
    IRecipe getPreviousIRecipe();

    /**
     *
     */
    @ZenSetter("previousRecipe")
    void setPreviousIRecipe(IRecipe previousRecipe);

    /**
     *
     */
    @ZenGetter("metaTileEntity")
    IControllerTile getMetaTile();

    /**
     *
     */
    @ZenGetter("workingEnabled")
    boolean isWorkingEnabled();

    /**
     *
     */
    @ZenMethod
    @ZenSetter("workingEnabled")
    void setWorkingEnabled(boolean val);

    /**
     *
     */
    @ZenMethod
    @ZenGetter("recipeProgress")
    int getProgress();

    /**
     *
     */
    @ZenMethod
    @ZenSetter("recipeProgress")
    void setProgress(int val);

    /**
     *
     */
    @ZenMethod
    @ZenGetter("maxProgress")
    int getMaxProgress();

    /**
     *
     */
    @ZenMethod
    @ZenSetter("maxProgress")
    void setMaxProgress(int val);

    /**
     *
     */
    @ZenMethod
    @ZenGetter
    boolean isActive();

    /**
     *
     */
    @ZenMethod
    @ZenSetter("isActive")
    void setActive(boolean active);

    /**
     *
     */
    @ZenMethod
    @ZenSetter("isAllowOverclocking")
    void setAllowOverclocking(boolean val);

    /**
     *
     */
    @ZenMethod
    @ZenGetter
    boolean isWorking();

    /**
     *
     */
    @ZenMethod
    @ZenGetter()
    boolean isAllowOverclocking();

    /**
     *
     */
    @ZenMethod
    @ZenGetter
    boolean hasNotEnoughEnergy();

    /**
     *
     */
    @ZenMethod
    @ZenSetter
    void hasNotEnoughEnergy(boolean hasNotEnoughEnergy);

    /**
     *
     */
    @ZenMethod
    @ZenGetter("energyInputPerSecond")
    long getEnergyInputPerSecond();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("energyStored")
    long getEnergyStored();

    /**
     *
     */
    @ZenGetter("energyCapacity")
    long getEnergyCapacity();

    /**
     *
     */
    @ZenMethod
    int[] calculateOverclock(IRecipe recipe);

    /**
     *
     */
    @ZenMethod
    boolean drawEnergy(int recipeEUt);

    /**
     *
     */
    @ZenMethod
    void update();

    /**
     *
     */
    @ZenMethod
    void invalidate();

    /**
     *
     */
    @ZenMethod
    void trySearchNewRecipe();

    /**
     *
     */
    @ZenMethod
    void trySearchNewRecipeCombined();

    /**
     *
     */
    @ZenMethod
    void trySearchNewRecipeDistinct();

    /**
     *
     */
    @ZenMethod
    void invalidateInputs();

    /**
     *
     */
    @ZenMethod
    boolean checkPreviousRecipeDistinct(IIItemHandlerModifiable previousBus);

    /**
     *
     */
    @ZenMethod
    boolean prepareRecipeDistinct(IRecipe recipe);

    /**
     *
     */
    @ZenMethod
    void performMaintenanceMufflerOperations();

    /**
     *
     */
    @ZenMethod
    void forceRecipeRecheck();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("recipeMap")
    RecipeMap<?> getRecipeMap();

    /**
     *
     */
    @ZenMethod
    @ZenGetter("maxVoltage")
    long getMaxVoltage();

    /**
     *
     */
    @ZenGetter("currentDistinctInputBus")
    IIItemHandlerModifiable getCurrentDistinctInputBus();

    /**
     *
     */
    @ZenGetter("invalidatedInputList")
    List<IIItemHandlerModifiable> getInvalidatedInputList();

    /**
     *
     */
    @ZenGetter("inputBus")
    List<IIItemHandlerModifiable> getInputBus();

    /**
     *
     */
    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInInventory();

    /**
     *
     */
    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutInventory();

    /**
     *
     */
    @ZenGetter("inputTank")
    IIMultipleTankHandler getInTank();

    /**
     *
     */
    @ZenGetter("outputTank")
    IIMultipleTankHandler getOutTank();

    /**
     *
     */
    @ZenGetter("evergyHatchr")
    IIEnergyContainer getEnergyHatch();

}
