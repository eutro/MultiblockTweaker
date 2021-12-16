package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import stanhebben.zenscript.annotations.ZenClass;

@FunctionalInterface
@ZenClass("mods.gregtech.predicate.IPredicate")
@ZenRegister
public interface IPredicate {
    boolean test(IBlockWorldState blockWorldState);
}
