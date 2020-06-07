package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockShapeInfoBuilder;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockInfo;
import gregtech.api.util.BlockInfo;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * This is used to define what block should show in the JEI preview.
 *
 * It is used in {@link MultiblockShapeInfoBuilder#where(String, IBlockInfo)}.
 *
 * @see MultiblockShapeInfoBuilder
 * @see IMultiblockShapeInfo
 */
@ZenClass("mods.gregtech.multiblock.IBlockInfo")
@ZenRegister
public interface IBlockInfo {

    @NotNull
    BlockInfo getInternal();

    /**
     * Get an {@link IBlockInfo} from a block.
     *
     * Equivalent to {@code block as IBlockInfo}.
     *
     * @param block The block to display.
     * @return An {@link IBlockInfo} that represents the given block.
     */
    @ZenMethod
    static IBlockInfo fromBlock(@NotNull IBlock block) {
        return new MCBlockInfo(new BlockInfo(CraftTweakerMC.getBlock(block.getDefinition())));
    }

    /**
     * Get an {@link IBlockInfo} from a block state.
     *
     * Equivalent to {@code state as IBlockInfo}.
     *
     * @param state The block state to display.
     * @return An {@link IBlockInfo} that represents the given block.
     */
    @ZenMethod
    static IBlockInfo fromState(@NotNull IBlockState state) {
        return new MCBlockInfo(new BlockInfo(CraftTweakerMC.getBlockState(state)));
    }

    /**
     * Get an {@link IBlockInfo} from a block state.
     *
     * Equivalent to {@code state as IBlockInfo}.
     *
     * @param id The id of the meta tile entity to display.
     * @param facing (Optional) The direction this should face. {@code IFacing.west()} by default.
     * @return An {@link IBlockInfo} that represents the given block.
     */
    @ZenMethod
    static IBlockInfo controller(String id, @Optional @Nullable IFacing facing) {
        return new MCBlockInfo(new MCBlockInfo.ControllerInfo(
                java.util.Optional.ofNullable(facing)
                        .map(IFacing::getInternal)
                        .filter(EnumFacing.class::isInstance)
                        .map(EnumFacing.class::cast)
                        .orElse(EnumFacing.WEST),
                id));
    }

    /**
     * An {@link IBlockInfo} that displays nothing.
     */
    @ZenProperty IBlockInfo EMPTY = new MCBlockInfo(BlockInfo.EMPTY);

}
