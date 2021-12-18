package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import stanhebben.zenscript.annotations.ZenClass;

@FunctionalInterface
@ZenClass("mods.gregtech.energy.IMTEPredicate")
@ZenRegister
public interface IMTEPredicate {
    /**
     * similar to the {@link IPredicate}.
     * @param state block world state
     * @param mte will be null if the block not the {@link IMetaTileEntity}
     * @return checking result;
     */
    boolean apply(IBlockWorldState state, IMetaTileEntity mte);
}
