package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import stanhebben.zenscript.annotations.ZenClass;

@FunctionalInterface
@ZenClass("mods.gregtech.predicate.IPredicate")
@ZenRegister
public interface IPredicate {
    /**
     * Whether the block at that position pass checking {@link eutros.multiblocktweaker.gregtech.renderer.IBlockStateRenderer}.
     * @param blockWorldState block world state.
     * @return checking result.
     */
    boolean test(IBlockWorldState blockWorldState);
}
