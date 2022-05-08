package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidDefinition;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.block.VariantActiveBlock;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockWorldState;
import gregtech.api.pattern.PatternStringError;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.util.BlockInfo;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.ArrayUtils;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Candidate for multiblock structure pattern.
 *
 * @zenClass mods.gregtech.multiblock.CTPredicate
 * @see eutros.multiblocktweaker.crafttweaker.construction.BlockPatternBuilder
 */
@ZenClass("mods.gregtech.multiblock.CTPredicate")
@ZenRegister
public class CTTraceabilityPredicate {
    TraceabilityPredicate internal;
    public static CTTraceabilityPredicate ANY = new CTTraceabilityPredicate(TraceabilityPredicate.ANY);
    public static CTTraceabilityPredicate AIR = new CTTraceabilityPredicate(TraceabilityPredicate.AIR);

    public CTTraceabilityPredicate(TraceabilityPredicate internal) {
        this.internal = internal;
    }

    public CTTraceabilityPredicate(Predicate<BlockWorldState> predicate, Supplier<BlockInfo[]> candidates) {
        this(new TraceabilityPredicate(predicate, candidates));
    }

    @ZenConstructor
    public CTTraceabilityPredicate(IPredicate predicate, ICandidates candidates) {
        this(new TraceabilityPredicate((state) -> predicate.test(new MCBlockWorldState(state)),
                () -> Arrays.stream(candidates.get()).map(IBlockInfo::getInternal).toArray(BlockInfo[]::new)));
    }

    @ZenConstructor
    public CTTraceabilityPredicate() {
        this(new TraceabilityPredicate());
    }

    /**
     * Can be any block.
     *
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given ANY predicate.
     */
    @ZenMethod
    public static CTTraceabilityPredicate getAny() {
        return ANY;
    }

    /**
     * Only the air block.
     * 
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given AIR predicate.
     */
    @ZenMethod
    public static CTTraceabilityPredicate getAir() {
        return AIR;
    }

    /**
     * The Wire Coils block. with the same type. will also write its temperature to the context("coils_temperature").
     *
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given coils predicate.
     */
    @ZenMethod
    public static CTTraceabilityPredicate getCoils() {
        return new CTTraceabilityPredicate(new TraceabilityPredicate(blockWorldState -> {
            net.minecraft.block.state.IBlockState blockState = blockWorldState.getBlockState();
            if ((blockState.getBlock() instanceof BlockWireCoil)) {
                BlockWireCoil blockWireCoil = (BlockWireCoil) blockState.getBlock();
                BlockWireCoil.CoilType coilType = blockWireCoil.getState(blockState);
                Object currentCoilType = blockWorldState.getMatchContext().getOrPut("CoilType", coilType);
                blockWorldState.getMatchContext().getOrPut("coils_temperature", coilType.getCoilTemperature());
                if (!currentCoilType.toString().equals(coilType.getName())) {
                    blockWorldState.setError(new PatternStringError("gregtech.multiblock.pattern.error.coils"));
                    return false;
                }
                blockWorldState.getMatchContext().getOrPut("VABlock", new LinkedList<>()).add(blockWorldState.getPos());
                return true;
            }
            return false;
        }, ()-> ArrayUtils.addAll(
                Arrays.stream(BlockWireCoil.CoilType.values()).map(type->new BlockInfo(MetaBlocks.WIRE_COIL.getState(type), null)).toArray(BlockInfo[]::new)))
                .addTooltips("gregtech.multiblock.pattern.error.coils"));
    }

    /**
     * MetaTileEntity checking. will try to cast to the meta tile entity.
     * 
     * @param predicate Matching logic of meta tile entity. {@link IMTEPredicate}
     * @param candidates Get candidates of this predicate. {@link ICandidates}
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given MTEs predicate.
     */
    @ZenMethod
    public static CTTraceabilityPredicate mtePredicate(IMTEPredicate predicate, ICandidates candidates) {
        return new CTTraceabilityPredicate(blockWorldState -> {
            TileEntity tileEntity = blockWorldState.getInternal().getTileEntity();
            if (!(tileEntity instanceof MetaTileEntityHolder))
                return false;
            MetaTileEntity metaTileEntity = ((MetaTileEntityHolder) tileEntity).getMetaTileEntity();
            if (metaTileEntity == null) return false;
            if (predicate.test(blockWorldState, new MCMetaTileEntity(metaTileEntity))) {
                if (metaTileEntity instanceof IMultiblockPart) {
                    Set<IMultiblockPart> partsFound = blockWorldState.getInternal().getMatchContext().getOrCreate("MultiblockParts", HashSet::new);
                    partsFound.add((IMultiblockPart) metaTileEntity);
                }
                return true;
            }
            return false;
        }, candidates);
    }


