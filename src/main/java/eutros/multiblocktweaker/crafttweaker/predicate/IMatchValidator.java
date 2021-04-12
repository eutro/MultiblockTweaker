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
 * <p>
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
     * <p>
     * Can be applied by just {@code &}.
     * <p>
     * {@code (validator_a as IMatchValidator).and(validator_b as IMatchValidator)}
     * <p>
     * is equivalent to
     * <p>
     * {@code validator_a as IMatchValidator & validator_b as IMatchValidator}
     * <p>
     * which is equivalent to
     * <p>
     * {@code function (context as IPatternMatchContext) as boolean { return (validator_a as IMatchValidator).test(context) && (validator_b as IMatchValidator).test(context); } as IMatchValidator}
     *
     * @param other The other predicate, both this and other must succeed.
     * @return A predicate that succeeds if both this and the other predicate succeed.
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.AND)
    default IMatchValidator and(@Nonnull IMatchValidator other) {
        return t -> test(t) && other.test(t);
    }

    /**
     * Get an {@link IMatchValidator} that inverts a predicate such that blocks that fail the original pass the new one.
     * <p>
     * Can be applied by just {@code !}.
     * <p>
     * {@code (validator as IMatchValidator).negate()}
     * <p>
     * is equivalent to
     * <p>
     * {@code !(validator as IMatchValidator)}
     * <p>
     * which is equivalent to
     * <p>
     * {@code function (context as IPatternMatchContext) as boolean { return !(validator as IMatchValidator).test(context); } as IMatchValidator}
     *
     * @return A predicate that succeeds when this would fail, and fails when this would succeed.
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.NEG)
    default IMatchValidator negate() {
        return t -> !test(t);
    }

    /**
     * Get an {@link IMatchValidator} that combines two predicates such that a block may pass either to be valid.
     * <p>
     * Can be applied by just {@code |}.
     * <p>
     * {@code (validator_a as IMatchValidator).or(validator_b as IMatchValidator)}
     * <p>
     * is equivalent to
     * <p>
     * {@code validator_a as IMatchValidator | validator_b as IMatchValidator}
     * <p>
     * which is equivalent to
     * <p>
     * {@code function (context as IPatternMatchContext) as boolean { return (validator_a as IMatchValidator).test(context) || (validator_b as IMatchValidator).test(context); } as IMatchValidator}
     *
     * @param other The other predicate, either this or other may succeed.
     * @return A predicate that succeeds if this or the other predicate succeeds.
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.OR)
    default IMatchValidator or(@Nonnull IMatchValidator other) {
        return t -> test(t) || other.test(t);
    }

}
