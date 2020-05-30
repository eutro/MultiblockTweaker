package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import gregtech.api.multiblock.BlockPattern;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.multiblock.IBlockPattern")
@ZenRegister
public interface IBlockPattern {

    @NotNull
    BlockPattern getInternal();

    @ZenGetter("fingerLength")
    int getFingerLength();

    @ZenGetter("thumbLength")
    int getThumbLength();

    @ZenGetter("palmLength")
    int getPalmLength();

    @NotNull
    @ZenMethod
    IPatternMatchContext checkPatternAt(IWorld world, IBlockPos centerPos, IFacing facing);

}
