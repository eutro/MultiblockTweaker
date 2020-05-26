package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.gregtech.multiblock.IMultiblockAbility")
@ZenRegister
public interface IMultiblockAbility {

    @NotNull
    MultiblockAbility<?> getInternal();

}
