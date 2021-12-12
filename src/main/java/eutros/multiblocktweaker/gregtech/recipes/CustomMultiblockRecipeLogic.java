package eutros.multiblocktweaker.gregtech.recipes;


import crafttweaker.CraftTweakerAPI;
import eutros.multiblocktweaker.crafttweaker.functions.ICompleteRecipeFunction;
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
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;


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

//    @Override
//    protected boolean setupAndConsumeRecipeInputs(Recipe recipe) {
//        TileControllerCustom controller = (TileControllerCustom) this.metaTileEntity;
//        if (!controller.checkRecipe(recipe, false)) {
//            return false;
//        }
//
//        int[] resultOverclock = calculateOverclock(recipe.getEUt(), getMaxVoltage(), recipe.getDuration());
//        int totalEUt = resultOverclock[0] * resultOverclock[1];
//        IItemHandlerModifiable importInventory = getInputInventory();
//        IItemHandlerModifiable exportInventory = getOutputInventory();
//        IMultipleTankHandler importFluids = getInputTank();
//        IMultipleTankHandler exportFluids = getOutputTank();
//        boolean ret = (totalEUt >= 0 ?
//                getEnergyStored() >= (totalEUt > getEnergyCapacity() / 2 ? resultOverclock[0] : totalEUt) :
//                (getEnergyStored() - resultOverclock[0] <= getEnergyCapacity())) &&
//                      MetaTileEntity.addItemsToItemHandler(exportInventory, true, recipe.getAllItemOutputs(exportInventory.getSlots())) &&
//                      MetaTileEntity.addFluidsToFluidHandler(exportFluids, true, recipe.getFluidOutputs()) &&
//                      recipe.matches(new Random().nextInt(100) <=
//                                     (recipe.getRecipePropertyStorage().getRecipePropertyKeys().contains(CONSUME_CHANCE) ?
//                                             recipe.getIntegerProperty(CONSUME_CHANCE) :
//                                             100),
//                              importInventory, importFluids);
//
//        if (ret) {
//            controller.checkRecipe(recipe, true);
//        }
//
//        return ret;
//    }

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
    public void trySearchNewRecipe() {
        super.trySearchNewRecipe(); // protected.
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

//    @Override
//    protected boolean checkRecipeInputsDirty(IItemHandler inputs, IMultipleTankHandler fluidInputs) {
//        boolean ret = super.checkRecipeInputsDirty(inputs, fluidInputs);
//        if (ret) return true;
//
//        if (metaTileEntity.getWorld().getWorldTime() % 20 == 0) {
//            // check every 20 ticks, if there is a recipe predicate, they may be checking different things
//            return ((TileControllerCustom) metaTileEntity).multiblock.recipePredicate != null;
//        }
//        return false;
//    }

    // CT EXPOSED

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
