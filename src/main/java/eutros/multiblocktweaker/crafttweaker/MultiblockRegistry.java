package eutros.multiblocktweaker.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.common.metatileentities.MetaTileEntities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.*;

/**
 * An alternative way to get a {@link CustomMultiblock} from its metadata or ID.
 *
 * @zenClass mods.gregtech.multiblock.MultiblockRegistry
 */
@ZenClass("mods.gregtech.multiblock.MultiblockRegistry")
@ZenRegister
public class MultiblockRegistry {

    private static final Int2ObjectMap<CustomMultiblock> metaIdMap = new Int2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, CustomMultiblock> resourceLocMap = new HashMap<>();

    public static void registerMultiblock(@NotNull CustomMultiblock multiblock) {
        resourceLocMap.put(multiblock.loc, multiblock);
        metaIdMap.put(multiblock.metaId, multiblock);
        MetaTileEntities.registerMetaTileEntity(multiblock.metaId, new TileControllerCustom(multiblock));
    }

    /**
     * Get a {@link CustomMultiblock} by its metadata.
     *
     * @param metaId The metadata of the multiblock controller.
     * @return The controller, if it was registered, or null.
     */
    @Nullable
    @ZenMethod
    public static CustomMultiblock get(int metaId) {
        return metaIdMap.get(metaId);
    }

    /**
     * Get a {@link CustomMultiblock} by its meta tile entity ID.
     *
     * @param location The ID of the multiblock controller.
     * @return The controller, if it was registered, or null.
     */
    @Nullable
    @ZenMethod
    public static CustomMultiblock get(@NotNull String location) {
        ResourceLocation loc = new ResourceLocation(location);
        if (loc.getNamespace().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getPath());
        }
        return get(loc);
    }

    /**
     * @return All the registered {@link CustomMultiblock}s.
     */
    @ZenMethod
    public static List<CustomMultiblock> all() {
        return new ArrayList<>(getAll());
    }

    @Nullable
    public static CustomMultiblock get(@NotNull ResourceLocation resourceLocation) {
        return resourceLocMap.get(resourceLocation);
    }

    @NotNull
    public static Collection<CustomMultiblock> getAll() {
        return metaIdMap.values();
    }

}
