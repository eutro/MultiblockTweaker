package eutros.multiblocktweaker.gregtech.recipes;


import crafttweaker.CraftTweakerAPI;
import eutros.multiblocktweaker.crafttweaker.functions.ICompleteRecipeFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IRunOverclockingLogicFunction;
import eutros.multiblocktweaker.crafttweaker.functions.ISetupRecipeFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IUpdateFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IUpdateWorktableFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIEnergyContainer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIItemHandlerModifiable;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIMultipleTankHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.*;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CustomMultiblockRecipeLogic extends MultiblockRecipeLogic implements IRecipeLogic {

    /**
     * This property will set the chance of the inputs being consumed when a recipe is started.
     */
    public static final String CONSUME_CHANCE = "consumeChance";
    @Nullable
    private IUpdateFunction update;
    @Nullable
    private IUpdateWorktableFunction updateWorktable;
    @Nullable
    private ISetupRecipeFunction setupRecipe;
    @Nullable
    private ICompleteRecipeFunction completeRecipe;
    @Nullable
    private IRunOverclockingLogicFunction runOverclockingLogic;

    public CustomMultiblockRecipeLogic(RecipeMapMultiblockController tileEntity,
                                       @Nullable IUpdateFunction update,
                                       @Nullable IUpdateWorktableFunction updateWorktable,
                                       @Nullable ISetupRecipeFunction setupRecipe,
                                       @Nullable ICompleteRecipeFunction completeRecipe) {
        super(tileEntity);
        this.update = update;
        this.updateWorktable = updateWorktable;
        this.setupRecipe = setupRecipe;
        this.completeRecipe = completeRecipe;
    }

    // FUNCTION GETTERS

    public Optional<IUpdateFunction> getUpdate() {
        return Optional.ofNullable(update);
    }

    public Optional<IUpdateWorktableFunction> getUpdateWorktable() {
        return Optional.ofNullable(updateWorktable);
    }

    public Optional<ISetupRecipeFunction> getSetupRecipe() {
        return Optional.ofNullable(setupRecipe);
    }

    public Optional<ICompleteRecipeFunction> getCompleteRecipe() {
        return Optional.ofNullable(completeRecipe);
    }

    // FUNCTIONS

    private void logFailure(String func, Throwable t) {
        CraftTweakerAPI.logError(String.format("Couldn't run %s function of %s.", func, getMetaTile().getMultiblock()), t);
    }

    @Override
    public void update() {
        super.update();
        getUpdate().ifPresent(u -> {
            try {
                u.run(this);
            } catch (RuntimeException t) {
                logFailure("update", t);
                update = null;
            }
        });
    }

    @Override
    protected int[] runOverclockingLogic(@NotNull Recipe recipe, boolean negativeEU, int maxOverclocks) {
        if (runOverclockingLogic != null) {
            try {
                return runOverclockingLogic.run(new MCRecipe(recipe), negativeEU, maxOverclocks);
            } catch (RuntimeException t) {
                logFailure("runOverclockingLogic", t);
                runOverclockingLogic = null;
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
        if (getUpdateWorktable().map(u -> {
            try {
                return u.run(this);
            } catch (RuntimeException t) {
                logFailure("updateWorktable", t);
                updateWorktable = null;
                return true;
            }
        }).orElse(true)) {
            super.updateWorkable();
        }
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        super.setupRecipe(recipe);
        getSetupRecipe().ifPresent(s -> {
            try {
                s.run(this, new MCRecipe(recipe));
            } catch (RuntimeException t) {
                logFailure("setupRecipe", t);
                setupRecipe = null;
            }
        });
    }

    @Override
    protected void completeRecipe() {
        super.completeRecipe();
        getCompleteRecipe().ifPresent(c -> {
            try {
                c.run(this);
            } catch (RuntimeException t) {
                logFailure("completeRecipe", t);
                completeRecipe = null;
            }
        });
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
