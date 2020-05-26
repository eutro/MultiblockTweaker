package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import com.google.common.collect.ImmutableMap;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.BasicCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.SidedCubeRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ZenClass("mods.gregtech.render.ICubeRenderer")
@ZenRegister
public interface ICubeRenderer {

    @NotNull
    gregtech.api.render.ICubeRenderer getInternal();

    @ZenMethod
    static ICubeRenderer nonSided(String loc) {
        return new MCCubeRenderer(new BasicCubeRenderer(new ResourceLocation(loc)));
    }

    @ZenMethod
    static ICubeRenderer sided(@Nonnull String top,
                               @Nonnull String front,
                               @Nullable String left,
                               @Nullable String right,
                               @Nullable String back,
                               @Nullable String bottom) {
        if(bottom == null) {
            bottom = top;
        }
        if(back == null) {
            back = front;
        }
        if(left == null) {
            left = right == null ? back : right;
        }
        if(right == null) {
            right = left;
        }

        return new MCCubeRenderer(new SidedCubeRenderer(
                ImmutableMap.<EnumFacing, ResourceLocation>builder()
                        .put(EnumFacing.UP, new ResourceLocation(top))
                        .put(EnumFacing.NORTH, new ResourceLocation(front))
                        .put(EnumFacing.EAST, new ResourceLocation(left))
                        .put(EnumFacing.WEST, new ResourceLocation(right))
                        .put(EnumFacing.SOUTH, new ResourceLocation(back))
                        .put(EnumFacing.DOWN, new ResourceLocation(bottom))
                        .build()
        ));
    }

}
