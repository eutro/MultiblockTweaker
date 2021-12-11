package eutros.multiblocktweaker.gregtech.recipes;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;
import org.jetbrains.annotations.NotNull;

public class CustomRecipeBuilder extends RecipeBuilder<CustomRecipeBuilder> {

    public CustomRecipeBuilder() {
    }

    public CustomRecipeBuilder(CustomRecipeBuilder builder) {
        super(builder);
    }

    @Override
    @NotNull
    public CustomRecipeBuilder copy() {
        return new CustomRecipeBuilder(this);
    }

    @NotNull
    protected EnumValidationResult validate() {
        if (this.EUt == 0) {
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
                        this.duration,
                        this.EUt,
                        this.hidden));
    }

}
