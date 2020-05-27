package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockShapeInfo;
import eutros.multiblocktweaker.gregtech.cuberenderer.BasicCubeRenderer;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.BlockInfo;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ZenClass("mods.gregtech.multiblock.Builder")
@ZenRegister
public class MultiblockBuilder {

    public ResourceLocation loc;
    public int metaId;
    public gregtech.api.render.ICubeRenderer texture = null;
    public RecipeMap<?> recipeMap = null;
    public BlockPattern pattern = null;

    @NotNull
    public List<MultiblockShapeInfo> designs = new ArrayList<>();

    private MultiblockBuilder(ResourceLocation loc, int metaId) {
        this.loc = loc;
        this.metaId = metaId;
    }

    /**
     * Create a multiblock builder from a resource location.
     *
     * @param location The resource location of the multiblock.
     *                 Used for resolving textures or getting the recipe map again.
     *                 If no namespace is defined, it defaults to this mod's mod id.
     * @param metaId   The metadata the resulting multiblock will be registered as.
     * @return A multiblock builder instance, that should be used to set the properties of the multiblock.
     */
    @ZenMethod
    public static MultiblockBuilder start(@NotNull String location, int metaId) {
        ResourceLocation loc = new ResourceLocation(location);
        if(loc.getResourceDomain().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getResourcePath());
        }
        return new MultiblockBuilder(loc, metaId);
    }

    /**
     * Compulsory, set the pattern for the multiblock.
     *
     * @param pattern A BlockPattern defining the multiblock's construction.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder withPattern(@NotNull IBlockPattern pattern) {
        this.pattern = pattern.getInternal();
        return this;
    }

    @ZenMethod
    public MultiblockBuilder addDesign(@NotNull IMultiblockShapeInfo shapeInfo) {
        this.designs.add(shapeInfo.getInternal());
        return this;
    }

    /**
     * Compulsory, set the texture for the multiblock. This will be used when the multiblock forms, texturing all the inputs and outputs.
     *
     * @param texture The texture to use.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockBuilder withTexture(@NotNull ICubeRenderer texture) {
        this.texture = texture.getInternal();
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

    @ZenMethod
    @Nullable
    public CustomMultiblock build() {
        if(pattern == null) {
            CraftTweakerAPI.logError(String.format("No pattern defined for multiblock \"%s\"", loc));
            return null;
        }
        if(recipeMap == null) {
            CraftTweakerAPI.logError(String.format("No recipeMap defined for multiblock \"%s\"", loc));
            return null;
        }
        if(texture == null) {
            if(designs.isEmpty()) {
                CraftTweakerAPI.logError(String.format("No texture defined for multiblock \"%s\", and there are no defined designs.", loc));
                return null;
            } else {
                if(Minecraft.getMinecraft() != null) { // i.e. we are on the client
                    BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
                    @SuppressWarnings("Convert2MethodRef") // blah blah different semantics
                    Optional<BasicCubeRenderer> tex = designs.parallelStream() // get the mode block's particle texture
                            .map(design -> design.getBlocks())
                            .flatMap(Arrays::stream)
                            .flatMap(Arrays::stream)
                            .flatMap(Arrays::stream) // flatten the 3D array
                            .map(BlockInfo::getBlockState)
                            .filter(IBlockState::isOpaqueCube) // solid block gang
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                            .entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue()) // get the mode
                            .map(Map.Entry::getKey)
                            .map(brd::getModelForState)
                            .map(IBakedModel::getParticleTexture) // use the particle texture
                            .map(BasicCubeRenderer::new);

                    if(!tex.isPresent()) {
                        CraftTweakerAPI.logWarning(String.format("No texture defined for multiblock \"%s\", and couldn't resolve texture through JEI previews.", loc));
                        return null;
                    }

                    texture = tex.get();
                }
            }
        }
        if(designs.isEmpty()) {
            CraftTweakerAPI.logWarning(String.format("No designs defined for multiblock \"%s\". It will not show up in JEI.", loc));
        }

        return new CustomMultiblock(this);
    }

    @ZenMethod
    public void buildAndRegister() {
        Optional.ofNullable(build()).ifPresent(CustomMultiblock::register);
    }

}
