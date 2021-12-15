package eutros.multiblocktweaker.crafttweaker.predicate;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockWorldState;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.util.BlockInfo;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.blocks.VariantActiveBlock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ZenClass("mods.gregtech.multiblock.CTPredicate")
@ZenRegister
public class CTTraceabilityPredicate {
    TraceabilityPredicate internal;

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

    @ZenMethod
    public static CTTraceabilityPredicate ANY() {
        return new CTTraceabilityPredicate(TraceabilityPredicate.ANY.get());
    }

    @ZenMethod
    public static CTTraceabilityPredicate AIR() {
        return new CTTraceabilityPredicate(TraceabilityPredicate.AIR.get());
    }

    @ZenMethod
    public static CTTraceabilityPredicate COILS() {
        return new CTTraceabilityPredicate(TraceabilityPredicate.HEATING_COILS.get());
    }

    @ZenMethod
    public static CTTraceabilityPredicate mtePredicate(IMTEPredicate predicate, ICandidates candidates) {
        return new CTTraceabilityPredicate(blockWorldState -> {
            TileEntity tileEntity = blockWorldState.getInternal().getTileEntity();
            if (!(tileEntity instanceof MetaTileEntityHolder))
                return false;
            MetaTileEntity metaTileEntity = ((MetaTileEntityHolder) tileEntity).getMetaTileEntity();
            if (predicate.apply(blockWorldState, new MCMetaTileEntity(metaTileEntity))) {
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
     * @param stacks The list of {@link IItemStack}s to match.
     * @return An {@link CTTraceabilityPredicate} that returns true for any of the given blocks.
     */
    @ZenMethod
    public static CTTraceabilityPredicate blocks(IItemStack... stacks) {
        List<IBlock> list = new ArrayList<>();
        for (IItemStack stack : stacks) {
            IBlock asBlock = stack.asBlock();
            list.add(asBlock);
        }
        return blocks(list.toArray(new IBlock[0]));
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

    @ZenMethod
    public static CTTraceabilityPredicate metaTileEntities(IMetaTileEntity... allowedMTEs) {
        return mtePredicate((state, mte) -> Arrays.stream(allowedMTEs).anyMatch(mte2->mte2.getInternal().metaTileEntityId == mte.getInternal().metaTileEntityId),
                getCandidates(Arrays.stream(allowedMTEs).map(IMetaTileEntity::getInternal).toArray(MetaTileEntity[]::new)));
    }

    @ZenMethod
    @ZenOperator(OperatorType.OR)
    public CTTraceabilityPredicate or(CTTraceabilityPredicate predicate) {
        internal = internal.or(predicate.internal);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setCenter() {
        internal = internal.setCenter();
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate sort() {
        internal = internal.sort();
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate addTooltips(String... tips) {
        internal = internal.addTooltips(tips);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMinGlobalLimited(int min) {
        internal = internal.setMinGlobalLimited(min);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMinGlobalLimited(int min, int previewCount) {
        internal = internal.setMinGlobalLimited(min, previewCount);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMaxGlobalLimited(int max) {
        internal = internal.setMaxGlobalLimited(max);

        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMaxGlobalLimited(int max, int previewCount) {
        internal = internal.setMaxGlobalLimited(max, previewCount);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMinLayerLimited(int min) {
        internal = internal.setMinLayerLimited(min);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMinLayerLimited(int min, int previewCount) {
        internal = internal.setMinLayerLimited(min, previewCount);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMaxLayerLimited(int max) {
        internal = internal.setMaxLayerLimited(max);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setMaxLayerLimited(int max, int previewCount) {
        internal = internal.setMaxLayerLimited(max, previewCount);
        return this;
    }

    @ZenMethod
    public CTTraceabilityPredicate setPreviewCount(int count) {
        internal = internal.setPreviewCount(count);
        return this;
    }

    public TraceabilityPredicate toInternal() {
        return internal;
    }
}
