package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.gui.widgets.ProgressWidget;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.render.MoveType")
@ZenRegister
public enum ConstantMoveType {
    /** */ @ZenProperty VERTICAL(ProgressWidget.MoveType.VERTICAL),
    /** */ @ZenProperty HORIZONTAL(ProgressWidget.MoveType.HORIZONTAL),
    /** */ @ZenProperty VERTICAL_INVERTED(ProgressWidget.MoveType.VERTICAL_INVERTED);

    public final ProgressWidget.MoveType delegate;

    ConstantMoveType(ProgressWidget.MoveType type) {
        delegate = type;
    }
}
