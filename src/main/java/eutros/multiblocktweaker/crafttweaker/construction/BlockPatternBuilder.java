package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.functions.IPatternBuilderFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantRelativeDirection;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import gregtech.api.pattern.FactoryBlockPattern;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/**
 * Used to construct an {@link IBlockPattern}.
 * <p>
 * Used for {@link MultiblockBuilder#withPattern(IPatternBuilderFunction)}.
 *
 * @zenClass mods.gregtech.multiblock.FactoryBlockPattern
 * @see IBlockPattern
 */
@ZenClass("mods.gregtech.multiblock.FactoryBlockPattern")
@ZenRegister
public class BlockPatternBuilder {

    private FactoryBlockPattern delegate;

    public BlockPatternBuilder(FactoryBlockPattern delegate) {
        this.delegate = delegate;
    }

    /**
     * Start an empty builder. Equivalent to {@code FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)}
     *
     * @return An empty builder.
     */
    @ZenMethod
    public static BlockPatternBuilder start() {
        return new BlockPatternBuilder(FactoryBlockPattern.start());
    }

    /**
     * Start an empty builder, defining the directions of the aisle, the strings and the characters used when setting aisles.
     *
     * @param charDir   The position of each character in a string relative to the one before.
     * @param stringDir The position of each string in an {@link #aisle(String...)} call relative to the one before.
     * @param aisleDir  The position of each aisle relative to the one before.
     * @return The empty builder.
     */
    @ZenMethod
    public static BlockPatternBuilder start(ConstantRelativeDirection charDir, ConstantRelativeDirection stringDir, ConstantRelativeDirection aisleDir) {
        return new BlockPatternBuilder(FactoryBlockPattern.start(charDir.val, stringDir.val, aisleDir.val));
    }

    /**
     * Add a repeatable aisle.
     *
     * @param minRepeat How many times this aisle must be repeated at minimum.
     * @param maxRepeat How many times this aisle can be repeated at maximum.
     * @param aisle     The aisle pattern. {@link #aisle(String...)}
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder aisleRepeatable(int minRepeat, int maxRepeat, String... aisle) {
        delegate = delegate.aisleRepeatable(minRepeat, maxRepeat, aisle);
        return this;
    }

    /**
     * Add a repeatable aisle.
     *
     * @param repeats How many times this aisle must be repeated.
     * @param aisle   The aisle pattern. {@link #aisle(String...)}
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder aisleRepeatable(int repeats, String... aisle) {
        return this.aisleRepeatable(repeats, repeats, aisle);
    }

    /**
     * Add a single aisle.
     *
     * @param aisle The aisle pattern. Each unique character in any string must be defined in {@link #where(String, CTTraceabilityPredicate)}.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder aisle(String... aisle) {
        delegate = delegate.aisle(aisle);
        return this;
    }

    /**
     * Makes the previous aisle entered be repeatable. {@link #aisleRepeatable(int, int, String...)}.
     *
     * @param minRepeat How many times the aisle must be repeated at minimum.
     * @param maxRepeat How many times the aisle can be repeated at maximum.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setRepeatable(int minRepeat, int maxRepeat) {
        delegate = delegate.setRepeatable(minRepeat, maxRepeat);
        return this;
    }

    /**
     * Makes the previous aisle entered be repeatable. {@link #aisleRepeatable(int, int, String...)}.
     * Equivalent to {@code FactoryBlockPattern.setRepeatable(repeatCount, repeatCount)}.
     *
     * @param repeatCount How many times the aisle must be repeated, exactly.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setRepeatable(int repeatCount) {
        delegate = delegate.setRepeatable(repeatCount);
        return this;
    }

    /**
     * Define a symbol. This predicate will be used when checking if the multiblock is valid.
     *
     * @param symbol       The character that will represent this predicate in {@link #aisle(String...)} and the other aisle methods.
     * @param predicate The predicate to match blocks by.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder where(String symbol, CTTraceabilityPredicate predicate) {
        if (symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.where(symbol.charAt(0), predicate.toInternal());
        return this;
    }


    /**
     * Define a symbol. Any of the given predicates may be satisfied.
     *
     * @param symbol   The character that will represent this predicate in {@link #aisle(String...)} and the other aisle methods.
     * @param first    The first matcher.
     * @param predicates The rest of the predicates.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder whereOr(String symbol, CTTraceabilityPredicate first, CTTraceabilityPredicate... predicates) {
        if (symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.where(symbol.charAt(0), Arrays.stream(predicates).reduce(first, CTTraceabilityPredicate::or).toInternal());
        return this;
    }

    /**
     * Create the {@link IBlockPattern}, for use in multiblock creation. Can be called multiple times.
     *
     * @return The built {@link IBlockPattern}.
     */
    @ZenMethod
    public IBlockPattern build() {
        return new MCBlockPattern(delegate.build());
    }

}
