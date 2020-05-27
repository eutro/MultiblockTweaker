package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCPatternMatchContext;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.multiblock.PatternMatchContext;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.IMatchValidator")
@ZenRegister
public interface IMatchValidator {

    @ZenMethod
    boolean test(IPatternMatchContext context);


    static Predicate<PatternMatchContext> wrap(IMatchValidator validator) {
        return a -> validator.test(new MCPatternMatchContext(a));
    }

    @Nonnull
    @ZenMethod
    default IMatchValidator and(@Nonnull IMatchValidator other) {
        return t -> test(t) && other.test(t);
    }

    @Nonnull
    @ZenMethod
    default IMatchValidator negate() {
        return t -> !test(t);
    }

    @Nonnull
    @ZenMethod
    default IMatchValidator or(@Nonnull IMatchValidator other) {
        return t -> test(t) || other.test(t);
    }

}
