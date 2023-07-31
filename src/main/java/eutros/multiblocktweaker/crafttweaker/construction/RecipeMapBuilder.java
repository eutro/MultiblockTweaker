package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantMoveType;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ISound;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeBuilder;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeProperty;
import eutros.multiblocktweaker.gregtech.recipes.RecipeMapMultiblock;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.integration.crafttweaker.recipe.CTRecipeBuilder;
import net.minecraft.util.SoundEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Used for {@link RecipeMap} creation.
 * <p>
 * Used for {@link MultiblockBuilder#withRecipeMap(RecipeMap)}.
 *
 * @zenClass mods.gregtech.recipe.FactoryRecipeMap
 */
@ZenClass("mods.gregtech.recipe.FactoryRecipeMap")
@ZenRegister
public class RecipeMapBuilder {

    private final String name;
    private int minInputs = 0;
    private int maxInputs = 0;
    private int minOutputs = 0;
    private int maxOutputs = 0;
    private int minFluidInputs = 0;
    private int maxFluidInputs = 0;
    private int minFluidOutputs = 0;
    private int maxFluidOutputs = 0;
    private boolean isHidden = false;
    public CTRecipeBuilder defaultRecipe;
    private final TByteObjectMap<TextureArea> slotOverlays = new TByteObjectHashMap<>();
    private ProgressWidget.MoveType moveType = null;
    private TextureArea progressBarTexture = null;
    private SoundEvent sound;

    /**
     * Create a new, blank {@link CTRecipeBuilder} that can be used with {@link #setDefaultRecipe(CTRecipeBuilder)}.
     *
     * @param recipeProperties RecipeProperty keys fot the recipe map.
     * @return A blank {@link CTRecipeBuilder}.
     */
    @ZenMethod
    public static CTRecipeBuilder startBuilder(CustomRecipeProperty...recipeProperties) {
        return new CTRecipeBuilder(new CustomRecipeBuilder(recipeProperties));
    }

    public RecipeMapBuilder(String name, CustomRecipeProperty...recipeProperties) {
        this.name = name;
        this.defaultRecipe = startBuilder(recipeProperties);
    }

    /**
     * Start construction, with the given name. This will be used for localisation and later referencing.
     * <p>
     *
     * @param recipeProperties RecipeProperty keys fot the recipe map.
     * @param name The unlocalized name for the recipe map.
     * @return The initialized builder.
     */
    @ZenMethod
    public static RecipeMapBuilder start(String name, CustomRecipeProperty...recipeProperties) {
        return new RecipeMapBuilder(name, recipeProperties);
    }

    /**
     * Start construction, with the given name. This will be used for localisation and later referencing.
     * <p>
     *
     * @param name The unlocalized name for the recipe map.
     * @return The initialized builder.
     */
    @ZenMethod
    public static RecipeMapBuilder start(String name) {
        return new RecipeMapBuilder(name);
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
     * Should hide recipes.
     * @param isHidden isHidden.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setHidden(boolean isHidden) {
        this.isHidden = isHidden;
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
     * Set sound for the recipe map.
     *
     * @param sound sound
     * @return This builder, for convenience.
     */
    @ZenMethod
    public RecipeMapBuilder setSound(ISound sound) {
        this.sound = sound == null ? null : sound.getInternal();
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
                maxInputs,
                maxOutputs,
                maxFluidInputs,
                maxFluidOutputs,
                (CustomRecipeBuilder) defaultRecipe.backingBuilder,
                isHidden);
        if (sound != null) {
            map.setSound(sound);
        }
        for (byte key : slotOverlays.keys()) {
            map.setSlotOverlay((key & 2) != 0, (key & 1) != 0, (key & 4) != 0, slotOverlays.get(key));
        }

        if (progressBarTexture != null && moveType != null) {
            map.setProgressBar(progressBarTexture, moveType);
        }

        return map;
    }

}
