package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.formatting.IFormattedText;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultiblockPart;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Called when get the texture of the controller and parts.
 *
 * @zenClass mods.gregtech.multiblock.functions.IGetBaseTextureFunction
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.functions.IGetBaseTextureFunction")
@ZenRegister
public interface IGetBaseTextureFunction {

    /**
     * Implement this with a function.
     *
     * @param part asking for the texture of the controller if the part is null.
     * @return the texture of the controller and parts.
     */
    @ZenMethod
    IICubeRenderer get(IControllerTile controllerTile, @Nullable IIMultiblockPart part);

}
