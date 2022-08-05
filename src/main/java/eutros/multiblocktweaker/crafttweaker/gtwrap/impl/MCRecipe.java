package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.crafttweaker.ChancedEntry;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class MCRecipe implements IRecipe {

    private final Recipe inner;

    public MCRecipe(Recipe inner) {
        this.inner = inner;
    }

    public Recipe getInner() {
        return inner;
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
                .map(ci -> CraftTweakerMC.getIIngredient(ci)
                        .amount(ci.getAmount()))
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
    public IItemStack[] getAllItemOutputs() {
        return CraftTweakerMC.getIItemStacks(inner.getAllItemOutputs());
    }

    @Override
    public ILiquidStack[] getFluidInputs() {
        return inner.getFluidInputs().stream().map(fi -> CraftTweakerMC.getILiquidStack(fi.getInputFluidStack())).toArray(ILiquidStack[]::new);
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
    public boolean getBooleanProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    @Override
    public int getIntegerProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    @Override
    public long getLongProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof Number ? ((Number) value).longValue() : 0;
    }

    @Override
    public float getFloatProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof Number ? ((Number) value).floatValue() : 0;
    }

    @Override
    public IItemStack getItemStackProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof ItemStack ? CraftTweakerMC.getIItemStack((ItemStack) value) : null;
    }

    @Override
    public String getProperty(String key) {
        Object value = inner.getPropertyRaw(key);
        return value instanceof String ? (String) value : null;
    }

}
