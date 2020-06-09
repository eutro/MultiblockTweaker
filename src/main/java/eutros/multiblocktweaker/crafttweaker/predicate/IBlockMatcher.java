package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.construction.BlockPatternBuilder;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.IPatternCenterPredicate;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static gregtech.api.metatileentity.multiblock.MultiblockControllerBase.tilePredicate;

/**
 * Used by an {@link IBlockPattern} to decide whether the placement of a block is valid.
 *
 * @see BlockPatternBuilder
 */
@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.IBlockMatcher")
@ZenRegister
public interface IBlockMatcher {

    /**
     * The method that is implemented when you use this as a function.
     *
     * @param state The {@link IBlockWorldState} to test.
     * @return Whether the given state is valid for this predicate.
     */
    @ZenMethod
    @ZenOperator(OperatorType.CONTAINS)
    boolean test(IBlockWorldState state);

    /**
     * Matches anything.
     * <p>
     * Equivalent to
     * {@code function (state as IBlockWorldState) as boolean { return true; } as IBlockMatcher}
     */
    @ZenProperty IBlockMatcher ANY = a -> true;
    /**
     * Matches any air blocks.
     */
    @ZenProperty IBlockMatcher AIR = a -> MultiblockControllerBase.isAirPredicate().test(a.getInternal());
    /**
     * Matches anything while inside the JEI preview.
     *
     * Equivalent to
     * {@code function (state as IBlockWorldState) as boolean { return state.getWorld().getDimension() == 2147483647; } as IBlockMatcher }
     */
    @ZenProperty IBlockMatcher IN_PREVIEW = a -> a.getWorld().getDimension() == Integer.MAX_VALUE;

    static Predicate<BlockWorldState> toInternal(IBlockMatcher blockMatcher) {
        if(blockMatcher instanceof MCBlockMatcher) {
            return ((MCBlockMatcher) blockMatcher).predicate;
        }
        return a -> blockMatcher.test(new MCBlockWorldState(a));
    }

    static IBlockMatcher toCT(Predicate<BlockWorldState> blockMatcher) {
        return new MCBlockMatcher(blockMatcher);
    }

    /**
     * Get an {@link IBlockMatcher} that combines two predicates such that a block must pass both to be valid.
     *
     * Can be applied by just {@code &&}.
     *
     * {@code (matcher_a as IBlockMatcher).and(matcher_b as IBlockMatcher)}
     * is equivalent to
     * {@code matcher_a as IBlockMatcher && matcher_b as IBlockMatcher}
     * which is equivalent to
     * {@code function (state as IBlockWorldState) as boolean { return (matcher_a as IBlockMatcher).test(state) && (matcher_b as IBlockMatcher).test(state); } as IBlockMatcher}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.AND)
    default IBlockMatcher and(@Nonnull IBlockMatcher other) {
        if(isCenterPredicate(this) || isCenterPredicate(other)) {
            return new MCBlockMatcher(
                    BlockWorldState.wrap(toInternal(this).and(toInternal(other)))
            );
        }
        return t -> test(t) && other.test(t);
    }

    /**
     * Get an {@link IBlockMatcher} that inverts a predicate such that blocks that fail the original pass the new one.
     *
     * Can be applied by just {@code !}.
     *
     * {@code (matcher as IBlockMatcher).negate()}
     * is equivalent to
     * {@code !(matcher as IBlockMatcher)}
     * which is equivalent to
     * {@code function (state as IBlockWorldState) as boolean { return !(matcher as IBlockMatcher).test(state); } as IBlockMatcher}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.NEG)
    default IBlockMatcher negate() {
        if(isCenterPredicate(this)) {
            return new MCBlockMatcher(
                    BlockWorldState.wrap(toInternal(this).negate())
            );
        }
        return t -> !test(t);
    }

    /**
     * Get an {@link IBlockMatcher} that combines two predicates such that a block may pass either to be valid.
     *
     * Can be applied by just {@code ||}.
     *
     * {@code (matcher_a as IBlockMatcher).or(matcher_b as IBlockMatcher)}
     * is equivalent to
     * {@code matcher_a as IBlockMatcher || matcher_b as IBlockMatcher}
     * which is equivalent to
     * {@code function (state as IBlockWorldState) as boolean { return (matcher_a as IBlockMatcher).test(state) || (matcher_b as IBlockMatcher).test(state); } as IBlockMatcher}
     */
    @Nonnull
    @ZenMethod
    @ZenOperator(OperatorType.OR)
    default IBlockMatcher or(@Nonnull IBlockMatcher other) {
        if(isCenterPredicate(this) || isCenterPredicate(other)) {
            return new MCBlockMatcher(
                    BlockWorldState.wrap(toInternal(this).or(toInternal(other)))
            );
        }
        return t -> test(t) || other.test(t);
    }

    static boolean isCenterPredicate(@Nonnull IBlockMatcher matcher) {
        return matcher instanceof MCBlockMatcher &&
                ((MCBlockMatcher) matcher).predicate instanceof IPatternCenterPredicate;
    }

