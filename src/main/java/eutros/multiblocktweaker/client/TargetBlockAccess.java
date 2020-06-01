package eutros.multiblocktweaker.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TargetBlockAccess implements IBlockAccess {

    private final IBlockAccess delegate;
    private BlockPos targetPos;

    public TargetBlockAccess(IBlockAccess delegate, BlockPos pos) {
        this.delegate = delegate;
        this.targetPos = pos;
    }

    public void setPos(BlockPos pos) {
        targetPos = pos;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return pos.equals(BlockPos.ORIGIN) ? delegate.getTileEntity(targetPos) : null;
    }

    @Override
    public int getCombinedLight(@NotNull BlockPos pos, int lightValue) {
        return 15;
    }

    @NotNull
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return pos.equals(BlockPos.ORIGIN) ? delegate.getBlockState(targetPos) : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return !pos.equals(BlockPos.ORIGIN) || delegate.isAirBlock(targetPos);
    }

    @NotNull
    @Override
    public Biome getBiome(@NotNull BlockPos pos) {
        return delegate.getBiome(targetPos);
    }

    @ParametersAreNonnullByDefault
    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @NotNull
    @Override
    public WorldType getWorldType() {
        return delegate.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, @NotNull EnumFacing side, boolean _default) {
        return pos.equals(BlockPos.ORIGIN) && delegate.isSideSolid(targetPos, side, _default);
    }

}
