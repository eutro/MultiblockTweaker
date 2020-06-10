package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Called every time the worktable updates, probably.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IUpdateFunction")
@ZenRegister
public interface IUpdateFunction {

    /**
     * Implement this with a function.
     */
    @ZenMethod
    void run(IRecipeLogic logic);

}
