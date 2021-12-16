package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called every time the worktable updates.
 *
 * @zenClass mods.gregtech.recipe.functions.IUpdateFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IUpdateFormedValidFunction")
@ZenRegister
public interface IUpdateFormedValidFunction {

    /**
     * Called every time the worktable updates.
     * <p>
     * Implement this with a function.
     *
     */
    @ZenMethod
    void run(@Nonnull IControllerTile controller);

}
