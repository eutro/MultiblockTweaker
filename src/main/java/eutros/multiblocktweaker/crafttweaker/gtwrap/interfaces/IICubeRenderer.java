package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import crafttweaker.mc1120.world.MCFacing;
import eutros.multiblocktweaker.crafttweaker.brackethandler.CubeRendererBracketHandler;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantOverlayFace;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCICubeRenderer;
import eutros.multiblocktweaker.gregtech.renderer.IBlockStateRenderer;
import eutros.multiblocktweaker.gregtech.renderer.SidedCubeRenderer;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Used in {@link MultiblockBuilder#withBaseTexture(IICubeRenderer)} to set the texture of the controller,
 * and that of all the components, when the multiblock forms.
 *
 * @zenClass mods.gregtech.render.ICubeRenderer
 * @see MultiblockBuilder
 */
@ZenClass("mods.gregtech.render.ICubeRenderer")
@ZenRegister
public interface IICubeRenderer extends ICubeRenderer {

    /**
     * Get a ICubeRenderer by path from original CEu.
     *
     * @param path The path of the cube renderer.
     * @return The cube renderer referenced by the path, or null.
     */
    @ZenMethod
    @Nullable
    static IICubeRenderer byPath(@NotNull String path) {
        ICubeRenderer renderer = Textures.CUBE_RENDERER_REGISTRY.getOrDefault(path, null);
        if (renderer != null) {
            return new MCICubeRenderer(renderer);
        }
        return null;
    }

    /**
     * Create a {@link gregtech.client.renderer.texture.cube.SimpleOverlayRenderer} with all sides showing the given texture.
     * <p>
     * If the texture is registered in the CEu, will use the default one.
     * Otherwise, this must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     * <p> Normal texture:
     *     {@code format("blocks/%s", basePath)}
     * </p>
     * <p> Emissive texture (Optional):
     *     {@code format("blocks/%s_emissive", basePath)}
     * </p>
     * @param key Searching key for bracket.
     * @param path The resource location pointing to the texture to use.
     * @return An {@link IICubeRenderer}.
     */
    @ZenMethod
    static IICubeRenderer simpleOverlay(String key, String path) {
        return CubeRendererBracketHandler.cache.computeIfAbsent(key, $ -> new MCICubeRenderer(new SimpleOverlayRenderer(path)));
    }

    /**
     * Get a sided {@link SidedCubeRenderer} (i.e. different sides can have different textures).
     * <p>
     * {@code IFacing.up()} is the only mapping that is required, blanks will be filled in as such:
     * <p>
     * If DOWN is not defined, it will be mapped to UP.
     * NORTH will be resolved as the first defined horizontal side in the order NORTH, EAST, SOUTH, WEST, otherwise UP.
     * WEST, SOUTH and EAST will be mapped to NORTH if they are not defined yet.
     * <p> Normal texture:
     *     {@code format("%s", basePath)}
     * </p>
     * <p> Emissive texture (Optional):
     *     {@code format("%s_emissive", basePath)}
     * </p>
     * <p>
     * If the texture at a given location is not already used by something else,
     * it must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     *
     * @param key Searching key for bracket.
     * @param map A mapping of sides to texture resource locations.
     * @return An {@link IICubeRenderer} with all sides showing the given texture.
     */
    @ZenMethod
    static IICubeRenderer sidedOverlay(String key, Map<IFacing, String> map) {
        EnumMap<EnumFacing, String> result = new EnumMap<>(EnumFacing.class);
        for (Map.Entry<IFacing, String> e : map.entrySet()) {
            if (result.put((EnumFacing) e.getKey().getInternal(), e.getValue()) != null) {
                CraftTweakerAPI.logError("Duplicate key: " + e.getKey().getName());
            }
        }
        return CubeRendererBracketHandler.cache.computeIfAbsent(key, $ -> new MCICubeRenderer(new SidedCubeRenderer(key, SidedCubeRenderer.fillBlanks(result))));
    }

    /**
     * same as {@link IICubeRenderer#sidedOverlay(String, Map)}
     * @param key Searching key for bracket.
     * @param up    The texture to use for the top face.
     * @param north (Optional) The texture to use for the north face.
     * @param east  (Optional) The texture to use for the east face.
     * @param south (Optional) The texture to use for the south face.
     * @param west  (Optional) The texture to use for the west face.
     * @param down  (Optional) The texture to use for the bottom face.
     * @return An {@link IICubeRenderer} with all sides showing the given texture.
     */
    @ZenMethod
    static IICubeRenderer sidedOverlay(String key, @Nonnull String up,
                                @Optional String north,
                                @Optional String east,
                                @Optional String south,
                                @Optional String west,
                                @Optional String down) {

        Map<IFacing, String> builder = new HashMap<>();

        builder.put(new MCFacing(EnumFacing.UP), up);
        if (north != null) builder.put(new MCFacing(EnumFacing.NORTH), north);
        if (east != null) builder.put(new MCFacing(EnumFacing.EAST), east);
        if (west != null) builder.put(new MCFacing(EnumFacing.WEST), west);
        if (south != null) builder.put(new MCFacing(EnumFacing.SOUTH), south);
        if (down != null) builder.put(new MCFacing(EnumFacing.DOWN), down);

        return sidedOverlay(key, builder);
    }

    /**
     * Create a {@link gregtech.client.renderer.texture.cube.OrientedOverlayRenderer} with textures of five oriented sides of the machine and its working state.
     * <p> FRONT {@link MetaTileEntity#getFrontFacing()}
     * <p> BACK {@link MetaTileEntity#getFrontFacing()}.{@link EnumFacing#getOpposite()}
     * <p> TOP {@link EnumFacing#UP}
     * <p> BOTTOM {@link EnumFacing#DOWN}
     * <p> SIDE {@link EnumFacing#NORTH} + {@link EnumFacing#SOUTH} + {@link EnumFacing#EAST} + {@link EnumFacing#WEST}
     * <p> Normal texture:
     *     {@code format("blocks/%s/overlay_%s", basePath, faceName)}
     * </p>
     * <p> Active state texture (Optional):
     *     {@code format("blocks/%s/overlay_%s_active", basePath, faceName)}
     * </p>
     * <p> Pause state texture (Optional):
     *     {@code format("blocks/%s/overlay_%s_paused", basePath, faceName)}
     * </p>
     * <p> Emissive texture (Optional):
     *     {@code format("blocks/%s/overlay_%s_%s_emissive", basePath, faceName, state)}
     * </p>
     * <p>
     * If the texture is registered in the CEu, will use the default one.
     * Otherwise, this must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     *
     * @param key Searching key for bracket.
     * @param path The resource location pointing to the texture to use.
     * @param faces Available faces of the texture. {@link ConstantOverlayFace}
     * @return An {@link IICubeRenderer}.
     */
    @ZenMethod
    static IICubeRenderer orientedOverlay(String key, String path, ConstantOverlayFace... faces) {
        return CubeRendererBracketHandler.cache.computeIfAbsent(key, $ -> new MCICubeRenderer(new OrientedOverlayRenderer(path)));

    }

    /**
     * Get an {@link IBlockStateRenderer} from a block.
     * <p>
     * This will typically only work with blocks that have conventional models.
     * <p>
     * Equivalent to {@code block as ICubeRenderer}.
     *
     * @param block The block whose textures should be used.
     * @return An {@link IICubeRenderer} textured the same as the given block.
     */
    @ZenMethod
    static IICubeRenderer fromBlock(IBlock block) {
        return IBlockStateRenderer.create(CraftTweakerMC.getBlock(block).getDefaultState());
    }

    /**
     * Get an {@link IBlockStateRenderer} from a block.
     * <p>
     * This will typically only work with blocks that have conventional models.
     * <p>
     * Equivalent to {@code stack as IBlock as ICubeRenderer}.
     *
     * @param stack The {@link IItemStack} of block whose textures should be used.
     * @return An {@link IICubeRenderer} textured the same as the given block.
     */
    @ZenMethod
    static IICubeRenderer fromBlock(IItemStack stack) {
        return IBlockStateRenderer.create(CraftTweakerMC.getBlock(stack).getDefaultState());
    }

    /**
     * Get an {@link IBlockStateRenderer} from a block state.
     * <p>
     * This will typically only work with blocks that have conventional models.
     * <p>
     * Equivalent to {@code state as ICubeRenderer}.
     *
     * @param state The block state whose textures should be used.
     * @return An {@link IICubeRenderer} textured the same as the given block state.
     */
    @ZenMethod
    static IICubeRenderer fromState(IBlockState state) {
        return IBlockStateRenderer.create(CraftTweakerMC.getBlockState(state));
    }

    @Override
    @SideOnly(Side.CLIENT)
    default void registerIcons(TextureMap textureMap) {}
}
