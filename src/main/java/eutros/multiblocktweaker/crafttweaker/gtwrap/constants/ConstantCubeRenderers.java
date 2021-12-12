package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMachineRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.render.CubeRenderers")
@ZenRegister
public class ConstantCubeRenderers {
    private final static Map<String, IICubeRenderer> cache = new HashMap<>();

    @ZenMethod
    @ZenMemberGetter()
    public static IICubeRenderer get(String member) {
        if (!cache.containsKey(member)) {
            ICubeRenderer cubeRenderer = ReflectionHelper.getStatic(Textures.class, member);
            cache.put(member, cubeRenderer == null ? null : new MCMachineRenderer(cubeRenderer));
        }
        return cache.get(member);
    }
}
