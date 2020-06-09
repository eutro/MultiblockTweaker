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

/**
 * A representation of a custom GregTech Multiblock.
 *
 * This is best used for easy access to the recipe map.
 *
 * @see MultiblockBuilder
 *
 * Use this to set any custom working functions:
 * @see IUpdateFunction
 * @see IUpdateWorktableFunction
 * @see ISetupRecipeFunction
 * @see ICompleteRecipeFunction
 * @see IRecipePredicate
 */
@ZenClass("mods.gregtech.multiblock.Multiblock")
@ZenRegister
public class CustomMultiblock {

    /**
     * The meta value of the multiblock controller. Set in {@link MultiblockBuilder#start(String, int)}.
     */
    @ZenProperty
    public final int metaId;
    /**
     * The recipe map the multiblock uses. Set in {@link MultiblockBuilder#withRecipeMap(RecipeMap)}.
     */
    @ZenProperty
    public final RecipeMap<?> recipeMap;
    public final ResourceLocation loc;
    public final BlockPattern pattern;
    public final gregtech.api.render.ICubeRenderer texture;
    public final List<MultiblockShapeInfo> designs;

    /**
     * The {@link IUpdateFunction} this multiblock has.
     *
     * Should be set using the ZenSetter.
     */
    @ZenProperty public IUpdateFunction update;
    /**
     * The {@link IUpdateWorktableFunction} this multiblock has.
     *
     * Should be set using the ZenSetter.
     */
    @ZenProperty public IUpdateWorktableFunction updateWorktable;
    /**
     * The {@link ISetupRecipeFunction} this multiblock has.
     *
     * Should be set using the ZenSetter.
     */
    @ZenProperty public ISetupRecipeFunction setupRecipe;
    /**
     * The {@link ICompleteRecipeFunction} this multiblock has.
     *
     * Should be set using the ZenSetter.
     */
    @ZenProperty public ICompleteRecipeFunction completeRecipe;
    /**
     * The {@link IRecipePredicate} this multiblock has.
     *
     * Should be set using the ZenSetter.
     */
    @ZenProperty public IRecipePredicate recipePredicate;

    public CustomMultiblock(MultiblockBuilder builder) {
        metaId = builder.metaId;
        loc = builder.loc;
        pattern = builder.pattern;
        recipeMap = builder.recipeMap;
        texture = builder.texture;
        designs = builder.designs;
    }

    /**
     * @return The meta tile entity ID of this multiblock. Set in {@link MultiblockBuilder#start(String, int)}.
     */
    @NotNull
    @ZenGetter("loc")
    public String getLocation() {
        return loc.toString();
    }

    /**
     * @return The pattern of the multiblock. Set in {@link MultiblockBuilder#withPattern(IBlockPattern)}.
     */
    @NotNull
    @ZenGetter("pattern")
    public IBlockPattern getPattern() {
        return new MCBlockPattern(pattern);
    }

    /**
     * @return The texture of the multiblock. Optionally set in {@link MultiblockBuilder#withTexture(IICubeRenderer)}.
     */
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
