package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import gregtech.integration.crafttweaker.recipe.CTRecipe;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

@ZenRegister
@ZenExpansion("mods.gregtech.recipe.RecipeMap")
public class ExpandRecipeMap {
    /**
     * Copy all recipes from the given recipe map into this recipe map.
     *
     * @param self This, a recipe map.
     * @param src  The source recipe map to copy from.
     */
    @ZenMethod
    public static void copyAll(RecipeMap<?> self, RecipeMap<?> src) {
        for (Recipe recipe : src.getRecipeList()) {
            copyRecipe(self, recipe);
        }
    }

    /**
     * Copy a single recipe into this recipe map.
     *
     * @param self   This, a recipe map.
     * @param recipe The recipe to copy to this recipe map.
     */
    @ZenMethod
    public static void copyOne(RecipeMap<?> self, CTRecipe recipe) {
        copyRecipe(self, ReflectionHelper.getPrivate(CTRecipe.class, "backingRecipe", recipe));
    }

    private static void copyRecipe(RecipeMap<?> self, Recipe recipe) {
        RecipeBuilder<?> rb = self.recipeBuilder();
        rb.inputs(recipe.getInputs().toArray(new GTRecipeInput[0]));
        rb.outputs(recipe.getOutputs());
        for (Recipe.ChanceEntry chancedOutput : recipe.getChancedOutputs()) {
            rb.chancedOutput(chancedOutput.getItemStack(), chancedOutput.getChance(), chancedOutput.getBoostPerTier());
        }
        rb.fluidInputs(recipe.getFluidInputs());
        rb.fluidOutputs(recipe.getFluidOutputs());
        rb.duration(recipe.getDuration());
        rb.EUt(recipe.getEUt());
        if (recipe.isHidden()) rb.hidden();
        for (Map.Entry<RecipeProperty<?>, Object> entry : recipe.getRecipePropertyStorage().getRecipeProperties()) {
            rb.applyProperty(entry.getKey().getKey(), entry.getValue());
        }
        rb.buildAndRegister();
    }
}
