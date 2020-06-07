package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockShapeInfoBuilder;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * Represents an example structure for a multiblock, shown in JEI or as an in-world preview.
 *
 * @see MultiblockShapeInfoBuilder
 */
@ZenClass("mods.gregtech.multiblock.IMultiblockShapeInfo")
@ZenRegister
public interface IMultiblockShapeInfo {

    @NotNull
    MultiblockShapeInfo getInternal();

}
