package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.IPatternCenterPredicate;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static gregtech.api.metatileentity.multiblock.MultiblockControllerBase.tilePredicate;

@FunctionalInterface
@ZenClass("mods.gregtech.multiblock.IBlockMatcher")
@ZenRegister
public interface IBlockMatcher {

    @ZenMethod
    boolean test(IBlockWorldState state);

    @ZenProperty IBlockMatcher ANY = a -> true;
    @ZenProperty IBlockMatcher AIR = a -> MultiblockControllerBase.isAirPredicate().test(a.getInternal());

    static Predicate<BlockWorldState> toInternal(IBlockMatcher blockMatcher) {
        if(blockMatcher instanceof MCBlockMatcher) {
            return ((MCBlockMatcher) blockMatcher).predicate;
        }
        return a -> blockMatcher.test(new MCBlockWorldState(a));
    }

    static IBlockMatcher toCT(Predicate<BlockWorldState> blockMatcher) {
        return new MCBlockMatcher(blockMatcher);
    }

    @Nonnull
    @ZenMethod
    default IBlockMatcher and(@Nonnull IBlockMatcher other) {
        if(isCenterPredicate(this) || isCenterPredicate(other)) {
            return new MCBlockMatcher(
                     BlockWorldState.wrap(toInternal(this).and(toInternal(other)))
            );
        }
        return t -> test(t) && other.test(t);
    }

    @Nonnull
    @ZenMethod
    default IBlockMatcher negate() {
        if(isCenterPredicate(this)) {
            return new MCBlockMatcher(
                    BlockWorldState.wrap(toInternal(this).negate())
            );
        }
        return t -> !test(t);
    }

    @Nonnull
    @ZenMethod
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

    @ZenMethod
    static IBlockMatcher controller(String location) {
        ResourceLocation loc = new ResourceLocation(location);
        if(loc.getResourceDomain().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getResourcePath());
        }
        ResourceLocation finalLoc = loc;
        return toCT(BlockWorldState.wrap(tilePredicate((state, tile) -> tile.metaTileEntityId.equals(finalLoc))));
    }

    @ZenMethod
    static IBlockMatcher abilityPartPredicate(IMultiblockAbility... allowedAbilities) {
        Set<? extends MultiblockAbility<?>> abilities = Arrays.stream(allowedAbilities).map(IMultiblockAbility::getInternal).collect(Collectors.toSet());

        return toCT(tilePredicate((state, tile) -> tile instanceof IMultiblockAbilityPart &&
                abilities.contains(((IMultiblockAbilityPart<?>) tile).getAbility())));
    }

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

    @ZenMethod
    static IBlockMatcher statePredicate(IBlockState... allowedStates) {
        Set<net.minecraft.block.state.IBlockState> states = Arrays.stream(allowedStates)
                .map(CraftTweakerMC::getBlockState)
                .collect(Collectors.toSet());

        return toCT(blockWorldState -> states.contains(blockWorldState.getBlockState()));
    }

    @ZenMethod
    static IBlockMatcher blockPredicate(IBlockPattern... block) {
        Set<Block> blocks = Arrays.stream(block).map(IBlockPattern::getBlocks).flatMap(List::stream)
                .map(IBlock::getDefinition).map(CraftTweakerMC::getBlock)
                .collect(Collectors.toSet());

        return toCT(blockWorldState -> blocks.contains(blockWorldState.getBlockState().getBlock()));
    }

    @ZenMethod
    static IBlockMatcher blockPredicate(IItemStack... stacks) {
        List<IBlock> list = new ArrayList<>();
        for(IItemStack stack : stacks) {
            IBlock asBlock = stack.asBlock();
            list.add(asBlock);
        }

        return blockPredicate(list.toArray(new IBlock[0]));
    }

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
