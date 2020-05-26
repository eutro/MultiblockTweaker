package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCTextureArea;
import gregtech.api.gui.resources.TextureArea;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.render.ITextureArea")
@ZenRegister
public interface ITextureArea {

    @NotNull
    TextureArea getInternal();

    @ZenMethod
    static ITextureArea fullImage(String imageLocation) {
        return new MCTextureArea(TextureArea.fullImage(imageLocation));
    }

    @ZenMethod
    static ITextureArea areaOfImage(String imageLocation, int imageSizeX, int imageSizeY, int u, int v, int width, int height) {
        return new MCTextureArea(TextureArea.areaOfImage(imageLocation, imageSizeX, imageSizeY, u, v, width, height));
    }

    @ZenMethod
    ITextureArea getSubArea(double offsetX, double offsetY, double width, double height);

}
