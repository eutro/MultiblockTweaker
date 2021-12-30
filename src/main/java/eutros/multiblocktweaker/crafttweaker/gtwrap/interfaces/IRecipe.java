package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.crafttweaker.ChancedEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * A constructed GregTech recipe, with details about inputs, outputs, or custom properties.
 *
 * @zenClass mods.gregtech.recipe.IRecipe
 */
@ZenClass("mods.gregtech.recipe.IRecipe")
@ZenRegister
public interface IRecipe {

    Recipe getInner();

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
     * Get the defined input ingredients.
     *
     * @return The defined input ingredients.
     */
    @ZenMethod
    IIngredient[] getInputs();

    /**
     * Get the defined output items.
     *
     * @return The defined output items.
     */
    @ZenMethod
    IItemStack[] getOutputs();

    /**
     * Get the defined chanced output items.
     *
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
     * Get the defined input fluids.
     *
     * @return The defined input fluids.
     */
    @ZenMethod
    ILiquidStack[] getFluidInputs();

    /**
     * Get whether the given fluid is an ingredient.
     *
     * @param fluid The fluid to check.
     * @return Whether the given fluid is an ingredient.
     */
    @ZenMethod
    boolean hasInputFluid(ILiquidStack fluid);

    /**
     * Get the defined output fluids.
     *
     * @return The defined output fluids.
     */
    @ZenMethod
    ILiquidStack[] getFluidOutputs();

    /**
     * Get the duration of the recipe.
     *
     * @return The duration of the recipe.
     */
    @ZenMethod
    int getDuration();

    /**
     * Get the EU/t cost of the recipe. Negative if the recipe generates energy.
     *
     * @return The EU/t cost of the recipe. Negative if the recipe generates energy.
     */
    @ZenMethod
    int getEUt();

    /**
     * Get whether the recipe is a hidden one.
     *
     * @return Whether the recipe is a hidden one.
     */
    @ZenMethod
    boolean isHidden();

    /**
     * Get all the defined properties.
     *
     * @return All the defined properties.
     */
    @ZenMethod
    String[] getPropertyKeys();

    /**
     * Get the boolean property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The boolean property referenced by the given key.
     */
    @ZenMethod
    boolean getBooleanProperty(String key);

    /**
     * Get the integer property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The integer property referenced by the given key.
     */
    @ZenMethod
    int getIntegerProperty(String key);

    /**
     * Get the long property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The integer property referenced by the given key.
     */
    @ZenMethod
    long getLongProperty(String key);

    /**
     * Get the float property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The integer property referenced by the given key.
     */
    @ZenMethod
    float getFloatProperty(String key);
    
    /**
     * Get the itemStack property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The itemStack property referenced by the given key.
     */
    @ZenMethod
    IItemStack getItemStackProperty(String key);

    /**
     * Get the string property referenced by the given key.
     *
     * @param key The key to reference.
     * @return The string property referenced by the given key.
     */
    @ZenMethod
    String getProperty(String key);

}
