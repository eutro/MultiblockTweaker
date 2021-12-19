package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import gregtech.api.pattern.BlockWorldState;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Similar to an {@link IBlockState}, but bound to a position and world,
 * and holding extra information about the match taking place.
 *
 * @zenClass mods.gregtech.multiblock.IBlockWorldState
 */
@ZenClass("mods.gregtech.multiblock.IBlockWorldState")
@ZenRegister
public interface IBlockWorldState {

    @NotNull
    BlockWorldState getInternal();

    @ZenMethod
    @ZenGetter("hasError")
    boolean hasError();

    /**
     * Set error info for tips. if predicate checking failed.
     * @param error Error tips.
     */
    @ZenMethod
    void setError(String error);

    /**
     * @return The match context of the entire structure as opposed to that of a single layer.
     */
    @ZenMethod
    @ZenGetter("matchContext")
    IPatternMatchContext getMatchContext();

    /**
     * @return The block state at the position referenced by this {@link IBlockWorldState}.
     */
    @ZenMethod
    @ZenGetter("state")
    IBlockState getBlockState();

    /**
     * @return The position referenced by this {@link IBlockWorldState}.
     */
    @ZenMethod
    @ZenGetter("pos")
    IBlockPos getPos();

    /**
     * @return The world referenced by this {@link IBlockWorldState}.
     */
    @ZenMethod
    @ZenGetter("world")
    IWorld getWorld();

    /**
     * @param face The direction to offset in.
     * @return The block state at the position offset from this.
     */
    @ZenMethod
    IBlockState getOffsetState(IFacing face);

}
