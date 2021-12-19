package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * A predicate for a block at a specific position.
 *
 * @zenClass mods.gregtech.predicate.ICandidates
 */
@FunctionalInterface
@ZenClass("mods.gregtech.predicate.IPredicate")
@ZenRegister
public interface IPredicate {
    /**
     * Test the block at a position. {@link eutros.multiblocktweaker.gregtech.renderer.IBlockStateRenderer}.
     * @param blockWorldState block world state.
     * @return checking result.
     */
    boolean test(IBlockWorldState blockWorldState);
}
