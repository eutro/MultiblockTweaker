package eutros.multiblocktweaker.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.functions.*;
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
 */
@ZenClass("mods.gregtech.multiblock.Multiblock")
@ZenRegister
public class CustomMultiblock {

    /**
     * The meta value of the multiblock controller. Set in {@link MultiblockBuilder#start(String, int)}.
     */
    @ZenProperty
    public final int metaId;
    @ZenProperty
    public final ICubeRenderer baseTexture;
    /**
     * The recipe map the multiblock uses. Set in {@link MultiblockBuilder#withRecipeMap(RecipeMap)}.
     */
    @ZenProperty
    public final RecipeMap<?> recipeMap;
    public final ResourceLocation loc;
    public final IPatternBuilderFunction pattern;
    public final List<MultiblockShapeInfo> designs;

    /**
     * Set hasMaintenanceMechanics of the controller.
     */
    @ZenProperty
    public Boolean hasMaintenanceMechanics;
    /**
     * Set hasMufflerMechanics of the controller.
     */
    @ZenProperty
    public Boolean hasMufflerMechanics;
    /**
     * Set the overlay texture for the front of the controller.
     */
    @ZenProperty
    public ICubeRenderer frontOverlay;
    /**
     * Set allow same fluid fill for outputs.
     */
    @ZenProperty
    public Boolean allowSameFluidFillForOutputs;
    /**
     * Can be distinct.
     */
    @ZenProperty
    public Boolean canBeDistinct;

    /**
     * The {@link IUpdateWorktableFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IUpdateWorktableFunction updateWorktableFunction;
    /**
     * The {@link ISetupRecipeFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public ISetupRecipeFunction setupRecipeFunction;
    /**
     * The {@link ICompleteRecipeFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public ICompleteRecipeFunction completeRecipeFunction;
    /**
     * The {@link ICheckRecipeFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public ICheckRecipeFunction checkRecipeFunction;
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
    /**
     * The {@link IUpdateFormedValidFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IUpdateFormedValidFunction updateFormedValidFunction;
    /**
     * The {@link IInvalidateStructureFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IInvalidateStructureFunction invalidateStructureFunction;
    /**
     * The {@link IRunOverclockingLogicFunction} this multiblock has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IRunOverclockingLogicFunction runOverclockingLogic;

    public CustomMultiblock(MultiblockBuilder builder) {
        metaId = builder.metaId;
        loc = builder.loc;
        pattern = builder.pattern;
        recipeMap = builder.recipeMap;
        designs = builder.designs;
        baseTexture = builder.baseTexture;
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