    private static Supplier<BlockInfo[]> getCandidates(Set<net.minecraft.block.state.IBlockState> allowedStates){
        return ()-> allowedStates.stream().map(state-> new BlockInfo(state, null)).toArray(BlockInfo[]::new);
    }

    private static ICandidates getCandidates(MetaTileEntity... metaTileEntities){
        return ()->Arrays.stream(metaTileEntities).map(tile->{
            MetaTileEntityHolder holder = new MetaTileEntityHolder();
            holder.setMetaTileEntity(tile);
            holder.getMetaTileEntity().setFrontFacing(EnumFacing.SOUTH);
            return new MCBlockInfo(new BlockInfo(MetaBlocks.MACHINE.getDefaultState(), holder));
        }).toArray(IBlockInfo[]::new);
    }

    /**
     * Match any of the given {@link IBlockState}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IBlockState as IBlockMatcher}.
     *
     * @param allowedStates The list of {@link IBlockState}s to match.
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given blockstates.
     */
    @ZenMethod
    public static CTTraceabilityPredicate states(IBlockState... allowedStates) {
        Set<net.minecraft.block.state.IBlockState> states = new HashSet<>();
        for (IBlockState allowedState : allowedStates) {
            states.add(CraftTweakerMC.getBlockState(allowedState));
        }
        return new CTTraceabilityPredicate(blockWorldState -> {
            net.minecraft.block.state.IBlockState state = blockWorldState.getBlockState();
            if (state.getBlock() instanceof VariantActiveBlock) {
                state = state.withProperty(VariantActiveBlock.ACTIVE, false);
                blockWorldState.getMatchContext().getOrPut("VABlock", new LinkedList<>()).add(blockWorldState.getPos());
            }
            return states.contains(state);
        }, getCandidates(states));
    }

    /**
     * Match any blockstate with one of the given {@link IBlock}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IBlock as IBlockMatcher}`
     *
     * @param blocks The list of {@link IBlock}s to match.
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given blocks.
     */
    @ZenMethod
    public static CTTraceabilityPredicate blocks(IBlock... blocks) {
        Set<Block> bloxx = new HashSet<>();
        for (IBlock block : blocks) {
            bloxx.add(CraftTweakerMC.getBlock(block.getDefinition()));
        }
        return new CTTraceabilityPredicate(blockWorldState -> bloxx.contains(blockWorldState.getBlockState().getBlock()),
                getCandidates(bloxx.stream().map(Block::getDefaultState).collect(Collectors.toSet())));
    }

    /**
     * Match any blockstate with one of the given {@link IItemStack}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code IItemStack as IBlock as IBlockMatcher}`
     *
     * @param itemStacks The list of {@link IItemStack}s to match.
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given blocks.
     */
    @ZenMethod
    public static CTTraceabilityPredicate items(IItemStack... itemStacks) {
        return blocks(Arrays.stream(itemStacks).map(IItemStack::asBlock).toArray(IBlock[]::new));
    }

    /**
     * Match any blockstate with one of the given {@link ILiquidStack}s.
     * <p>
     * When called with a single parameter, it is equivalent to {@code ILiquidStack as IBlock as IBlockMatcher}`
     *
     * @param liquidStacks The list of {@link IItemStack}s to match.
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given blocks.
     */
    @ZenMethod
    public static CTTraceabilityPredicate liquids(ILiquidStack... liquidStacks) {
        return blocks(Arrays.stream(liquidStacks).map(ILiquidStack::getDefinition).map(ILiquidDefinition::getBlock).toArray(IBlock[]::new));
    }

    /**
     * Match any block that has one of the given {@link IMultiblockAbility}-es.
     *
     * @param allowedAbilities One or multiple {@link IMultiblockAbility}-es to match for.
     * @return An {@link CTTraceabilityPredicate} that matches any blocks with one of the given {@link IMultiblockAbility}-es.
     */
    @ZenMethod
    public static CTTraceabilityPredicate abilities(IMultiblockAbility... allowedAbilities) {
        Set<? extends MultiblockAbility<?>> abilities = Arrays.stream(allowedAbilities).map(IMultiblockAbility::getInternal).collect(Collectors.toSet());
        return mtePredicate((state, tile) -> tile.getInternal() instanceof IMultiblockAbilityPart<?> && abilities.contains(((IMultiblockAbilityPart<?>) tile.getInternal()).getAbility()),
                getCandidates(abilities.stream().flatMap(ability -> MultiblockAbility.REGISTRY.get(ability).stream()).toArray(MetaTileEntity[]::new)));
    }

