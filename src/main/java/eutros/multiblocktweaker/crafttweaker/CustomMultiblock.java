package eutros.multiblocktweaker.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.functions.*;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
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

import java.util.List;

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

    @ZenProperty public IUpdateFunction update;
    @ZenProperty public IUpdateWorktableFunction updateWorktable;
    @ZenProperty public ISetupRecipeFunction setupRecipe;
    @ZenProperty public ICompleteRecipeFunction completeRecipe;
    @ZenProperty public IRecipePredicate recipePredicate;

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
    public IICubeRenderer getTexture() {
        return new MCCubeRenderer(texture);
    }

    /**
     * Register this multiblock. Calling this more than once will error.
     *
     * @return This multiblock, for convenience.
     */
    @ZenMethod
    public CustomMultiblock register() {
        MultiblockRegistry.registerMultiblock(this);
        CraftTweakerAPI.logInfo(String.format("Registered multiblock: %s, with meta %s", loc, metaId));
        return this;
    }

}
