package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class MCBlockPattern implements IBlockPattern {

    @NotNull
    private final BlockPattern internal;

    public MCBlockPattern(@NotNull BlockPattern internal) {
        this.internal = internal;
    }

    @NotNull
    @Override
    public BlockPattern getInternal() {
        return internal;
    }

    @Override
    public MCPatternMatchContext checkPatternAt(IWorld world, IBlockPos centerPos, IFacing facing) {
        PatternMatchContext match = internal.checkPatternFastAt(CraftTweakerMC.getWorld(world), CraftTweakerMC.getBlockPos(centerPos), (EnumFacing) facing.getInternal());
        return match == null ? null : new MCPatternMatchContext(match);
    }

}
