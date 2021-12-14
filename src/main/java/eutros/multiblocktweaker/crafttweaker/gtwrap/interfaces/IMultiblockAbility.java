package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantMultiblockAbilities;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * An ability that a multiblock may have, such as fluid import/export, or item import/export.
 *
 * @zenClass mods.gregtech.multiblock.IMultiblockAbility
 * @see ConstantMultiblockAbilities
 */
@ZenClass("mods.gregtech.multiblock.IMultiblockAbility")
@ZenRegister
public interface IMultiblockAbility {

    @NotNull
    MultiblockAbility<?> getInternal();

}
