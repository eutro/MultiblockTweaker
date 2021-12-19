package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Called to create the pattern structure.
 *
 * @zenClass mods.gregtech.multiblock.functions.IPatternBuilderFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IPatternBuilderFunction")
@ZenRegister
public interface IPatternBuilderFunction {

    /**
     * Implement this with a function.
     *
     * @param controller The multiblock controller.
     * @return A list of {@link IFormattedText}s to add to the UI.
     */
    @ZenMethod
    IBlockPattern build(@Nonnull IControllerTile controller);

}
