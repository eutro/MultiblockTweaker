package eutros.multiblocktweaker.gregtech.recipes;

import com.google.common.collect.ImmutableMap;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
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
        builder.propertyMap = new HashMap<>(propertyMap);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        propertyMap.put(key, value);
        return true;
    }

    @Override
    public @NotNull CustomRecipeBuilder copy() {
        return new CustomRecipeBuilder(this);
    }

    @Override
    public @NotNull ValidationResult<Recipe> build() {
        return ValidationResult.newResult(this.finalizeAndValidate(),
                new Recipe(this.inputs,
                        this.outputs,
                        this.chancedOutputs,
                        this.fluidInputs,
                        this.fluidOutputs,
                        ImmutableMap.copyOf(propertyMap),
                        this.duration,
                        this.EUt,
                        this.hidden));
    }

}
