package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.brackethandler.MultiblockAbilityBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMultiblockAbility;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    /**
     * Get a meta tile entity by its Name.
     *
     * @param name The Name of the meta block Ability.
     * @return The meta tile entity referenced by the ID, or null.
     */
    @ZenMethod
    @Nullable
    static IMultiblockAbility byName(@NotNull String name) {
        if (MultiblockAbility.NAME_REGISTRY.containsKey(name)) {
            return new MCMultiblockAbility<>(MultiblockAbility.NAME_REGISTRY.get(name));
        }
        return null;
    }

    @NotNull
    MultiblockAbility<?> getInternal();

    @ZenMethod
    List<IMetaTileEntity> getMetaTileEntities();
}
