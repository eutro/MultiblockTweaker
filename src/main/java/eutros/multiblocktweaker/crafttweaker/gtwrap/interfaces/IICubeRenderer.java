package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import eutros.multiblocktweaker.crafttweaker.construction.MultiblockBuilder;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.BasicCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.SidedCubeRenderer;
import gregtech.api.render.ICubeRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

/**
 * Used in {@link MultiblockBuilder#withTexture(IICubeRenderer)} to set the texture of the controller,
 * and that of all the components, when the multiblock forms.
 *
 * @zenClass mods.gregtech.render.ICubeRenderer
 * @see MultiblockBuilder
 */
@ZenClass("mods.gregtech.render.ICubeRenderer")
@ZenRegister
public interface IICubeRenderer {

    @NotNull
    ICubeRenderer getInternal();

    /**
     * Get a non-sided {@link IICubeRenderer} (i.e. all sides have the same texture).
     * <p>
     * If the texture at the given location is not already used by something else,
     * this must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     *
     * @param loc The resource location pointing to the texture to use.
     * @return An {@link IICubeRenderer} with all sides showing the given texture.
     */
    @ZenMethod
    static IICubeRenderer nonSided(String loc) {
        return new MCCubeRenderer(new BasicCubeRenderer(new ResourceLocation(loc)));
    }

    /**
     * Get an {@link IICubeRenderer} from a block.
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
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlock(block).getDefaultState()));
    }

    /**
     * Get an {@link IICubeRenderer} from a block.
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
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlock(stack).getDefaultState()));
    }

    /**
     * Get an {@link IICubeRenderer} from a block state.
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
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlockState(state)));
    }

    /**
     * Get a sided {@link IICubeRenderer} (i.e. different sides can have different textures).
     * <p>
     * {@code IFacing.up()} is the only mapping that is required, blanks will be filled in as such:
     * <p>
     * If DOWN is not defined, it will be mapped to UP.
     * NORTH will be resolved as the first defined horizontal side in the order NORTH, EAST, SOUTH, WEST, otherwise UP.
     * WEST, SOUTH and EAST will be mapped to NORTH if they are not defined yet.
     * <p>
     * If the texture at a given location is not already used by something else,
     * it must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     *
     * @param map A mapping of sides to texture resource locations.
     * @return An {@link IICubeRenderer} with all sides showing the given texture.
     */
    @ZenMethod
    static IICubeRenderer sided(Map<IFacing, String> map) {
        EnumMap<EnumFacing, ResourceLocation> result = new EnumMap<>(EnumFacing.class);
        for (Map.Entry<IFacing, String> e : map.entrySet()) {
            if (result.put((EnumFacing) e.getKey().getInternal(), new ResourceLocation(e.getValue())) != null) {
                CraftTweakerAPI.logError("Duplicate key: " + e.getKey().getName());
            }
        }
        return new MCCubeRenderer(new SidedCubeRenderer(SidedCubeRenderer.fillBlanks(result)));
    }

    /**
     * Get a sided {@link IICubeRenderer} (i.e. different sides can have different textures).
     * <p>
     * {@code up} is the only mapping that is required, blanks will be filled in as such:
     * <p>
     * If DOWN is not defined, it will be mapped to UP.
     * NORTH will be resolved as the first defined horizontal side in the order NORTH, EAST, SOUTH, WEST, otherwise UP.
     * WEST, SOUTH and EAST will be mapped to NORTH if they are not defined yet.
     * <p>
     * If the texture at a given location is not already used by something else,
     * it must be registered in preinit. Use {@code #loader preinit} in a separate script
     * and define it there first.
     *
     * @param up    The texture to use for the top face.
     * @param north (Optional) The texture to use for the north face.
     * @param east  (Optional) The texture to use for the east face.
     * @param south (Optional) The texture to use for the south face.
     * @param west  (Optional) The texture to use for the west face.
     * @param down  (Optional) The texture to use for the bottom face.
     * @return An {@link IICubeRenderer} with all sides showing the given texture.
     */
    @ZenMethod
    static IICubeRenderer sided(@Nonnull String up,
                                @Optional String north,
                                @Optional String east,
                                @Optional String south,
                                @Optional String west,
                                @Optional String down) {

        EnumMap<EnumFacing, ResourceLocation> builder = new EnumMap<>(EnumFacing.class);

        builder.put(EnumFacing.UP, new ResourceLocation(up));

        if (north != null) builder.put(EnumFacing.NORTH, new ResourceLocation(north));
        if (east != null) builder.put(EnumFacing.EAST, new ResourceLocation(east));
        if (west != null) builder.put(EnumFacing.WEST, new ResourceLocation(west));
        if (south != null) builder.put(EnumFacing.SOUTH, new ResourceLocation(south));
        if (down != null) builder.put(EnumFacing.DOWN, new ResourceLocation(down));

        return new MCCubeRenderer(new SidedCubeRenderer(SidedCubeRenderer.fillBlanks(builder)));
    }

}
