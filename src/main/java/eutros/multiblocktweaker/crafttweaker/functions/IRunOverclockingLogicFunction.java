package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called every time a recipe completes.
 *
 * @zenClass mods.gregtech.recipe.functions.ICompleteRecipeFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IRunOverclockingLogicFunction")
@ZenRegister
public interface IRunOverclockingLogicFunction {

    /**
     * Converts the recipe's values into ones used for overclocking.
     *
     * @param recipeLogic the recipe logic
     * @param recipe the current recipe
     * @param negativeEU the negative of Eu to perform
     * @param maxOverclocks the maximum amount of overclocks to perform
     * @return an int array of {OverclockedEUt, OverclockedDuration}
     */
    @ZenMethod
    int[] run(@Nonnull IRecipeLogic recipeLogic, @Nonnull IRecipe recipe, boolean negativeEU, int maxOverclocks);

}
