package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import gregtech.api.gui.resources.TextureArea;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class MCTextureArea implements ITextureArea {

    @NotNull
    private final TextureArea inner;

    public MCTextureArea(@NotNull TextureArea inner) {
        this.inner = inner;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @NotNull
    @Override
    public TextureArea getInternal() {
        return inner;
    }

    @Override
    public ITextureArea getSubArea(double offsetX, double offsetY, double width, double height) {
        return new MCTextureArea(inner.getSubArea(offsetX, offsetY, width, height));
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre evt) {
        evt.getMap().registerSprite(inner.imageLocation);
    }

}
