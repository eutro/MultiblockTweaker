package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * A function run when the worktable is updated, that is, every tick.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IUpdateWorktableFunction")
@ZenRegister
public interface IUpdateWorktableFunction {

    /**
     * Implement this with a function.
     *
     * @return Whether to perform the rest of the update logic.
     */
    @ZenMethod
    boolean run(@Nonnull IRecipeLogic logic);

}
