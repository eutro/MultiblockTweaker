package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.MultiblockRegistry;
import eutros.multiblocktweaker.crafttweaker.functions.IPatternBuilderFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockShapeInfo;
import gregtech.api.GTValues;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Builder, or Multiblock Builder, is used to define a custom {@link CustomMultiblock}.
 * <p>
 * To get started, call {@link #start(String, int)}.
 *
 * @zenClass mods.gregtech.multiblock.Builder
 */
@ZenClass("mods.gregtech.multiblock.Builder")
@ZenRegister
public class MultiblockBuilder {

    public ResourceLocation loc;
    public int metaId;
    public ICubeRenderer baseTexture;
    public RecipeMap<?> recipeMap;
    public IPatternBuilderFunction pattern;
    public List<MultiblockShapeInfo> designs;
    private static int AUTO_ID = 32000;

    private MultiblockBuilder(ResourceLocation loc, int metaId) {
        this.loc = loc;
        this.metaId = metaId;
    }

    /**
     * Create a multiblock builder from a resource location.
     *
     * @param location The resource location of the multiblock.
     *                 Used for getting the recipe map again.
     *                 If no namespace is defined, it defaults to this mod's mod id.
     * @param metaId   (optional) The metadata the resulting multiblock will be registered as. A non-used ID is automatically registered if metaId is empty.
     * @return A multiblock builder instance, that should be used to set the properties of the multiblock.
     */
    @ZenMethod
    public static MultiblockBuilder start(@NotNull String location, int metaId) {
        ResourceLocation loc = new ResourceLocation(location);
        if (loc.getNamespace().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getPath());
        }
        return new MultiblockBuilder(loc, metaId);
    }

    /**
     * Create a multiblock builder from a resource location.
     *
     * @param location The resource location of the multiblock.
     *                 Used for getting the recipe map again.
     *                 If no namespace is defined, it defaults to this mod's mod id.
     * @return A multiblock builder instance, that should be used to set the properties of the multiblock.
     */
    @ZenMethod
    public static MultiblockBuilder start(@NotNull String location) {
        while (!(MultiblockRegistry.get(AUTO_ID) == null)) {
            AUTO_ID++;
        }
        return start(location, AUTO_ID++);
    }

    /**
     * Compulsory, set the pattern for the multiblock.
     *
     * @param pattern An {@link IBlockPattern} defining the multiblock's construction.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder withPattern(@NotNull IPatternBuilderFunction pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * Compulsory, set the recipe map for the multiblock.
     *
     * @param map The map to use.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder withRecipeMap(@NotNull RecipeMap<?> map) {
        this.recipeMap = map;
        return this;
    }

    /**
     * Set the texture for the multiblock.
     * This will be used for the controller, and all the inputs/outputs when the structure forms.
     * <p>
     * If this is not defined, {@link #build()} will fail.
     *
     * @param texture The texture to use.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder withBaseTexture(@NotNull IICubeRenderer texture) {
        this.baseTexture = texture;
        return this;
    }

    /**
     * Add a design to be shown in JEI or structure previews. Can be called multiple times.
     * <p>
     * If none are defined, one will be generated automatically.
     *
     * @param designs The designs to add and show in JEI.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder addDesign(@NotNull IMultiblockShapeInfo... designs) {
        if (this.designs == null) this.designs = new ArrayList<>();
        for (IMultiblockShapeInfo design : designs) {
            this.designs.add(design.getInternal());
        }
        return this;
    }

    /**
     * Construct the {@link CustomMultiblock} using the defined features.
     * <p>
     * Will fail if {@link #withPattern(IPatternBuilderFunction)} or {@link #withRecipeMap(RecipeMap)} wasn't called,
     * or if {@link #withBaseTexture(IICubeRenderer)} wasn't called.
     *
     * @return The built {@link CustomMultiblock}.
     */
    @ZenMethod
    @Nullable
    public CustomMultiblock build() {
        if (pattern == null) {
            CraftTweakerAPI.logError(String.format("No pattern defined for multiblock \"%s\"", loc));
            return null;
        }
        if (recipeMap == null) {
            CraftTweakerAPI.logError(String.format("No recipeMap defined for multiblock \"%s\"", loc));
            return null;
        }
        if (baseTexture == null) {
            CraftTweakerAPI.logError(String.format("No baseTexture defined for multiblock \"%s\"", loc));
            return null;
        }

        return new CustomMultiblock(this);
    }

    /**
     * Convenience method, equivalent to {@code build().register()}
     * <p>
     *
     * @return The built {@link CustomMultiblock}.
     * @see #build()
     * @see CustomMultiblock#register()
     */
    @Nullable
    @ZenMethod
    public CustomMultiblock buildAndRegister() {
        return Optional.ofNullable(build()).map(CustomMultiblock::register).orElse(null);
    }

}
