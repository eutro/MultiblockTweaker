package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCPatternMatchContext;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.multiblock.PatternMatchContext;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Can be used for matching either a layer, or the whole multiblock.
 *
 * They are used internally by GregTech for the Distillation Tower, to ensure that each
 * layer contains exactly one fluid output hatch.
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.IMatchValidator")
@ZenRegister
public interface IMatchValidator {

    @ZenMethod
    @ZenOperator(OperatorType.CONTAINS)
    boolean test(IPatternMatchContext context);

    static Predicate<PatternMatchContext> wrap(IMatchValidator validator) {
        return a -> validator.test(new MCPatternMatchContext(a));
    }

    /**
     * Get an {@link IMatchValidator} that combines two predicates such that a block must pass both to be valid.
     *
     * Can be applied by just {@code &&}.
     *
     * {@code (validator_a as IMatchValidator).and(validator_b as IMatchValidator)}
     * is equivalent to
     * {@code validator_a as IMatchValidator && validator_b as IMatchValidator}
     * which is equivalent to
     * {@code function (context as IPatternMatchContext) as boolean { return (validator_a as IMatchValidator).test(context) && (validator_b as IMatchValidator).test(context); } as IMatchValidator}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.AND)
    default IMatchValidator and(@Nonnull IMatchValidator other) {
        return t -> test(t) && other.test(t);
    }

    /**
     * Get an {@link IMatchValidator} that inverts a predicate such that blocks that fail the original pass the new one.
     *
     * Can be applied by just {@code !}.
     *
     * {@code (validator as IMatchValidator).negate()}
     *
     * is equivalent to
     *
     * {@code !(validator as IMatchValidator)}
     *
     * which is equivalent to
     *
     * {@code function (context as IPatternMatchContext) as boolean { return !(validator as IMatchValidator).test(context); } as IMatchValidator}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.NEG)
    default IMatchValidator negate() {
        return t -> !test(t);
    }

    /**
     * Get an {@link IMatchValidator} that combines two predicates such that a block may pass either to be valid.
     *
     * Can be applied by just {@code ||}.
     *
     * {@code (validator_a as IMatchValidator).or(validator_b as IMatchValidator)}
     *
     * is equivalent to
     *
     * {@code validator_a as IMatchValidator || validator_b as IMatchValidator}
     *
     * which is equivalent to
     *
     * {@code function (context as IPatternMatchContext) as boolean { return (validator_a as IMatchValidator).test(context) || (validator_b as IMatchValidator).test(context); } as IMatchValidator}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.OR)
    default IMatchValidator or(@Nonnull IMatchValidator other) {
        return t -> test(t) || other.test(t);
    }

}
