package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called whenever the worktable sets up for a new recipe.
 *
 * @zenClass mods.gregtech.recipe.functions.ISetupRecipeFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.ISetupRecipeFunction")
@ZenRegister
public interface ISetupRecipeFunction {

    /**
     * Called whenever the worktable sets up for a new recipe.
     * <p>
     * Implement this with a function.
     *
     * @param logic  The {@link IRecipeLogic} of the multiblock from which this is run.
     * @param recipe The recipe about to be run.
     */
    @ZenMethod
    void run(@Nonnull IRecipeLogic logic, @Nonnull IRecipe recipe);

}
