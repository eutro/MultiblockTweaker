package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.gregtech.multiblock.IMultiblockShapeInfo")
@ZenRegister
public interface IMultiblockShapeInfo {

    @NotNull
    MultiblockShapeInfo getInternal();

}
