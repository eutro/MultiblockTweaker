package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import gregtech.api.recipes.crafttweaker.ChancedEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * A constructed GregTech recipe, with details about inputs, outputs, or custom properties.
 */
@ZenClass("mods.gregtech.recipe.IRecipe")
@ZenRegister
public interface IRecipe {

    /**
     * Check whether the recipe matches the given ingredients.
     *
     * @param consumeIfSuccessful Whether to consume the ingredients on success.
     * @param inputs              The item inputs.
     * @param fluidInputs         The fluid inputs.
     * @return Whether the recipe matches.
     */
    @ZenMethod
    boolean matches(boolean consumeIfSuccessful, IItemStack[] inputs, ILiquidStack[] fluidInputs);

    /**
     * @return The defined input ingredients.
     */
    @ZenMethod
    IIngredient[] getInputs();

    /**
     * @return The defined output items.
     */
    @ZenMethod
    IItemStack[] getOutputs();

    /**
     * @return The defined chanced output items.
     */
    @ZenMethod
    ChancedEntry[] getChancedOutputs();

    /**
     * Get all the outputs of a recipe, limited to {@code maxOutputSlots}, including all chanced outputs possible.
     *
     * @param maxOutputSlots The max outputs.
     * @return All the item outputs of the recipe.
     */
    @ZenMethod
    IItemStack[] getAllItemOutputs(int maxOutputSlots);

    /**
     * @return The defined input fluids.
     */
    @ZenMethod
    ILiquidStack[] getFluidInputs();

    /**
     * @param fluid The fluid to check.
     * @return Whether the given fluid is an ingredient.
     */
    @ZenMethod
    boolean hasInputFluid(ILiquidStack fluid);

    /**
     * @return The defined output fluids.
     */
    @ZenMethod
    ILiquidStack[] getFluidOutputs();

    /**
     * @return The duration of the recipe.
     */
    @ZenMethod
    int getDuration();

    /**
     * @return The EU/t cost of the recipe. Negative if the recipe generates energy.
     */
    @ZenMethod
    int getEUt();

    /**
     * @return Whether the recipe is a hidden one.
     */
    @ZenMethod
    boolean isHidden();

    /**
     * @return Get all the defined properties.
     */
    @ZenMethod
    String[] getPropertyKeys();

    /**
     * @param key The key to reference.
     * @return Get the boolean property referenced by the given key.
     */
    @ZenMethod
    boolean getBooleanProperty(String key);

    /**
     * @param key The key to reference.
     * @return Get the integer property referenced by the given key.
     */
    @ZenMethod
    int getIntegerProperty(String key);

    /**
     * @param key The key to reference.
     * @return Get the string property referenced by the given key.
     */
    @ZenMethod
    String getProperty(String key);

}
