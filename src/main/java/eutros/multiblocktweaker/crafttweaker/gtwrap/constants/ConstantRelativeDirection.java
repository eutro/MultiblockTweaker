package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.multiblock.BlockPattern;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.multiblock.RelativeDirection")
@ZenRegister
public enum ConstantRelativeDirection {
    /** */ @ZenProperty UP(BlockPattern.RelativeDirection.UP),
    /** */ @ZenProperty DOWN(BlockPattern.RelativeDirection.DOWN),
    /** */ @ZenProperty LEFT(BlockPattern.RelativeDirection.LEFT),
    /** */ @ZenProperty RIGHT(BlockPattern.RelativeDirection.RIGHT),
    /** */ @ZenProperty FRONT(BlockPattern.RelativeDirection.FRONT),
    /** */ @ZenProperty BACK(BlockPattern.RelativeDirection.BACK);

    public BlockPattern.RelativeDirection val;

    ConstantRelativeDirection(BlockPattern.RelativeDirection direction) {
        val = direction;
    }
}
