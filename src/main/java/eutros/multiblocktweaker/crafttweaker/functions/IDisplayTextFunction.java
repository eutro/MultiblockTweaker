package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * Called when adding text to the multiblock GUI.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IDisplayTextFunction")
@ZenRegister
public interface IDisplayTextFunction {

    @ZenMethod
    List<IFormattedText> addDisplayText();

}
