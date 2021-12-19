package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Called when adding text to the ItemStack of this controller.
 *
 * @zenClass mods.gregtech.multiblock.functions.IAddInformationFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IAddInformationFunction")
@ZenRegister
public interface IAddInformationFunction {

    /**
     * Implement this with a function.
     *
     * @param controller The multiblock controller.
     * @return A list of tips sting to add to the item.
     */
    @ZenMethod
    String[] addTips(@Nonnull IControllerTile controller);

}
