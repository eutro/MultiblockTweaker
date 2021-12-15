package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Called when adding text to the multiblock UI.
 *
 * @zenClass mods.gregtech.multiblock.functions.IDisplayTextFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IAddInformationFunction")
@ZenRegister
public interface IAddInformationFunction {

    /**
     * Implement this with a function.
     *
     * @param controller The multiblock controller.
     * @return A list of tips {@link List<String>} to add to the item.
     */
    @ZenMethod
    List<String> addTips(@Nonnull IControllerTile controller);

}