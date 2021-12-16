package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeProperty;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


/**
 * Called when drawing info for Recipe Property
 *
 * @zenClass mods.gregtech.recipe.functions.IDrawInfoFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IDrawInfoFunction")
@ZenRegister
public interface IDrawInfoFunction {

    /**
     * Implement this with a function.
     *
     * @param recipeProperty The Recipe Property.
     */
    @ZenMethod
    void drawInfo(CustomRecipeProperty recipeProperty, int x, int y, int color, Object value);

}
