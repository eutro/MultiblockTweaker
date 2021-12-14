package eutros.multiblocktweaker.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.functions.IAddInformationFunction;
import eutros.multiblocktweaker.crafttweaker.functions.ICompleteRecipeFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IDisplayTextFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IFormStructureFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IGetBaseTextureFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IPatternBuilderFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IRecipePredicate;
import eutros.multiblocktweaker.crafttweaker.functions.IRemovalFunction;
import eutros.multiblocktweaker.crafttweaker.functions.ISetupRecipeFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IUpdateFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IUpdateWorktableFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

import java.util.List;

/**
 * A representation of a custom GregTech Multiblock.
 * <p>
 * This is best used for easy access to the recipe map.
 * <p>
 * Use this to set any custom working functions.
 *
 * @zenClass mods.gregtech.multiblock.Multiblock
 * @see MultiblockBuilder
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
    public final IPatternBuilderFunction pattern;
    public final ICubeRenderer baseTexture;
    public final ICubeRenderer frontOverlay;
    public final List<MultiblockShapeInfo> designs;

    /**
     * The {@link IUpdateFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IUpdateFunction update;
    /**
     * The {@link IUpdateWorktableFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IUpdateWorktableFunction updateWorktable;
    /**
     * The {@link ISetupRecipeFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public ISetupRecipeFunction setupRecipe;
    /**
     * The {@link ICompleteRecipeFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public ICompleteRecipeFunction completeRecipe;
    /**
     * The {@link IRecipePredicate} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IRecipePredicate recipePredicate;
    /**
     * The {@link IRemovalFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IRemovalFunction removalFunction;
    /**
     * The {@link IDisplayTextFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IDisplayTextFunction displayTextFunction;
    /**
     * The {@link IFormStructureFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IFormStructureFunction formStructureFunction;
    /**
     * The {@link IAddInformationFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IAddInformationFunction addInformationFunction;
    /**
     * The {@link IGetBaseTextureFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IGetBaseTextureFunction getBaseTextureFunction;

    public CustomMultiblock(MultiblockBuilder builder) {
        metaId = builder.metaId;
        loc = builder.loc;
        pattern = builder.pattern;
        recipeMap = builder.recipeMap;
        baseTexture = builder.baseTexture;
        frontOverlay = builder.frontOverlay;
        designs = builder.designs;
    }

    /**
     * @return The meta tile entity ID of this multiblock. Set in {@link MultiblockBuilder#start(String, int)}.
     *
     * @zenGetter loc
     */
    @NotNull
    @ZenGetter("loc")
    public String getLocation() {
        return loc.toString();
    }

    /**
     * @return The texture of the multiblock. Optionally set in {@link MultiblockBuilder#withBaseTexture(IICubeRenderer)}.
     *
     * @zenGetter texture
     */
    @NotNull
    @ZenGetter("texture")
    public IICubeRenderer getBaseTexture() {
        return new MCICubeRenderer(baseTexture);
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

    @Override
    public String toString() {
        return String.format("CustomMultiblock(%s)", loc);
    }

}
