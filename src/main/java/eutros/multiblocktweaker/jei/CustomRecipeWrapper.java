package eutros.multiblocktweaker.jei;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class CustomRecipeWrapper extends GTRecipeWrapper {

    public static final int LINE_DIFF = 10;
    private final Recipe recipe;

    public CustomRecipeWrapper(RecipeMap<?> recipeMap, Recipe recipe) {
        super(recipeMap, recipe);
        this.recipe = recipe;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int yPosition = recipeHeight - getPropertyListHeight();
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.total", Math.abs((long) recipe.getEUt()) * recipe.getDuration()), 0, yPosition, 0x111111);
        minecraft.fontRenderer.drawString(I18n.format(recipe.getEUt() >= 0 ? "gregtech.recipe.eu" : "gregtech.recipe.eu_inverted", Math.abs(recipe.getEUt())), 0, yPosition += LINE_DIFF, 0x111111);
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.duration", recipe.getDuration() / 20f), 0, yPosition += LINE_DIFF, 0x111111);
        for (String propertyKey : recipe.getPropertyKeys()) {
            minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe." + propertyKey,
                    recipe.<Object>getProperty(propertyKey)), 0, yPosition += LINE_DIFF, 0x111111);
        }
    }

    private int getPropertyListHeight() {
        return (recipe.getPropertyKeys().size() + 3) * LINE_DIFF;
    }

}
