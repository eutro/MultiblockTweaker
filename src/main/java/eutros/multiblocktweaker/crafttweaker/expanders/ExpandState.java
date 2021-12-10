package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenExpansion("crafttweaker.block.IBlockState")
@ZenRegister
public class ExpandState {

    @ZenCaster
    public static CTTraceabilityPredicate asCTPredicate(IBlockState self) {
        return CTTraceabilityPredicate.states(self);
    }

    @ZenCaster
    public static IBlockInfo asIBlockInfo(IBlockState self) {
        return IBlockInfo.fromState(self);
    }

    @ZenCaster
    public static IICubeRenderer asIICubeRenderer(IBlockState self) {
        return IICubeRenderer.fromState(self);
    }

}