    /**
     * Match any block that has one of the given {@link IMetaTileEntity}-es.
     *
     * @param allowedMTEs One or multiple {@link IMetaTileEntity}-es to match for.
     * @return An {@link CTTraceabilityPredicate} that matches any blocks with one of the given {@link IMetaTileEntity}-es.
     */
    @ZenMethod
    public static CTTraceabilityPredicate metaTileEntities(IMetaTileEntity... allowedMTEs) {
        return mtePredicate((state, mte) -> Arrays.stream(allowedMTEs).anyMatch(mte2->mte2.getInternal().metaTileEntityId == mte.getInternal().metaTileEntityId),
                getCandidates(Arrays.stream(allowedMTEs).map(IMetaTileEntity::getInternal).toArray(MetaTileEntity[]::new)));
    }

    /**
     * Use | for convenience. You can also use the {@link eutros.multiblocktweaker.crafttweaker.construction.BlockPatternBuilder#whereOr(String, CTTraceabilityPredicate, CTTraceabilityPredicate...)}
     *
     * @param predicate predicate.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    @ZenOperator(OperatorType.OR)
    public CTTraceabilityPredicate or(CTTraceabilityPredicate predicate) {
        internal = internal.or(predicate.internal);
        return this;
    }

    /**
     * Mark it as the controller of this multi. Normally you won't call it yourself.
     * Use {@link IControllerTile#self()} ()} please.
     *
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setCenter() {
        internal = internal.setCenter();
        return this;
    }

    /**
     * No need to call it in general.
     *
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate sort() {
        internal = internal.sort();
        return this;
    }

    /**
     * Add tooltips for candidates. They are shown in JEI Pages.
     *
     * @param tips Tooltips string.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate addTooltips(String... tips) {
        internal = internal.addTooltips(tips);
        return this;
    }

    /**
     * Set the minimum number of predicate blocks.
     *
     * @param min minimum number.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMinGlobalLimited(int min) {
        internal = internal.setMinGlobalLimited(min);
        return this;
    }

    /**
     * Set the minimum number of predicate blocks. and number of previews.
     *
     * @param min minimum number.
     * @param previewCount count.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMinGlobalLimited(int min, int previewCount) {
        internal = internal.setMinGlobalLimited(min, previewCount);
        return this;
    }

    /**
     * Set the maximum number of predicate blocks.
     *
     * @param max minimum number.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMaxGlobalLimited(int max) {
        internal = internal.setMaxGlobalLimited(max);

        return this;
    }

    /**
     * Set the maximum number of predicate blocks. and number of previews.
     *
     * @param max minimum number.
     * @param previewCount count.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMaxGlobalLimited(int max, int previewCount) {
        internal = internal.setMaxGlobalLimited(max, previewCount);
        return this;
    }

    /**
     * Set the minimum number of predicate blocks for each aisle layer.
     *
     * @param min minimum number.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMinLayerLimited(int min) {
        internal = internal.setMinLayerLimited(min);
        return this;
    }

    /**
     * Set the minimum number of predicate blocks for each aisle layer. and number of previews.
     *
     * @param min minimum number.
     * @param previewCount count.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMinLayerLimited(int min, int previewCount) {
        internal = internal.setMinLayerLimited(min, previewCount);
        return this;
    }

    /**
     * Set the maximum number of predicate blocks for each aisle layer.
     *
     * @param max minimum number.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMaxLayerLimited(int max) {
        internal = internal.setMaxLayerLimited(max);
        return this;
    }

    /**
     * Set the maximum number of predicate blocks for each aisle layer. and number of previews.
     *
     * @param max minimum number.
     * @param previewCount count.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setMaxLayerLimited(int max, int previewCount) {
        internal = internal.setMaxLayerLimited(max, previewCount);
        return this;
    }

    /**
     * Set the number of it appears in JEI pages. It only affects JEI preview. (The specific number)
     *
     * @param count count.
     * @return An {@link CTTraceabilityPredicate}.
     */
    @ZenMethod
    public CTTraceabilityPredicate setPreviewCount(int count) {
        internal = internal.setPreviewCount(count);
        return this;
    }

    public TraceabilityPredicate toInternal() {
        return internal;
    }
}
