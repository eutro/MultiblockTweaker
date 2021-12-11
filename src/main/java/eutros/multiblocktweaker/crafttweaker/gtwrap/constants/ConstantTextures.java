package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCTextureArea;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.resources.TextureArea;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.render.Textures")
@ZenRegister
public class ConstantTextures {
    Map<String, ITextureArea> cache = new HashMap<>();
    
    @ZenMethod
    @ZenMemberGetter()
    ITextureArea get(String member) {
        if (!cache.containsKey(member)) {
            TextureArea textureArea = ReflectionHelper.getStatic(GuiTextures.class, member);
            cache.put(member, textureArea == null ? null : new MCTextureArea(textureArea));
        }
        return cache.get(member);
    }
}
