package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.logic.OverclockingLogic;
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
     * Heating coil overclocking logic. Using to calculate the overclocking of the coil block.
     *
     * @param recipeEUt recipe EU cost
     * @param maximumVoltage recipe maximum voltage
     * @param recipeDuration recipe duration
     * @param maxOverclocks the maximum amount of overclocks to perform
     * @param currentTemp current temperature
     * @param recipeRequiredTemp recipe temperature
     * @return an int array of {OverclockedEUt, OverclockedDuration}
     */
    @ZenMethod
    static int[] heatingCoilOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        return OverclockingLogic.heatingCoilOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, currentTemp, recipeRequiredTemp);
    }

    /**
     * Standard overclocking logic. Using to calculate the overclocking as the default overclocking result.
     *
     * @param recipeEUt recipe EU cost
     * @param maximumVoltage recipe maximum voltage
     * @param recipeDuration recipe duration
     * @param durationDivisor recipe duration divisor
     * @param voltageMultiplier recipe voltage multiplier
     * @param maxOverclocks he maximum amount of overclocks to perform
     * @return an int array of {OverclockedEUt, OverclockedDuration}
     */
    @ZenMethod
    static int[] standardOverclockingLogic(int recipeEUt, long maximumVoltage, int recipeDuration, double durationDivisor, double voltageMultiplier, int maxOverclocks) {
        return OverclockingLogic.standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, durationDivisor, voltageMultiplier);
    }

    // super method

    /**
     * Call the method of the parent class.
     *
     * @param recipe current recipe.
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
     * @return //TODO
     */
    @ZenGetter
    int parallelRecipesPerformed();

    /**
     * //TODO
     * 
     * @param amount //TODO
     */
    @ZenSetter
    void ParallelRecipesPerformed(int amount);

    /**
     * Get overclocking voltage.
     *
     * @return  overclocking voltage
     */
    @ZenGetter
    long overclockVoltage();

    /**
     * Set overclocking voltage.
     *
     * @param overclockVoltage overclocking voltage
     */
    @ZenSetter
    void overclockVoltage(long overclockVoltage);

    /**
     * Get progress time.
     *
     * @return progress time
     */
    @ZenGetter
    int progressTime();

    /**
     * Set progress time.
     *
     * @param progressTime progress time
     */
    @ZenSetter
    void progressTime(int progressTime);

    /**
     * Get max progress time.
     *
     * @return max progress time
     */
    @ZenGetter
    int maxProgressTime();

    /**
     * Set max progress time.
     *
     * @param maxProgressTime max progress time
     */
    @ZenSetter
    void maxProgressTime(int maxProgressTime);

    /**
     * Get recipe eut.
     *
     * @return recipe eut
     */
    @ZenGetter
    int recipeEUt();

    /**
     * Set recipe eut.
     *
     * @param eut recipe eut
     */
    @ZenSetter
    void recipeEUt(int eut);

    /**
     * Get recipe fluid outputs.
     *
     * @return recipe fluid outputs.
     */
    @ZenGetter
    ILiquidStack[] fluidOutputs();

    /**
     * Set recipe fluid outputs.
     *
     * @param fluidOutputs recipe fluid outputs.
     */
    @ZenSetter
    void fluidOutputs(ILiquidStack[] fluidOutputs);

    /**
     * Get recipe item outputs.
     *
     * @return recipe item outputs.
     */
    @ZenGetter
    IItemStack[] itemOutputs();

    /**
     * Set recipe item outputs.
     *
     * @param itemOutputs recipe item outputs.
     */
    @ZenSetter
    void itemOutputs(IItemStack[] itemOutputs);

    /**
     * Get recipe logic wasActiveAndNeedsUpdate.
     *
     * @return  recipe logic wasActiveAndNeedsUpdate.
     */
    @ZenGetter
    boolean wasActiveAndNeedsUpdate();

    /**
     * Set recipe logic wasActiveAndNeedsUpdate.
     *
     * @param wasActiveAndNeedsUpdate wasActiveAndNeedsUpdate.
     */
    @ZenSetter
    void wasActiveAndNeedsUpdate(boolean wasActiveAndNeedsUpdate);

    /**
     * Get recipe logic isOutputsFull.
     *
     * @return isOutputsFull.
     */
    @ZenGetter
    boolean isOutputsFull();

    /**
     * Set recipe logic isOutputsFull.
     *
     * @param isOutputsFull isOutputsFull.
     */
    @ZenSetter
    void isOutputsFull(boolean isOutputsFull);

    /**
     * Get recipe logic invalidInputsForRecipes.
     *
     * @return   invalidInputsForRecipes.
     */
    @ZenGetter
    boolean invalidInputsForRecipes();

    /**
     * Set recipe logic invalidInputsForRecipes.
     *
     * @param invalidInputsForRecipes invalidInputsForRecipes.
     */
    @ZenSetter
    void invalidInputsForRecipes(boolean invalidInputsForRecipes);

    /**
     * Get recipe logic lastRecipeIndex.
     *
     * @return  lastRecipeIndex.
     */
    @ZenGetter("lastRecipeIndex")
    int getLastRecipeIndex();

    /**
     * Set recipe logic lastRecipeIndex.
     *
     * @param index lastRecipeIndex.
     */
    @ZenSetter("lastRecipeIndex")
    void setLstRecipeIndex(int index);

    /**
     * Get recipe logic previousRecipe.
     *
     * @return  previousRecipe.
     */
    @ZenGetter("previousRecipe")
    IRecipe getPreviousIRecipe();

    /**
     * Set recipe logic previousRecipe.
     *
     * @param previousRecipe previousRecipe.
     */
    @ZenSetter("previousRecipe")
    void setPreviousIRecipe(IRecipe previousRecipe);

    /**
     * Get the controller of this recipe logic.
     *
     * @return controller.
     */
    @ZenGetter("metaTileEntity")
    IControllerTile getMetaTile();

    /**
     * Get isWorkingEnabled of this recipe logic.
     *
     * @return isWorkingEnabled.
     */
    @ZenGetter("workingEnabled")
    boolean isWorkingEnabled();

    /**
     * Set isWorkingEnabled of this recipe logic.
     *
     * @param val isWorkingEnabled.
     */
    @ZenMethod
    @ZenSetter("workingEnabled")
    void setWorkingEnabled(boolean val);

    /**
     * Get recipeProgress of this recipe logic.
     *
     * @return recipeProgress.
     */
    @ZenMethod
    @ZenGetter("recipeProgress")
    int getProgress();

    /**
     * Set recipeProgress of this recipe logic.
     *
     * @param val recipeProgress.
     */
    @ZenMethod
    @ZenSetter("recipeProgress")
    void setProgress(int val);

    /**
     * Get maxProgress of this recipe logic.
     *
     * @return maxProgress.
     */
    @ZenMethod
    @ZenGetter("maxProgress")
    int getMaxProgress();

    /**
     * Set maxProgress of this recipe logic.
     *
     * @param val maxProgress.
     */
    @ZenMethod
    @ZenSetter("maxProgress")
    void setMaxProgress(int val);

    /**
     * Get isActive of this recipe logic.
     *
     * @return isActive.
     */
    @ZenMethod
    @ZenGetter
    boolean isActive();

    /**
     * Set isActive of this recipe logic.
     *
     * @param active isActive.
     */
    @ZenMethod
    @ZenSetter("isActive")
    void setActive(boolean active);

    /**
     * Set isAllowOverclocking of this recipe logic.
     *
     * @param val isAllowOverclocking.
     */
    @ZenMethod
    @ZenSetter("isAllowOverclocking")
    void setAllowOverclocking(boolean val);

    /**
     * Get isWorking of this recipe logic.
     *
     * @return isWorking.
     */
    @ZenMethod
    @ZenGetter
    boolean isWorking();

    /**
     * Get isAllowOverclocking of this recipe logic.
     *
     * @return isAllowOverclocking.
     */
    @ZenMethod
    @ZenGetter()
    boolean isAllowOverclocking();

    /**
     * Get hasNotEnoughEnergy of this recipe logic.
     *
     * @return hasNotEnoughEnergy.
     */
    @ZenMethod
    @ZenGetter
    boolean hasNotEnoughEnergy();

    /**
     * Set hasNotEnoughEnergy of this recipe logic.
     *
     * @param hasNotEnoughEnergy hasNotEnoughEnergy.
     */
    @ZenMethod
    @ZenSetter
    void hasNotEnoughEnergy(boolean hasNotEnoughEnergy);

    /**
     * Get energyInputPerSecond of this recipe logic.
     *
     * @return energyInputPerSecond.
     */
    @ZenMethod
    @ZenGetter("energyInputPerSecond")
    long getEnergyInputPerSecond();

    /**
     * Get energyStored of this recipe logic.
     *
     * @return energyStored.
     */
    @ZenMethod
    @ZenGetter("energyStored")
    long getEnergyStored();

    /**
     * Get energyCapacity of this recipe logic.
     *
     * @return energyCapacity.
     */
    @ZenGetter("energyCapacity")
    long getEnergyCapacity();

    /**
     * Using to calculate the overclocking of this recipe.
     *
     * @param recipe current recipe
     * @return an int array of {OverclockedEUt, OverclockedDuration}
     */
    @ZenMethod
    int[] calculateOverclock(IRecipe recipe);

    /**
     * Draw energy from the energy container.
     *
     * @param recipeEUt recipe eu cost
     * @param simulate is simulate
     * @return actual eu consumption
     */
    @ZenMethod
    boolean drawEnergy(int recipeEUt, boolean simulate);

    /**
     * Called every tick
     */
    @ZenMethod
    void update();

    /**
     * Called when the recipe logic is invalidated.
     */
    @ZenMethod
    void invalidate();

    /**
     * Called when the recipe logic try to find an available recipe according to the RecipeMap.
     */
    @ZenMethod
    void trySearchNewRecipe();

    /**
     * //TODO
     */
    @ZenMethod
    void trySearchNewRecipeCombined();

    /**
     * //TODO
     */
    @ZenMethod
    void trySearchNewRecipeDistinct();

    /**
     * Called to invalidate inputs.
     */
    @ZenMethod
    void invalidateInputs();

    /**
     * //TODO
     * @param previousBus //TODO
     * @return //TODO
     */
    @ZenMethod
    boolean checkPreviousRecipeDistinct(IIItemHandlerModifiable previousBus);

    /**
     * Prepare recipe distinct before setup recipe.
     *
     * @param recipe recipe found
     * @return is ana available recipe.
     */
    @ZenMethod
    boolean prepareRecipeDistinct(IRecipe recipe);

    /**
     * //TODO
     */
    @ZenMethod
    void performMaintenanceMufflerOperations();

    /**
     * Force recipe re-check.
     */
    @ZenMethod
    void forceRecipeRecheck();

    /**
     * Get the RecipeMap of it.
     *
     * @return RecipeMap
     */
    @ZenMethod
    @ZenGetter("recipeMap")
    RecipeMap<?> getRecipeMap();

    /**
     *
     * @return maxVoltage
     */
    @ZenMethod
    @ZenGetter("maxVoltage")
    long getMaxVoltage();

    /**
     *
     * @return currentDistinctInputBus
     */
    @ZenGetter("currentDistinctInputBus")
    IIItemHandlerModifiable getCurrentDistinctInputBus();

    /**
     *
     * @return invalidatedInputList
     */
    @ZenGetter("invalidatedInputList")
    List<IIItemHandlerModifiable> getInvalidatedInputList();

    /**
     *
     * @return inputBus
     */
    @ZenGetter("inputBus")
    List<IIItemHandlerModifiable> getInputBus();

    /**
     *
     * @return inputInventory
     */
    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInInventory();

    /**
     *
     * @return outputInventory
     */
    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutInventory();

    /**
     *
     * @return inputTank
     */
    @ZenGetter("inputTank")
    IIMultipleTankHandler getInTank();

    /**
     *
     * @return outputTank
     */
    @ZenGetter("outputTank")
    IIMultipleTankHandler getOutTank();

    /**
     *
     * @return evergyHatch
     */
    @ZenGetter("evergyHatch")
    IIEnergyContainer getEnergyHatch();

}
