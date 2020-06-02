package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockInfo;
import gregtech.api.util.BlockInfo;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.multiblock.IBlockInfo")
@ZenRegister
public interface IBlockInfo {

    @NotNull
    BlockInfo getInternal();

    @ZenMethod
    static IBlockInfo fromBlock(@NotNull IBlock block) {
        return new MCBlockInfo(new BlockInfo(CraftTweakerMC.getBlock(block.getDefinition())));
    }

    @ZenMethod
    static IBlockInfo fromState(@NotNull crafttweaker.api.block.IBlockState state) {
        return new MCBlockInfo(new BlockInfo(CraftTweakerMC.getBlockState(state)));
    }

    @ZenMethod
    static IBlockInfo controller(String id, @NotNull IFacing facing) {
        return new MCBlockInfo(new MCBlockInfo.ControllerInfo((EnumFacing) facing.getInternal(), id));
    }

    @NotNull
    @ZenMethod
    static IBlockInfo controller(String id) {
        return controller(id, CraftTweakerMC.getIFacing(EnumFacing.WEST));
    }

    @ZenProperty IBlockInfo EMPTY = new MCBlockInfo(BlockInfo.EMPTY);

}
