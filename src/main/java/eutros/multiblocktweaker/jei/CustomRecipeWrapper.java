package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.gregtech.recipes.RecipeMapMultiblock;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class CustomRecipeWrapper extends GTRecipeWrapper {

    public static final int LINE_DIFF = 10;
    public static final int BOTTOM_TEXT_OFFSET = RecipeMapMultiblock.DEFAULT_HEIGHT - 70;
    private final Recipe recipe;

    public CustomRecipeWrapper(RecipeMap<?> recipeMap, Recipe recipe) {
        super(recipeMap, recipe);
        this.recipe = recipe;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = recipeHeight * 3 / 2 - BOTTOM_TEXT_OFFSET;

        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.total", Math.abs((long) recipe.getEUt()) * recipe.getDuration()), 0, y, 0x111111);
        y += LINE_DIFF;

        minecraft.fontRenderer.drawString(I18n.format(recipe.getEUt() >= 0 ? "gregtech.recipe.eu" : "gregtech.recipe.eu_inverted", Math.abs(recipe.getEUt())), 0, y, 0x111111);
        y += LINE_DIFF;

        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe.duration", recipe.getDuration() / 20f), 0, y, 0x111111);
        y += LINE_DIFF;

        for (String propertyKey : recipe.getPropertyKeys()) {
            minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe." + propertyKey,
                    recipe.<Object>getProperty(propertyKey)), 0, y, 0x111111);
            y += LINE_DIFF;
        }
    }

}
