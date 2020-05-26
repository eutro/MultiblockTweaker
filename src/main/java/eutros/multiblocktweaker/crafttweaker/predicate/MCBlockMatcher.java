package eutros.multiblocktweaker.crafttweaker.predicate;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import gregtech.api.multiblock.BlockWorldState;

import java.util.function.Predicate;

public class MCBlockMatcher implements IBlockMatcher {

    public final Predicate<BlockWorldState> predicate;

    public MCBlockMatcher(Predicate<BlockWorldState> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(IBlockWorldState state) {
        return predicate.test(state.getInternal());
    }

}
