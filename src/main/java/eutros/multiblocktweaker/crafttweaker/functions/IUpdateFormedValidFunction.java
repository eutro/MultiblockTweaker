package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called every tick if the multiblock is structure formed.
 *
 * @zenClass mods.gregtech.recipe.functions.IUpdateFormedValidFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IUpdateFormedValidFunction")
@ZenRegister
public interface IUpdateFormedValidFunction {

    /**
     * Called every tick if the multiblock is structure formed.
     *
     * @param controller The multiblock controller.
     */
    @ZenMethod
    void run(@Nonnull IControllerTile controller);

}
