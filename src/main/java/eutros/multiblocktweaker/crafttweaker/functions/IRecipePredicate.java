package eutros.multiblocktweaker.crafttweaker.functions;


import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called before a recipe is chosen. Used for additional logic such as blast furnace temperature.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IRecipePredicate")
@ZenRegister
public interface IRecipePredicate {

    /**
     * Implement this with a function.
     *
     * @param controller       The controller this is being called from.
     * @param recipe           The recipe to check.
     * @param consumeIfSuccess Whether the ingredients are to be consumed.
     * @return Whether the recipe is valid and should be gone through with.
     */
    @ZenMethod
    boolean test(@Nonnull IControllerTile controller, @Nonnull IRecipe recipe, boolean consumeIfSuccess);

}

