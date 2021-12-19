package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * A predicate for meta tile entities.
 *
 * @zenClass mods.gregtech.predicate.IMTEPredicate
 */
@FunctionalInterface
@ZenClass("mods.gregtech.predicate.IMTEPredicate")
@ZenRegister
public interface IMTEPredicate {
    /**
     * Check the tile at a given position.
     * @param state block world state
     * @param mte will be null if the block not the {@link IMetaTileEntity}
     * @return checking result;
     */
    boolean test(IBlockWorldState state, IMetaTileEntity mte);
}
