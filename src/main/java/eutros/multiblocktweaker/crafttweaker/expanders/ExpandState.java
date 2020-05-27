package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.predicate.IBlockMatcher;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenExpansion("crafttweaker.block.IBlockState")
@ZenRegister
public class ExpandState {

    @ZenCaster
    public static IBlockMatcher asIBlockMatcher(IBlockState self) {
        return IBlockMatcher.statePredicate(self);
    }

    @ZenCaster
    public static IBlockInfo asIBlockInfo(IBlockState self) {
        return IBlockInfo.fromState(self);
    }

}
