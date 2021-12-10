package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.util.RelativeDirection;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.multiblock.RelativeDirection")
@ZenRegister
public enum ConstantRelativeDirection {
    /**
     *
     */@ZenProperty UP(RelativeDirection.UP),
    /**
     *
     */@ZenProperty DOWN(RelativeDirection.DOWN),
    /**
     *
     */@ZenProperty LEFT(RelativeDirection.LEFT),
    /**
     *
     */@ZenProperty RIGHT(RelativeDirection.RIGHT),
    /**
     *
     */@ZenProperty FRONT(RelativeDirection.FRONT),
    /**
     *
     */@ZenProperty BACK(RelativeDirection.BACK);

    public RelativeDirection val;

    ConstantRelativeDirection(RelativeDirection direction) {
        val = direction;
    }
}
