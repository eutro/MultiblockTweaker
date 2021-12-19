package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * Constant OverlayFace
 *
 * @zenClass mods.gregtech.renderer.OverlayFace
 * @see eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer#orientedOverlay(String, String, ConstantOverlayFace...)
 */
@ZenClass("mods.gregtech.renderer.OverlayFace")
@ZenRegister
public enum ConstantOverlayFace {
    /**
     *
     */@ZenProperty TOP(OrientedOverlayRenderer.OverlayFace.TOP),
    /**
     *
     */@ZenProperty BOTTOM(OrientedOverlayRenderer.OverlayFace.BOTTOM),
    /**
     *
     */@ZenProperty SIDE(OrientedOverlayRenderer.OverlayFace.SIDE),
    /**
     *
     */@ZenProperty FRONT(OrientedOverlayRenderer.OverlayFace.FRONT),
    /**
     *
     */@ZenProperty BACK(OrientedOverlayRenderer.OverlayFace.BACK);

    public OrientedOverlayRenderer.OverlayFace val;

    ConstantOverlayFace(OrientedOverlayRenderer.OverlayFace direction) {
        val = direction;
    }
}
