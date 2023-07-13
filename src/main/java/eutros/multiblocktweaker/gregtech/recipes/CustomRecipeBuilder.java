package eutros.multiblocktweaker.gregtech.recipes;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.util.ValidationResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CustomRecipeBuilder extends RecipeBuilder<CustomRecipeBuilder> {

    Map<CustomRecipeProperty, Object> properties;

    private CustomRecipeBuilder(CustomRecipeBuilder builder, Map<CustomRecipeProperty, Object> recipeProperties) {
        super(builder);
        this.properties = recipeProperties;
    }

    public CustomRecipeBuilder(CustomRecipeProperty[] recipeProperties) {
        if (recipeProperties.length > 0) {
            properties = new HashMap<>();
            for (CustomRecipeProperty recipeProperty : recipeProperties) {
                properties.put(recipeProperty, null);
            }
        }
    }

    @Override
    @NotNull
    public CustomRecipeBuilder copy() {
        return new CustomRecipeBuilder(this, properties);
    }

    @Override
    @NotNull
    public ValidationResult<Recipe> build() {
        Recipe recipe = new Recipe(this.inputs,
                this.outputs,
                this.chancedOutputs,
                this.fluidInputs,
                this.fluidOutputs,
                this.duration,
                this.EUt,
                this.hidden,
                this.isCTRecipe,
                this.recipePropertyStorage,
                this.category);
        return ValidationResult.newResult(finalizeAndValidate(), recipe);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        if (properties == null) {
            return false;
        }
        CustomRecipeProperty property = properties.keySet().stream().filter(p->p.getKey().equals(key)).findFirst().orElse(null);
        if (property == null) {
            return false;
        }
        properties.put(property, value);
        return true;
    }

}
