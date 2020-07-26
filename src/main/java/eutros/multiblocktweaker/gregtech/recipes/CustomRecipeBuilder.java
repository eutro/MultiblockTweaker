package eutros.multiblocktweaker.gregtech.recipes;

import com.google.common.collect.ImmutableMap;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CustomRecipeBuilder extends RecipeBuilder<CustomRecipeBuilder> {

    private Map<String, Object> propertyMap = new HashMap<>();

    public CustomRecipeBuilder() {
    }

    public CustomRecipeBuilder(CustomRecipeBuilder builder) {
        super(builder);
        propertyMap = new HashMap<>(builder.propertyMap);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        propertyMap.put(key, value);
        return true;
    }

    @Override
    @NotNull
    public CustomRecipeBuilder copy() {
        return new CustomRecipeBuilder(this);
    }

    @NotNull
    protected EnumValidationResult validate() {
        if(this.EUt == 0) {
            int eUt = EUt;
            EUt = 1;
            super.validate();
            EUt = eUt;
        } else {
            super.validate();
        }

        return this.recipeStatus;
    }

    @Override
    @NotNull
    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(this.finalizeAndValidate(),
                new Recipe(this.inputs,
                        this.outputs,
                        this.chancedOutputs,
                        this.fluidInputs,
                        this.fluidOutputs,
                        ImmutableMap.copyOf(propertyMap),
                        this.duration,
                        this.EUt,
                        this.hidden,
                        this.needsEmptyOutput));
    }

}
