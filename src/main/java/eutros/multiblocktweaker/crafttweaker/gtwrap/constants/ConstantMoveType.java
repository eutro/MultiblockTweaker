package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import gregtech.api.gui.widgets.ProgressWidget;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * Constant MoveType
 *
 * @zenClass mods.gregtech.render.MoveType
 * @see eutros.multiblocktweaker.crafttweaker.construction.RecipeMapBuilder#setProgressBar(ITextureArea, ConstantMoveType) 
 */
@ZenClass("mods.gregtech.render.MoveType")
@ZenRegister
public enum ConstantMoveType {
    /**
     *
     */@ZenProperty VERTICAL(ProgressWidget.MoveType.VERTICAL),
    /**
     *
     */@ZenProperty HORIZONTAL(ProgressWidget.MoveType.HORIZONTAL),
    /**
     *
     */@ZenProperty VERTICAL_INVERTED(ProgressWidget.MoveType.VERTICAL_INVERTED);

    public final ProgressWidget.MoveType delegate;

    ConstantMoveType(ProgressWidget.MoveType type) {
        delegate = type;
    }
}
