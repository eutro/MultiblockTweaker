package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

/**
 * Called when the multiblock is formed.
 *
 * Use this to store any extra data from the pattern matching.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IFormStructureFunction")
@ZenRegister
public interface IFormStructureFunction {

    /**
     * Implement this with a function.
     *
     * @param context The context of the structure.
     */
    @ZenMethod
    void formStructure(@Nonnull IPatternMatchContext context);

}
