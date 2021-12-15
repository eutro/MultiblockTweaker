package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.brackethandler.MultiblockAbilityBracketHandler;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * An ability that a multiblock may have, such as fluid import/export, or item import/export.
 *
 * @zenClass mods.gregtech.multiblock.IMultiblockAbility
 * @see MultiblockAbilityBracketHandler
 */
@ZenClass("mods.gregtech.multiblock.IMultiblockAbility")
@ZenRegister
public interface IMultiblockAbility {

    @NotNull
    MultiblockAbility<?> getInternal();

    @ZenMethod
    List<IMetaTileEntity> getMetaTileEntities();
}
