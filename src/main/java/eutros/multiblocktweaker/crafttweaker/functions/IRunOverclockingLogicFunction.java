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

    @ZenMethod
    int[] run(@Nonnull IRecipeLogic recipeLogic, @Nonnull IRecipe recipe, boolean negativeEU, int maxOverclocks);

}
