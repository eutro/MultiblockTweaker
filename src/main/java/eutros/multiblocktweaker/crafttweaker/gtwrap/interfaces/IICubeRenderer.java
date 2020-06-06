package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.BasicCubeRenderer;
import eutros.multiblocktweaker.gregtech.cuberenderer.SidedCubeRenderer;
import gregtech.api.render.ICubeRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

@ZenClass("mods.gregtech.render.ICubeRenderer")
@ZenRegister
public interface IICubeRenderer {

    @NotNull
    ICubeRenderer getInternal();

    @ZenMethod
    static IICubeRenderer nonSided(String loc) {
        return new MCCubeRenderer(new BasicCubeRenderer(new ResourceLocation(loc)));
    }

    @ZenMethod
    static IICubeRenderer fromBlock(IBlock block) {
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlock(block).getDefaultState()));
    }

    @ZenMethod
    static IICubeRenderer fromBlock(IItemStack stack) {
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlock(stack).getDefaultState()));
    }

    @ZenMethod
    static IICubeRenderer fromState(IBlockState state) {
        return new MCCubeRenderer(new SidedCubeRenderer(CraftTweakerMC.getBlockState(state)));
    }

    @ZenMethod
    static IICubeRenderer sided(Map<IFacing, String> map) {
        EnumMap<EnumFacing, ResourceLocation> result = new EnumMap<>(EnumFacing.class);
        for(Map.Entry<IFacing, String> e : map.entrySet()) {
            if(result.put((EnumFacing) e.getKey().getInternal(), new ResourceLocation(e.getValue())) != null) {
                CraftTweakerAPI.logError("Duplicate key: " + e.getKey().getName());
            }
        }
        return new MCCubeRenderer(new SidedCubeRenderer(
                SidedCubeRenderer.fillBlanks(result)
        ));
    }

    @ZenMethod
    static IICubeRenderer sided(@Nonnull String up,
                                @Nullable String north,
                                @Nullable String east,
                                @Nullable String west,
                                @Nullable String south,
                                @Nullable String down) {

        EnumMap<EnumFacing, ResourceLocation> builder = new EnumMap<>(EnumFacing.class);

        builder.put(EnumFacing.UP, new ResourceLocation(up));

        if(north != null) builder.put(EnumFacing.NORTH, new ResourceLocation(north));
        if(east != null) builder.put(EnumFacing.EAST, new ResourceLocation(east));
        if(west != null) builder.put(EnumFacing.WEST, new ResourceLocation(west));
        if(south != null) builder.put(EnumFacing.SOUTH, new ResourceLocation(south));
        if(down != null) builder.put(EnumFacing.DOWN, new ResourceLocation(down));

        return new MCCubeRenderer(new SidedCubeRenderer(SidedCubeRenderer.fillBlanks(builder)));
    }

}
