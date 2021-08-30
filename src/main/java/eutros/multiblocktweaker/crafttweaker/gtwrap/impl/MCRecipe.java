package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.crafttweaker.ChancedEntry;

import java.util.Arrays;

public class MCRecipe implements IRecipe {

    private final Recipe inner;

    public MCRecipe(Recipe inner) {
        this.inner = inner;
    }

    @Override
    public boolean matches(boolean consumeIfSuccessful, IItemStack[] inputs, ILiquidStack[] fluidInputs) {
        return inner.matches(consumeIfSuccessful,
                Arrays.asList(CraftTweakerMC.getItemStacks(inputs)),
                Arrays.asList(CraftTweakerMC.getLiquidStacks(fluidInputs)));
    }

    @Override
    public IIngredient[] getInputs() {
        return inner.getInputs().stream()
                .map(ci -> CraftTweakerMC.getIIngredient(ci.getIngredient())
                        .amount(ci.getCount()))
                .toArray(IIngredient[]::new);
    }

    @Override
    public IItemStack[] getOutputs() {
        return CraftTweakerMC.getIItemStacks(inner.getOutputs());
    }

    @Override
    public ChancedEntry[] getChancedOutputs() {
        return inner.getChancedOutputs()
                .stream()
                .map(c -> new ChancedEntry(CraftTweakerMC.getIItemStack(c.getItemStack()), c.getChance(), c.getBoostPerTier()))
                .toArray(ChancedEntry[]::new);
    }

    @Override
    public IItemStack[] getAllItemOutputs(int maxOutputSlots) {
        return CraftTweakerMC.getIItemStacks(inner.getAllItemOutputs(maxOutputSlots));
    }

    @Override
    public ILiquidStack[] getFluidInputs() {
        return inner.getFluidInputs().stream().map(CraftTweakerMC::getILiquidStack).toArray(ILiquidStack[]::new);
    }

    @Override
    public boolean hasInputFluid(ILiquidStack fluid) {
        return inner.hasInputFluid(CraftTweakerMC.getLiquidStack(fluid));
    }

    @Override
    public ILiquidStack[] getFluidOutputs() {
        return inner.getFluidOutputs().stream().map(CraftTweakerMC::getILiquidStack).toArray(ILiquidStack[]::new);
    }

    @Override
    public int getDuration() {
        return inner.getDuration();
    }

    @Override
    public int getEUt() {
        return inner.getEUt();
    }

    @Override
    public boolean isHidden() {
        return inner.isHidden();
    }

    @Override
    public String[] getPropertyKeys() {
        return inner.getPropertyKeys().toArray(new String[0]);
    }

    @Override
    @Deprecated
    public boolean getBooleanProperty(String key) {
        return (boolean) inner.getPropertyRaw(key);
    }

    @Override
    @Deprecated
    public int getIntegerProperty(String key) {
        return (int) inner.getPropertyRaw(key);
    }

    @Override
    @Deprecated
    public String getProperty(String key) {
        return (String) inner.getPropertyRaw(key);
    }

}
