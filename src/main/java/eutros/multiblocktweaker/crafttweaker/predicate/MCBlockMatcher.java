package eutros.multiblocktweaker.crafttweaker.predicate;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import gregtech.api.pattern.TraceabilityPredicate;


public class MCBlockMatcher implements IBlockMatcher {

    public final TraceabilityPredicate predicate;

    public MCBlockMatcher(TraceabilityPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(IBlockWorldState state) {
        return predicate.test(state.getInternal());
    }

}
