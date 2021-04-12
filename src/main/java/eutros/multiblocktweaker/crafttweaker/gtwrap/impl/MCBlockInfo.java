package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.MultiblockRegistry;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
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
        private final String id;
        private MetaTileEntityHolder te = null;

        public ControllerInfo(EnumFacing facing, String id) {
            super(MetaBlocks.MACHINE.getDefaultState());
            this.facing = facing;
            this.id = id;
        }

        @Override
        public TileEntity getTileEntity() {
            if (te == null) {
                te = new MetaTileEntityHolder();
                CustomMultiblock mb = MultiblockRegistry.get(id);
                if (mb != null) {
                    te.setMetaTileEntity(new TileControllerCustom(mb));
                    te.getMetaTileEntity().setFrontFacing(facing);
                } else {
                    te = null;
                }
            }
            return te;
        }

        @Override
        public void apply(World world, BlockPos pos) {
            world.setBlockState(pos, getBlockState());
            if (getTileEntity() != null) {
                world.setTileEntity(pos, getTileEntity());
            }
        }

    }

}