    /**
     * Match the controller, with the given resource location.
     * <p>
     * If there isn't exactly one of these present in an {@link IBlockPattern}, it will be considered invalid.
     *
     * @param resourceLocation The location, or "Meta Tile Entity ID", of the controller to match.
     * @return An {@link IBlockMatcher} that matches the controller.
     */
    @ZenMethod
    static IBlockMatcher controller(String resourceLocation) {
        ResourceLocation loc = new ResourceLocation(resourceLocation);
        if(loc.getResourceDomain().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getResourcePath());
        }
        ResourceLocation finalLoc = loc;
        return toCT(BlockWorldState.wrap(tilePredicate((state, tile) -> tile.metaTileEntityId.equals(finalLoc))));
    }

    /**
     * Match any block that has one of the given {@link IMultiblockAbility}-es.
     *
     * @param allowedAbilities One or multiple {@link IMultiblockAbility}-es to match for.
     * @return An {@link IBlockMatcher} that matches any blocks with one of the given {@link IMultiblockAbility}-es.
     */
    @ZenMethod
    static IBlockMatcher abilityPartPredicate(IMultiblockAbility... allowedAbilities) {
        Set<? extends MultiblockAbility<?>> abilities = Arrays.stream(allowedAbilities).map(IMultiblockAbility::getInternal).collect(Collectors.toSet());

        return toCT(tilePredicate((state, tile) -> tile instanceof IMultiblockAbilityPart &&
                abilities.contains(((IMultiblockAbilityPart<?>) tile).getAbility())));
    }

    /**
     * Match any meta tile entity that is an instance of the class,
     * as defined by {@link java.lang.Class#isAssignableFrom(java.lang.Class)},
     * given the fully qualified class name.
     * <p>
     * If you don't know what that means, this likely isn't for you, and even then, you probably have other options.
     *
     * @param className A fully qualified class name.
     * @return An {@link IBlockMatcher} that returns true when matching a block with a meta tile entity assignable from the given class.
     */
    @ZenMethod
    static IBlockMatcher partPredicate(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch(ClassNotFoundException e) {
            CraftTweakerAPI.logError(String.format("No class found for name: %s", className));
            return a -> false;
        }

        return toCT(tilePredicate((state, tile) ->
                tile instanceof IMultiblockPart && clazz.isAssignableFrom(tile.getClass())));
    }

    /**
     * Match any of the given {@link IBlockState}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IBlockState as IBlockMatcher}.
     *
     * @param allowedStates The list of {@link IBlockState}s to match.
     * @return An {@link IBlockMatcher} that returns true for any of the given blockstates.
     */
    @ZenMethod
    static IBlockMatcher statePredicate(IBlockState... allowedStates) {
        Set<net.minecraft.block.state.IBlockState> states = new HashSet<>();
        for(IBlockState allowedState : allowedStates) {
            states.add(CraftTweakerMC.getBlockState(allowedState));
        }

        return toCT(blockWorldState -> states.contains(blockWorldState.getBlockState()));
    }

    /**
     * Match any blockstate with one of the given {@link IBlock}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IBlock as IBlockMatcher}`
     *
     * @param blocks The list of {@link IBlock}s to match.
     * @return An {@link IBlockMatcher} that returns true for any of the given blocks.
     */
    @ZenMethod
    static IBlockMatcher blockPredicate(IBlock... blocks) {
        Set<Block> bloxx = new HashSet<>();
        for(IBlock block : blocks) {
            bloxx.add(CraftTweakerMC.getBlock(block.getDefinition()));
        }

        return toCT(blockWorldState -> bloxx.contains(blockWorldState.getBlockState().getBlock()));
    }

    /**
     * Match any blockstate with one of the given {@link IItemStack}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IItemStack as IBlock as IBlockMatcher}`
     *
     * @param stacks The list of {@link IItemStack}s to match.
     * @return An {@link IBlockMatcher} that returns true for any of the given blocks.
     */
    @ZenMethod
    static IBlockMatcher blockPredicate(IItemStack... stacks) {
        List<IBlock> list = new ArrayList<>();
        for(IItemStack stack : stacks) {
            IBlock asBlock = stack.asBlock();
            list.add(asBlock);
        }

        return blockPredicate(list.toArray(new IBlock[0]));
    }

    /**
     * An {@link IBlockMatcher} that counts the number of blocks that end up matching this predicate.
     * <p>
     * This can be accessed from {@link IPatternMatchContext#getInt(String)}.
     *
     * @param key      The key whose value will be incremented when a match occurs.
     * @param original The {@link IBlockMatcher} to count matches for.
     * @return An {@link IBlockMatcher} that returns true the same as {@param original}, but also counts the matches.
     */
    @ZenMethod
    static IBlockMatcher countMatch(String key, IBlockMatcher original) {
        return (blockWorldState) -> {
            if(original.test(blockWorldState)) {
                blockWorldState.getLayerContext().increment(key, 1);
                return true;
            } else {
                return false;
            }
        };
    }

}
