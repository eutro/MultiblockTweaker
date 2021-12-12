package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.render.IOverlayRenderer;
import gregtech.api.render.Textures;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.render.CubeRenderers")
@ZenRegister
public class ConstantOverlayRenderers {
    private final static Map<String, IIOverlayRenderer> cache = new HashMap<>();

    @ZenMethod
    @ZenMemberGetter()
    public static IIOverlayRenderer get(String member) {
        if (!cache.containsKey(member)) {
            IOverlayRenderer overlayRenderer = ReflectionHelper.getStatic(Textures.class, member);
            cache.put(member, overlayRenderer == null ? null : new MCOverlayRenderer(overlayRenderer));
        }
        return cache.get(member);
    }
}
