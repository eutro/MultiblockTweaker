package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantRelativeDirection;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.predicate.IBlockMatcher;
import eutros.multiblocktweaker.crafttweaker.predicate.IMatchValidator;
import gregtech.api.multiblock.FactoryBlockPattern;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/**
 * An interface to GTCE's own {@link FactoryBlockPattern}.
 */
@ZenClass("mods.gregtech.multiblock.FactoryBlockPattern")
@ZenRegister
public class BlockPatternBuilder {

    private FactoryBlockPattern delegate;

    public BlockPatternBuilder(FactoryBlockPattern delegate) {
        this.delegate = delegate;
    }

    /**
     * Start an empty builder. Equivalent to {@code FactoryBlockPatternWrapper.start(RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)}
     *
     * @return An empty builder.
     */
    @ZenMethod
    public static BlockPatternBuilder start() {
        return new BlockPatternBuilder(FactoryBlockPattern.start());
    }

    /**
     * Start an empty builder, defining the directions of the aisle, the strings and the characters used when setting aisles.
     * <img src="https://raw.githubusercontent.com/eutropius225/MultiblockTweaker/master/src/main/resources/FactoryBuilderDiagram.png">direction diagram</img>
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
     * @param aisle The aisle pattern. Each unique character in any string must be defined in {@link #where(String, IBlockMatcher)}.
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
     * Equivalent to {@code FactoryBlockPattern.setRepeatable(repeatCount, repeatCount)}
     *
     * @param repeatCount How many times the aisle must be repeated, exactly..
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setRepeatable(int repeatCount) {
        delegate = delegate.setRepeatable(repeatCount);
        return this;
    }

    /**
     * Set requirements for a given symbol.
     *
     * @param symbol    The symbol defined in {@link #where(String, IBlockMatcher)}
     * @param minAmount How many of the symbol-represented blocks can appear at maximum in a valid multiblock.
     * @param maxLimit  How many of the symbol-represented blocks can appear at maximum in a valid multiblock.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setAmountLimit(char symbol, int minAmount, int maxLimit) {
        delegate = delegate.setAmountLimit(symbol, minAmount, maxLimit);
        return this;
    }

    /**
     * Set requirements for a given symbol.
     *
     * @param symbol   The symbol defined in {@link #where(String, IBlockMatcher)}
     * @param minValue How many of the symbol-represented blocks can appear at maximum in a valid multiblock.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setAmountAtLeast(char symbol, int minValue) {
        delegate = delegate.setAmountAtLeast(symbol, minValue);
        return this;
    }

    /**
     * Set requirements for a given symbol.
     *
     * @param symbol   The symbol defined in {@link #where(String, IBlockMatcher)}
     * @param maxValue How many of the symbol-represented blocks can appear at maximum in a valid multiblock.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder setAmountAtMost(String symbol, int maxValue) {
        if(symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.setAmountAtMost(symbol.charAt(0), maxValue);
        return this;
    }

    /**
     * Define a symbol. This predicate will be used when checking if the multiblock is valid.
     *
     * @param symbol       The character that will represent this predicate in {@link #aisle(String...)} and the other aisle methods.
     * @param blockMatcher The predicate to match blocks by.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder where(String symbol, IBlockMatcher blockMatcher) {
        if(symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.where(symbol.charAt(0), IBlockMatcher.toInternal(blockMatcher));
        return this;
    }

    /**
     * Define a symbol. All given predicates must be satisfied.
     *
     * @param symbol   The character that will represent this predicate in {@link #aisle(String...)} and the other aisle methods.
     * @param first    The first matcher.
     * @param matchers The rest of the matchers.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder whereAnd(String symbol, IBlockMatcher first, IBlockMatcher... matchers) {
        if(symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.where(symbol.charAt(0),
                IBlockMatcher.toInternal(
                        Arrays.stream(matchers)
                                .reduce(first, IBlockMatcher::and)
                )
        );
        return this;
    }

    /**
     * Define a symbol. Any of the given predicates may be satisfied.
     *
     * @param symbol   The character that will represent this predicate in {@link #aisle(String...)} and the other aisle methods.
     * @param first    The first matcher.
     * @param matchers The rest of the matchers.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder whereOr(String symbol, IBlockMatcher first, IBlockMatcher... matchers) {
        if(symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return this;
        }
        delegate = delegate.where(symbol.charAt(0),
                IBlockMatcher.toInternal(
                        Arrays.stream(matchers)
                                .reduce(first, IBlockMatcher::or)
                )
        );
        return this;
    }

    /**
     * Convenience method for {@link #where(String, IBlockMatcher)}, shorthand for {@code where(symbol, IBlockMatcher.statePredicate(state))}
     */
    @ZenMethod
    public BlockPatternBuilder where(String symbol, IBlockState state) {
        return where(symbol, IBlockMatcher.statePredicate(state));
    }

    /**
     * Convenience method for {@link #where(String, IBlockMatcher)}, shorthand for {@code where(symbol, IBlockMatcher.blockPredicate(block))}
     */
    @ZenMethod
    public BlockPatternBuilder where(String symbol, IBlock block) {
        return where(symbol, IBlockMatcher.blockPredicate(block));
    }

    /**
     * Convenience method for {@link #where(String, IBlockMatcher)}, shorthand for {@code where(symbol, IBlockMatcher.blockPredicate(stack))}
     */
    @ZenMethod
    public BlockPatternBuilder where(String symbol, IItemStack stack) {
        return where(symbol, IBlockMatcher.blockPredicate(stack));
    }

    /**
     * Define a full match validator, to match the multiblock as a whole.
     *
     * @param validator A predicate to test the full match with.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder validateContext(IMatchValidator validator) {
        delegate = delegate.validateContext(IMatchValidator.wrap(validator));
        return this;
    }

    /**
     * Define a layer match validator, to match a layer as a whole.
     *
     * @param layerIndex     The index of the layer this should test.
     * @param layerValidator A predicate to test the full match with.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public BlockPatternBuilder validateLayer(int layerIndex, IMatchValidator layerValidator) {
        delegate = delegate.validateLayer(layerIndex, IMatchValidator.wrap(layerValidator));
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
