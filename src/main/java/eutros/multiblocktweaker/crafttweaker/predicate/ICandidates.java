package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * A candidates getter of this predicate.
 *
 * @zenClass mods.gregtech.predicate.ICandidates
 */
@FunctionalInterface
@ZenClass("mods.gregtech.predicate.ICandidates")
@ZenRegister
public interface ICandidates {
    /**
     * Get candidates of this predicate.
     *
     * @return available candidates.
     */
    IBlockInfo[] get();
}
