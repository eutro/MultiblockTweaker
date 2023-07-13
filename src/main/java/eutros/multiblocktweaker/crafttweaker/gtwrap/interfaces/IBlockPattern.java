package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.crafttweaker.construction.BlockPatternBuilder;
import gregtech.api.pattern.BlockPattern;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * This is what defines the required structure of a multiblock
 *
 * @zenClass mods.gregtech.multiblock.IBlockPattern
 * @see BlockPatternBuilder
 */
@ZenClass("mods.gregtech.multiblock.IBlockPattern")
@ZenRegister
public interface IBlockPattern {

    @NotNull
    BlockPattern getInternal();

    /**
     * Check whether the pattern matches a structure.
     * <p>
     * Returns {@code null} if there is no match found.
     *
     * @param world     The world to check in.
     * @param centerPos The position of the controller.
     * @param facing    The direction to match in, opposite to the direction the controller is facing.
     * @return The context of the match, or null if there was none.
     */
    @ZenMethod
    IPatternMatchContext checkPatternAt(IWorld world, IBlockPos centerPos, IFacing facing);

}
