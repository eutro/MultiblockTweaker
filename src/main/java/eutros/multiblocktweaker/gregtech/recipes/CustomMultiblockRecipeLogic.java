package eutros.multiblocktweaker.gregtech.recipes;


import crafttweaker.CraftTweakerAPI;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.*;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.*;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.recipes.Recipe;
import org.jetbrains.annotations.NotNull;

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
    public void update() {
        super.update();
        if (multiblock.update != null) {
            try {
                multiblock.update.run(this);
            } catch (RuntimeException t) {
                logFailure("update", t);
                multiblock.update = null;
            }
        }
    }

    @Override
    protected int[] runOverclockingLogic(@NotNull Recipe recipe, boolean negativeEU, int maxOverclocks) {
        if (multiblock.runOverclockingLogic != null) {
            try {
                return multiblock.runOverclockingLogic.run(this, new MCRecipe(recipe), negativeEU, maxOverclocks);
            } catch (RuntimeException t) {
                logFailure("runOverclockingLogic", t);
                multiblock.runOverclockingLogic = null;
            }
        }
        return super.runOverclockingLogic(recipe, negativeEU, maxOverclocks);
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
        if (multiblock.updateWorktable != null) {
            try {
                result = multiblock.updateWorktable.run(this);
            } catch (RuntimeException t) {
                logFailure("updateWorktable", t);
                multiblock.updateWorktable = null;
            }
        }
        if (result) {
            super.updateWorkable();
        }
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        super.setupRecipe(recipe);
        if (multiblock.setupRecipe != null) {
            try {
                multiblock.setupRecipe.run(this, new MCRecipe(recipe));
            } catch (RuntimeException t) {
                logFailure("setupRecipe", t);
                multiblock.setupRecipe = null;
            }
        }
    }

    @Override
    protected void completeRecipe() {
        super.completeRecipe();
        if (multiblock.completeRecipe != null) {
            try {
                multiblock.completeRecipe.run(this);
            } catch (RuntimeException t) {
                logFailure("completeRecipe", t);
                multiblock.completeRecipe = null;
            }
        }
    }

    // CT EXPOSED

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
        return new MCRecipe(previousRecipe);
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
    public boolean drawEnergy(int recipeEUt) {
        return super.drawEnergy(recipeEUt);
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
