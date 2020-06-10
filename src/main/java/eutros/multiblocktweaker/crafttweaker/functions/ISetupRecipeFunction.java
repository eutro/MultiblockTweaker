package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Called whenever the worktable sets up for a new recipe.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.ISetupRecipeFunction")
@ZenRegister
public interface ISetupRecipeFunction {

    /**
     * Implement this with a function.
     */
    @ZenMethod
    void run(IRecipeLogic logic, IRecipe recipe);

}
