package eutros.multiblocktweaker.gregtech.recipes;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.*;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.*;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.GTUtility;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class CustomMultiblockRecipeLogic extends MultiblockRecipeLogic implements IRecipeLogic {

    /**
     * This property will set the chance of the inputs being consumed when a recipe is started.
     */
    public final CustomMultiblock multiblock;

    public CustomMultiblockRecipeLogic(TileControllerCustom controller) {
        super(controller);
        this.multiblock = controller.multiblock;
    }

    // FUNCTIONS

    private void logFailure(String func, Throwable t) {
        CraftTweakerAPI.logError(String.format("Couldn't run %s function of %s.", func, getMetaTile().getMultiblock()), t);
    }

    @Override
    protected int[] performOverclocking( Recipe recipe ) {
        // The maximum number of overclocks is determined by the difference between the tier the recipe is running at,
        // and the maximum tier that the machine can overclock to.
        int recipeTier = GTUtility.getTierByVoltage(recipe.getEUt());
        int maximumOverclockTier = getOverclockForTier(getMaximumOverclockVoltage());

        // At this point, this value should not be negative or zero, as that is filtered out in CheckCanOverclock
        // Subtract 1 to get the desired behavior instead of filtering out LV recipes earlier, as that does not work all the time
        int maxOverclocks = maximumOverclockTier - recipeTier - 1;

        if (multiblock.runOverclockingLogic != null) {
            try {
                return multiblock.runOverclockingLogic.run(this, new MCRecipe(recipe), recipe.getEUt() < 0, maxOverclocks);
            } catch (RuntimeException t) {
                logFailure("runOverclockingLogic", t);
                multiblock.runOverclockingLogic = null;
            }
        }
        return super.runOverclockingLogic(recipe.getRecipePropertyStorage(), recipe.getEUt(), getMaxVoltage(), recipe.getDuration(), maxOverclocks);
    }

    @Override
    public void trySearchNewRecipe() {
        super.trySearchNewRecipe(); // protected.
    }

    @Override
    public void trySearchNewRecipeCombined() {
        super.trySearchNewRecipeCombined();
    }

    @Override
    public void trySearchNewRecipeDistinct() {
        super.trySearchNewRecipeDistinct();
    }

    @Override
    public boolean checkPreviousRecipeDistinct(IIItemHandlerModifiable previousBus) {
        return super.checkPreviousRecipeDistinct(previousBus.getInner());
    }

    @Override
    public boolean prepareRecipeDistinct(IRecipe recipe) {
        return super.prepareRecipeDistinct(recipe.getInner());
    }

    @Override
    public void performMaintenanceMufflerOperations() {
        super.performMaintenanceMufflerOperations();
    }

    @Override
    public void updateWorkable() {
        boolean result = true;
        if (multiblock.updateWorktableFunction != null) {
            try {
                result = multiblock.updateWorktableFunction.run(this);
            } catch (RuntimeException t) {
                logFailure("updateWorktable", t);
                multiblock.updateWorktableFunction = null;
            }
        }
        if (result) {
            super.updateWorkable();
        }
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        boolean result = true;
        if (multiblock.setupRecipeFunction != null) {
            try {
                result = multiblock.setupRecipeFunction.run(this, new MCRecipe(recipe));
            } catch (RuntimeException t) {
                logFailure("setupRecipe", t);
                multiblock.setupRecipeFunction = null;
            }
        }
        if (result) {
            super.setupRecipe(recipe);
        }
    }

    @Override
    protected void completeRecipe() {
        boolean result = true;
        if (multiblock.completeRecipeFunction != null) {
            try {
                result = multiblock.completeRecipeFunction.run(this);
            } catch (RuntimeException t) {
                logFailure("completeRecipe", t);
                multiblock.completeRecipeFunction = null;
            }
        }
        if (result) {
            super.completeRecipe();
        }
    }

    // CT EXPOSED

    @Override
    public void superSetupRecipe(IRecipe recipe) {
        super.setupRecipe(recipe.getInner());
    }

    @Override
    public void superCompleteRecipe() {
        super.completeRecipe();
    }

    @Override
    public void superUpdateWorkable() {
        super.updateWorkable();
    }

    @Override
    public int parallelRecipesPerformed() {
        return parallelRecipesPerformed;
    }

    @Override
    public void ParallelRecipesPerformed(int amount) {
        setParallelRecipesPerformed(amount);
    }

    @Override
    public long overclockVoltage() {
        return getMaximumOverclockVoltage();
    }

    @Override
    public void overclockVoltage(long overclockVoltage) {
        setMaximumOverclockVoltage(overclockVoltage);
    }

    @Override
    public int progressTime() {
        return progressTime;
    }

    @Override
    public void progressTime(int progressTime) {
        this.progressTime = progressTime;
    }

    @Override
    public int maxProgressTime() {
        return getMaxProgress();
    }

    @Override
    public void maxProgressTime(int maxProgressTime) {
        setMaxProgress(maxProgressTime);
    }

    @Override
    public int recipeEUt() {
        return getRecipeEUt();
    }

    @Override
    public void recipeEUt(int eut) {
        this.recipeEUt = eut;
    }

    @Override
    public ILiquidStack[] fluidOutputs() {
        return this.fluidOutputs == null ? null : this.fluidOutputs.stream().map(CraftTweakerMC::getILiquidStack).toArray(ILiquidStack[]::new);
    }

    @Override
    public void fluidOutputs(ILiquidStack[] fluidOutputs) {
        if (fluidOutputs == null) this.fluidOutputs = null;
        else {
            this.fluidOutputs = Arrays.stream(fluidOutputs).map(CraftTweakerMC::getLiquidStack).collect(Collectors.toList());
        }
    }

    @Override
    public IItemStack[] itemOutputs() {
        return this.itemOutputs == null ? null : this.itemOutputs.stream().map(CraftTweakerMC::getIItemStack).toArray(IItemStack[]::new);
    }

    @Override
    public void itemOutputs(IItemStack[] itemOutputs) {
        if (itemOutputs == null) this.itemOutputs = null;
        else {
            this.itemOutputs = NonNullList.create();
            Arrays.stream(itemOutputs).map(CraftTweakerMC::getItemStack).forEach(this.itemOutputs::add);
        }
    }

    @Override
    public boolean wasActiveAndNeedsUpdate() {
        return wasActiveAndNeedsUpdate;
    }

    @Override
    public void wasActiveAndNeedsUpdate(boolean wasActiveAndNeedsUpdate) {
        this.wasActiveAndNeedsUpdate = wasActiveAndNeedsUpdate;
    }

    @Override
    public boolean isOutputsFull() {
        return this.isOutputsFull;
    }

    @Override
    public void isOutputsFull(boolean isOutputsFull) {
        this.isOutputsFull = isOutputsFull;
    }

    @Override
    public boolean invalidInputsForRecipes() {
        return this.invalidInputsForRecipes;
    }

    @Override
    public void invalidInputsForRecipes(boolean invalidInputsForRecipes) {
        this.invalidInputsForRecipes = invalidInputsForRecipes;
    }

    @Override
    public int getLastRecipeIndex() {
        return lastRecipeIndex;
    }

    @Override
    public void setLstRecipeIndex(int index) {
        this.lastRecipeIndex = index;
    }

    @Override
    public IRecipe getPreviousIRecipe() {
        return previousRecipe == null ? null : new MCRecipe(previousRecipe);
    }

    @Override
    public void setPreviousIRecipe(IRecipe previousRecipe) {
        this.previousRecipe = previousRecipe == null ? null : previousRecipe.getInner();
    }

    @Override
    public IControllerTile getMetaTile() {
        return new MCControllerTile((TileControllerCustom) metaTileEntity);
    }

    @Override
    public void setProgress(int val) {
        progressTime = val;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    @Override
    public boolean hasNotEnoughEnergy() {
        return isHasNotEnoughEnergy();
    }

    @Override
    public void hasNotEnoughEnergy(boolean hasNotEnoughEnergy) {
        this.hasNotEnoughEnergy = hasNotEnoughEnergy;
    }

    @Override
    public long getEnergyInputPerSecond() {
        return super.getEnergyInputPerSecond();
    }

    @Override
    public long getEnergyStored() {
        return super.getEnergyStored();
    }

    @Override
    public long getEnergyCapacity() {
        return super.getEnergyCapacity();
    }

    @Override
    public int[] calculateOverclock(IRecipe recipe) {
        return super.calculateOverclock(recipe.getInner());
    }

    @Override
    public boolean drawEnergy(int recipeEUt, boolean simulate) {
        return super.drawEnergy(recipeEUt, simulate);
    }

    @Override
    public long getMaxVoltage() {
        return super.getMaxVoltage();
    }

    @Override
    public IIItemHandlerModifiable getCurrentDistinctInputBus() {
        return new MCIItemHandlerModifiable(super.currentDistinctInputBus);
    }

    @Override
    public List<IIItemHandlerModifiable> getInvalidatedInputList() {
        return super.invalidatedInputList.stream().map(MCIItemHandlerModifiable::new).collect(Collectors.toList());
    }

    @Override
    public List<IIItemHandlerModifiable> getInputBus() {
        return super.getInputBuses().stream().map(MCIItemHandlerModifiable::new).collect(Collectors.toList());
    }

    @Override
    public IIItemHandlerModifiable getInInventory() {
        return new MCIItemHandlerModifiable(super.getInputInventory());
    }

    @Override
    public IIItemHandlerModifiable getOutInventory() {
        return new MCIItemHandlerModifiable(super.getOutputInventory());
    }

    @Override
    public IIMultipleTankHandler getInTank() {
        return new MCIMultipleTankHandler(super.getInputTank());
    }

    @Override
    public IIMultipleTankHandler getOutTank() {
        return new MCIMultipleTankHandler(super.getOutputTank());
    }

    @Override
    public IIEnergyContainer getEnergyHatch() {
        return new MCIEnergyContainer(super.getEnergyContainer());
    }
}
