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
     * @param recipeProperty Recipe Property of this recipe.
     * @param value the value stored for this Recipe Property. (will be cast to string)
     * @return formatted text results.
     */
    @ZenMethod
    IFormattedText map(CustomRecipeProperty recipeProperty, String value);

}
