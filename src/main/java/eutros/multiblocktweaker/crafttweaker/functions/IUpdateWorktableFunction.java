package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * A function run when the worktable is updated, that is, every tick.
 *
 * @zenClass mods.gregtech.recipe.functions.IUpdateWorktableFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IUpdateWorktableFunction")
@ZenRegister
public interface IUpdateWorktableFunction {

    /**
     * A function run when the worktable is updated, that is, every tick.
     * <p>
     * Implement this with a function.
     *
     * @param logic The {@link IRecipeLogic} of the multiblock from which this is run.
     * @return Whether to perform the rest of the update logic.
     */
    @ZenMethod
    boolean run(@Nonnull IRecipeLogic logic);

}
