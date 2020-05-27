package eutros.multiblocktweaker.gregtech;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.GregTechAPI;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.multiblock.MultiblockRegistry")
@ZenRegister
public class MultiblockRegistry {

    private static Int2ObjectMap<CustomMultiblock> metaIdMap = new Int2ObjectOpenHashMap<>();
    private static Map<ResourceLocation, CustomMultiblock> resourceLocMap = new HashMap<>();

    public static void registerMultiblock(@NotNull CustomMultiblock multiblock) {
        resourceLocMap.put(multiblock.loc, multiblock);
        metaIdMap.put(multiblock.metaId, multiblock);
        GregTechAPI.registerMetaTileEntity(multiblock.metaId, new TileControllerCustom(multiblock));
    }

    @Nullable
    @ZenMethod
    public static CustomMultiblock get(int metaId) {
        return metaIdMap.get(metaId);
    }

    @Nullable
    @ZenMethod
    public static CustomMultiblock get(@NotNull String location) {
        ResourceLocation loc = new ResourceLocation(location);
        if(loc.getResourceDomain().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getResourcePath());
        }
        return get(loc);
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
