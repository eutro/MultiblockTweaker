package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.util.BlockInfo;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MCBlockInfo implements IBlockInfo {

    @NotNull
    private final BlockInfo internal;

    public MCBlockInfo(@NotNull BlockInfo internal) {
        this.internal = internal;
    }

    @NotNull
    @Override
    public BlockInfo getInternal() {
        return internal;
    }

    public static class ControllerInfo extends BlockInfo {

        private final EnumFacing facing;
        private final int id;
        private MetaTileEntityHolder te = null;

        public ControllerInfo(EnumFacing facing, int id) {
            super(MetaBlocks.MACHINE.getDefaultState());
            this.facing = facing;
            this.id = id;
        }

        @Override
        public TileEntity getTileEntity() {
            if(te == null) {
                te = new MetaTileEntityHolder();
                te.setMetaTileEntity(new TileControllerCustom(MultiblockRegistry.get(id)));
                te.getMetaTileEntity().setFrontFacing(facing);
            }
            return te;
        }

        @Override
        public void apply(World world, BlockPos pos) {
            world.setBlockState(pos, getBlockState());
            if(getTileEntity() != null) {
                world.setTileEntity(pos, getTileEntity());
            }
        }

    }

}
