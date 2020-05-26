package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.block.IBlockState;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import crafttweaker.mc1120.block.MCBlockState;
import crafttweaker.mc1120.world.MCBlockPos;
import crafttweaker.mc1120.world.MCWorld;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockWorldState;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.multiblock.BlockWorldState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MCBlockWorldState implements IBlockWorldState {

    @NotNull
    private final BlockWorldState inner;

    public MCBlockWorldState(@NotNull BlockWorldState inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public BlockWorldState getInternal() {
        return inner;
    }

    @Override
    public void update(@NotNull IWorld worldIn, @NotNull IBlockPos posIn, @NotNull IPatternMatchContext matchContext, @NotNull IPatternMatchContext layerContext) {
        inner.update((World) worldIn.getInternal(), (BlockPos) posIn.getInternal(), matchContext.getInternal(), layerContext.getInternal());
    }

    @Override
    public IPatternMatchContext getMatchContext() {
        return new MCPatternMatchContext(inner.getMatchContext());
    }

    @Override
    public IPatternMatchContext getLayerContext() {
        return new MCPatternMatchContext(inner.getLayerContext());
    }

    @Override
    public IBlockState getBlockState() {
        return new MCBlockState(inner.getBlockState());
    }

    @Override
    public IBlockPos getPos() {
        return new MCBlockPos(inner.getPos());
    }

    @Override
    public IBlockState getOffsetState(IFacing face) {
        return new MCBlockState(inner.getOffsetState((EnumFacing) face.getInternal()));
    }

    @Override
    public IWorld getWorld() {
        return new MCWorld(inner.getWorld());
    }

}
