package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called when adding text to the multiblock UI.
 *
 * @zenClass mods.gregtech.multiblock.functions.IDisplayTextFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IDisplayTextFunction")
@ZenRegister
public interface IDisplayTextFunction {

    /**
     * Implement this with a function.
     *
     * @param controller The multiblock controller.
     * @return A list of {@link IFormattedText}s to add to the UI.
     */
    @ZenMethod
    IFormattedText[] addDisplayText(@Nonnull IControllerTile controller);

}
