package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import gregtech.api.multiblock.BlockPattern;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MCBlockPattern implements IBlockPattern {

    @NotNull
    private BlockPattern internal;

    public MCBlockPattern(@NotNull BlockPattern internal) {
        this.internal = internal;
    }

    @NotNull
    @Override
    public BlockPattern getInternal() {
        return internal;
    }

    @Override
    public int getFingerLength() {
        return internal.getFingerLength();
    }

    @Override
    public int getThumbLength() {
        return internal.getThumbLength();
    }

    @Override
    public int getPalmLength() {
        return internal.getPalmLength();
    }

    @Override
    public @NotNull MCPatternMatchContext checkPatternAt(IWorld world, IBlockPos centerPos, IFacing facing) {
        return new MCPatternMatchContext(internal.checkPatternAt((World) world.getInternal(), (BlockPos) centerPos.getInternal(), (EnumFacing) facing.getInternal()));
    }

}
