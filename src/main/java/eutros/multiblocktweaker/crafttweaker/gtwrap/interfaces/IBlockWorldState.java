package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import gregtech.api.multiblock.BlockWorldState;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.multiblock.IBlockWorldState")
@ZenRegister
public interface IBlockWorldState {

    @NotNull
    BlockWorldState getInternal();

    @ZenMethod
    void update(@NotNull IWorld worldIn, @NotNull IBlockPos posIn, @NotNull IPatternMatchContext matchContext, @NotNull IPatternMatchContext layerContext);

    @ZenMethod
    IPatternMatchContext getMatchContext();

    @ZenMethod
    IPatternMatchContext getLayerContext();

    @ZenMethod
    IBlockState getBlockState();

    @ZenMethod
    IBlockPos getPos();

    @ZenMethod
    IBlockState getOffsetState(IFacing face);
    
    @ZenMethod
    IWorld getWorld();

}
