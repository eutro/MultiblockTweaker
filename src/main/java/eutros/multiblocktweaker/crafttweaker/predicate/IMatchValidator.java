package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCPatternMatchContext;
import gregtech.api.multiblock.PatternMatchContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@ZenClass("mods.gregtech.multiblock.IMatchValidator")
@ZenRegister
public interface IMatchValidator extends Predicate<MCPatternMatchContext> {

    static Predicate<PatternMatchContext> wrap(IMatchValidator validator) {
        return a -> validator.test(new MCPatternMatchContext(a));
    }

    boolean test(MCPatternMatchContext context);

    @Nonnull
    @ZenMethod
    @Override
    default IMatchValidator and(@Nonnull Predicate<? super MCPatternMatchContext> other) {
        return t -> test(t) && other.test(t);
    }

    @Nonnull
    @ZenMethod
    @Override
    default IMatchValidator negate() {
        return t -> !test(t);
    }

    @Nonnull
    @ZenMethod
    @Override
    default IMatchValidator or(@Nonnull Predicate<? super MCPatternMatchContext> other) {
        return t -> test(t) || other.test(t);
    }

}
