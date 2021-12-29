package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.brackethandler.MultiblockAbilityBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenCaster;
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
     * Get an ability by its name.
     *
     * @param name The Name of the meta block Ability.
     * @return The ability by the Name, or null.
     */
    @ZenMethod
    @Nullable
    static IMultiblockAbility byName(@NotNull String name) {
        if (MultiblockAbility.NAME_REGISTRY.containsKey(name)) {
            return new MCMultiblockAbility<>(MultiblockAbility.NAME_REGISTRY.get(name.toLowerCase()));
        }
        return null;
    }

    @NotNull
    MultiblockAbility<?> getInternal();

    /**
     * Get all meta tile entities with this ability.
     *
     * @return The meta tile entities.
     */
    @ZenMethod
    List<IMetaTileEntity> getMetaTileEntities();

    @ZenCaster
    CTTraceabilityPredicate castCTPredicate();
}
