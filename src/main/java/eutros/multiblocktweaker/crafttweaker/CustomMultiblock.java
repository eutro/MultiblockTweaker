package eutros.multiblocktweaker.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ICubeRenderer;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

import java.util.*;

@ZenClass("mods.gregtech.multiblock.Multiblock")
@ZenRegister
public class CustomMultiblock {

    @ZenProperty
    public final int metaId;
    @ZenProperty
    public final RecipeMap<?> recipeMap;
    public final ResourceLocation loc;
    public final BlockPattern pattern;
    public final gregtech.api.render.ICubeRenderer texture;
    public final List<MultiblockShapeInfo> designs;

    public CustomMultiblock(MultiblockBuilder builder) {
        metaId = builder.metaId;
        loc = builder.loc;
        pattern = builder.pattern;
        recipeMap = builder.recipeMap;
        texture = builder.texture;
        designs = builder.designs;
    }

    @NotNull
    @ZenGetter("loc")
    public String getLocation() {
        return loc.toString();
    }

    @NotNull
    @ZenGetter("pattern")
    public IBlockPattern getPattern() {
        return new MCBlockPattern(pattern);
    }

    @NotNull
    @ZenGetter("texture")
    public ICubeRenderer getTexture() {
        return new MCCubeRenderer(texture);
    }

    @ZenMethod
    public void register() {
        MultiblockRegistry.registerMultiblock(this);
    }

}
