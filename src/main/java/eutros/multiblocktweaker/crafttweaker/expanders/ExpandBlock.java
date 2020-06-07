package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.predicate.IBlockMatcher;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenExpansion("crafttweaker.block.IBlock")
@ZenRegister
public class ExpandBlock {

    @ZenCaster
    public static IBlockMatcher asIBlockMatcher(IBlock self) {
        return IBlockMatcher.blockPredicate(self);
    }

    @ZenCaster
    public static IBlockInfo asIBlockInfo(IBlock self) {
        return IBlockInfo.fromBlock(self);
    }

    @ZenCaster
    public static IICubeRenderer asIICubeRenderer(IBlock self) {
        return IICubeRenderer.fromBlock(self);
    }

}
