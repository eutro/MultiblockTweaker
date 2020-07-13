package eutros.multiblocktweaker.crafttweaker.expanders;

import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.crafttweaker.CTRecipeBuilder;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenExpansion("mods.gregtech.recipe.RecipeBuilder")
public class ExpandRecipeBuilder {

    public static RecipeBuilder<?> getBackingBuilder(CTRecipeBuilder self) {
        return ReflectionHelper.getPrivate(CTRecipeBuilder.class, "backingBuilder", self);
    }

    @ZenMethod
    public static CTRecipeBuilder property(CTRecipeBuilder self, String key, Object value) {
        RecipeBuilder<?> bb = getBackingBuilder(self);
        boolean applied = bb.applyProperty(key, value);
        if (!applied) {
            throw new IllegalArgumentException("Property " + key + " cannot be applied to recipe type " + bb.getClass().getSimpleName());
        } else {
            return self;
        }
    }

}
