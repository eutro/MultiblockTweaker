package eutros.multiblocktweaker.gregtech.recipes;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.functions.IDrawInfoFunction;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.recipe.CustomRecipeProperty")
@ZenRegister
public class CustomRecipeProperty extends RecipeProperty<Object> {

    @ZenProperty
    IDrawInfoFunction drawInfoFunction;
    @ZenProperty
    boolean isHidden;

    public CustomRecipeProperty(String key) {
        super(key, Object.class);
    }

    @Override
    @ZenMethod
    @ZenGetter("key")
    public String getKey() {
        return super.getKey();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        if (drawInfoFunction != null) {
            try {
                drawInfoFunction.drawInfo(this, x, y, color, value);
            } catch (RuntimeException t) {
                CraftTweakerAPI.logError(String.format("Couldn't run %s function of RecipeProperty %s.", drawInfoFunction, getKey()), t);
                drawInfoFunction = null;
            }
        }
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public boolean isOfType(Class<?> otherType) {
        return false;
    }
}
