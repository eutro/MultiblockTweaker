package eutros.multiblocktweaker.gregtech.recipes;


import eutros.multiblocktweaker.crafttweaker.functions.*;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIItemHandlerModifiable;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIMultipleTankHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.*;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;


public class CustomMultiblockRecipeLogic extends MultiblockRecipeLogic implements IRecipeLogic {

    /**
     * This property will set the chance of the inputs being consumed when a recipe is started.
     */
    public static final String CONSUME_CHANCE = "consumeChance";
    @Nullable private final IUpdateFunction update;
    @Nullable private final IUpdateWorktableFunction updateWorktable;
    @Nullable private final ISetupRecipeFunction setupRecipe;
    @Nullable private final ICompleteRecipeFunction completeRecipe;

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

    @Override
    protected boolean setupAndConsumeRecipeInputs(Recipe recipe) {
        int[] resultOverclock = calculateOverclock(recipe.getEUt(), getMaxVoltage(), recipe.getDuration());
        int totalEUt = resultOverclock[0] * resultOverclock[1];
        IItemHandlerModifiable importInventory = getInputInventory();
        IItemHandlerModifiable exportInventory = getOutputInventory();
        IMultipleTankHandler importFluids = getInputTank();
        IMultipleTankHandler exportFluids = getOutputTank();
        return (totalEUt >= 0 ?
                getEnergyStored() >= (totalEUt > getEnergyCapacity() / 2 ? resultOverclock[0] : totalEUt) :
                (getEnergyStored() - resultOverclock[0] <= getEnergyCapacity())) &&
                MetaTileEntity.addItemsToItemHandler(exportInventory, true, recipe.getAllItemOutputs(exportInventory.getSlots())) &&
                MetaTileEntity.addFluidsToFluidHandler(exportFluids, true, recipe.getFluidOutputs()) &&
                recipe.matches(new Random().nextInt(100) <=
                                (recipe.getPropertyKeys().contains(CONSUME_CHANCE) ?
                                 recipe.getIntegerProperty(CONSUME_CHANCE) :
                                 100),
                        importInventory, importFluids);
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

    @Override
    public void update() {
        getUpdate().ifPresent(u -> u.run(this));
    }

    @Override
    public void trySearchNewRecipe() {
        super.trySearchNewRecipe();
    }

    @Override
    public void updateWorkable() {
        if(getUpdateWorktable().map(u -> u.run(this)).orElse(true))
            super.updateWorkable();
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        super.setupRecipe(recipe);
        getSetupRecipe().ifPresent(s -> s.run(this, new MCRecipe(recipe)));
    }

    @Override
    protected void completeRecipe() {
        super.completeRecipe();
        getCompleteRecipe().ifPresent(c -> c.run(this));
    }

    // CT EXPOSED

    @Override
    public IRecipe getPreviousRecipe() {
        return new MCRecipe(previousRecipe);
    }

    @Override
    public IMetaTileEntity getMetaTile() {
        return new MCMetaTileEntity(metaTileEntity);
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

}
