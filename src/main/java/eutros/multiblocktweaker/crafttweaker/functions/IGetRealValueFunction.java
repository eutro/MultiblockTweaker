package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeProperty;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


/**
 * Called when drawing info for Recipe Property
 *
 * @zenClass mods.gregtech.recipe.functions.IDrawInfoFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IGetRealValueFunction")
@ZenRegister
public interface IGetRealValueFunction {

    /**
     * Implement this with a function. mapping Recipe Property tips.
     *
     */
    @ZenMethod
    IFormattedText map(CustomRecipeProperty recipeProperty, String value);

}
