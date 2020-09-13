package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * Called when the controller block is about to be removed.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IRemovalFunction")
@ZenRegister
public interface IRemovalFunction {

    /**
     * Implement this with a function.
     *
     * @param controller The controller about to be removed.
     */
    void onRemoval(IControllerTile controller);

}
