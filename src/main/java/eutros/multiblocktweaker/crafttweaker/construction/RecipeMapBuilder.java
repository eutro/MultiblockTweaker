package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantMoveType;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import eutros.multiblocktweaker.gregtech.recipes.RecipeMapMultiblock;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.recipes.crafttweaker.CTRecipeBuilder;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * An interface for {@link RecipeMap} creation.
 */
@ZenClass("mods.gregtech.recipe.FactoryRecipeMap")
@ZenRegister
public class RecipeMapBuilder {

    private String name;
    private int minInputs = 0;
    private int maxInputs = 0;
    private int minOutputs = 0;
    private int maxOutputs = 0;
    private int minFluidInputs = 0;
    private int maxFluidInputs = 0;
    private int minFluidOutputs = 0;
    private int maxFluidOutputs = 0;
    public CTRecipeBuilder defaultRecipe = new CTRecipeBuilder(new SimpleRecipeBuilder().hidden());
    private TByteObjectMap<TextureArea> slotOverlays = new TByteObjectHashMap<>();
    private ProgressWidget.MoveType moveType = null;
    private TextureArea progressBarTexture = null;

    /**
     * Create a new, blank {@link CTRecipeBuilder} that will be the base recipe for any new ones.
     *
     * @return A blank {@link CTRecipeBuilder}.
     */
    @ZenMethod
    public static CTRecipeBuilder startBuilder() {
        return new CTRecipeBuilder(new SimpleRecipeBuilder().hidden());
    }

    public RecipeMapBuilder(String name) {
        this.name = name;
    }

    /**
     * Start construction, with the given name. This will be used for localisation and later referencing.
     *
     * @param name The unlocalized name for the recipe map.
     * @return The initialized builder.
     */
    @ZenMethod
    public static RecipeMapBuilder start(String name) {
        return new RecipeMapBuilder(name);
    }

    /**
     * Start construction, setting all of the constructor values at the start.
     *
     * @param name            The unlocalized name for the recipe map.
     * @param minInputs       The minimum item inputs a recipe can have.
     * @param maxInputs       The maximum item inputs a recipe can have.
     * @param minOutputs      The minimum item outputs a recipe can have.
     * @param maxOutputs      The maximum item outputs a recipe can have.
     * @param minFluidInputs  The minimum fluid inputs a recipe can have.
     * @param maxFluidInputs  The maximum fluid inputs a recipe can have.
     * @param minFluidOutputs The minimum fluid outputs a recipe can have.
     * @param maxFluidOutputs The maximum fluid outputs a recipe can have.
     * @return The initialized builder.
     */
    @ZenMethod
    public static RecipeMapBuilder start(String name, int minInputs, int maxInputs, int minOutputs, int maxOutputs, int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs) {
        return start(name)
                .minInputs(minInputs)
                .maxInputs(maxInputs)
                .minOutputs(minOutputs)
                .maxOutputs(maxOutputs)
                .minFluidInputs(minFluidInputs)
                .maxFluidInputs(maxFluidInputs)
                .minFluidOutputs(minFluidOutputs)
                .maxFluidOutputs(maxFluidOutputs);
    }

    /**
     * Set the minimum item inputs a recipe can have.
     *
     * @param val The minimum item inputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder minInputs(int val) {
        minInputs = val;
        return this;
    }

    /**
     * Set the maximum item inputs a recipe can have.
     *
     * @param val The maximum item inputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder maxInputs(int val) {
        maxInputs = val;
        return this;
    }

    /**
     * Set the minimum fluid inputs a recipe can have.
     *
     * @param val The minimum fluid inputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder minFluidInputs(int val) {
        minFluidInputs = val;
        return this;
    }

    /**
     * Set the maximum fluid inputs a recipe can have.
     *
     * @param val The maximum fluid inputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder maxFluidInputs(int val) {
        maxFluidInputs = val;
        return this;
    }

    /**
     * Set the minimum item outputs a recipe can have.
     *
     * @param val The minimum item outputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder minOutputs(int val) {
        minOutputs = val;
        return this;
    }

    /**
     * Set the maximum item outputs a recipe can have.
     *
     * @param val The maximum item outputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder maxOutputs(int val) {
        maxOutputs = val;
        return this;
    }

    /**
     * Set the minimum fluid outputs a recipe can have.
     *
     * @param val The minimum fluid outputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder minFluidOutputs(int val) {
        minFluidOutputs = val;
        return this;
    }

    /**
     * Set the maximum fluid outputs a recipe can have.
     *
     * @param val The maximum fluid outputs a recipe can have.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder maxFluidOutputs(int val) {
        maxFluidOutputs = val;
        return this;
    }

    /**
     * Set the default recipe builder, that will be copied in order to add new recipes.
     *
     * @param builder The {@link CTRecipeBuilder} that holds the starting state for any new recipes.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setDefaultRecipe(CTRecipeBuilder builder) {
        defaultRecipe = builder;
        return this;
    }

    /**
     * Set a custom progress bar and its movement direction.
     *
     * @param progressBar The progress bar texture.
     * @param moveType    The progress bar's movement direction.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setProgressBar(ITextureArea progressBar, ConstantMoveType moveType) {
        this.progressBarTexture = progressBar.getInternal();
        this.moveType = moveType.delegate;
        return this;
    }

    /**
     * Set a custom overlay for all slots of this kind.
     *
     * @param isOutput    Whether this should apply to output slots, instead of input slots.
     * @param isFluid     Whether this should apply to fluid slots, instead of item slots.
     * @param slotOverlay The overlay texture.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setSlotOverlay(boolean isOutput, boolean isFluid, ITextureArea slotOverlay) {
        return this.setSlotOverlay(isOutput, isFluid, false, slotOverlay).setSlotOverlay(isOutput, isFluid, true, slotOverlay);
    }

    /**
     * Set a custom overlay for all slots of this kind.
     *
     * @param isOutput    Whether this should apply to output slots, instead of input slots.
     * @param isFluid     Whether this should apply to fluid slots, instead of item slots.
     * @param isLast      Whether this should apply only to the last slot of this kind.
     * @param slotOverlay The overlay texture.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setSlotOverlay(boolean isOutput, boolean isFluid, boolean isLast, ITextureArea slotOverlay) {
        slotOverlays.put((byte) ((isOutput ? 2 : 0) + (isFluid ? 1 : 0) + (isLast ? 4 : 0)), slotOverlay.getInternal());
        return this;
    }

    /**
     * Construct the {@link RecipeMap}.
     *
     * @return The built recipe map.
     */
    @ZenMethod
    public RecipeMap<?> build() {
        RecipeMapMultiblock map = new RecipeMapMultiblock(name,
                minInputs,
                maxInputs,
                minOutputs,
                maxOutputs,
                minFluidInputs,
                maxFluidInputs,
                minFluidOutputs,
                maxFluidOutputs,
                (SimpleRecipeBuilder) ObfuscationReflectionHelper.getPrivateValue(CTRecipeBuilder.class, defaultRecipe, "backingBuilder"));

        for(byte key : slotOverlays.keys()) {
            map.setSlotOverlay((key & 2) != 0, (key & 1) != 0, (key & 4) != 0, slotOverlays.get(key));
        }

        if(progressBarTexture != null && moveType != null) {
            map.setProgressBar(progressBarTexture, moveType);
        }

        return map;
    }

}
