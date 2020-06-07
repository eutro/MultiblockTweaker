package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantMetaTileEntities;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * A GregTech meta tile entity.
 *
 * @see ConstantMetaTileEntities
 */
@ZenClass("mods.gregtech.IMetaTileEntity")
@ZenRegister
public interface IMetaTileEntity {

    @NotNull
    MetaTileEntity getInternal();

    /**
     * Get a meta tile entity by its ID.
     * @param id The ID of the meta tile entity.
     * @return The meta tile entity referenced by the ID, or null.
     */
    @ZenMethod
    @Nullable
    static IMetaTileEntity byId(@NotNull String id) {
        ResourceLocation loc = new ResourceLocation(id);

        if(loc.getResourceDomain().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getResourcePath());
        }

        MetaTileEntity te = GregTechAPI.META_TILE_ENTITY_REGISTRY.getObject(loc);

        if(te != null)
            return new MCMetaTileEntity(te);

        return null;
    }

    /**
     * @return The world the meta tile entity is in.
     */
    @ZenMethod
    IWorld getWorld();

    /**
     * @return The position the meta tile entity is in.
     */
    @ZenMethod
    IBlockPos getPos();

    /**
     * @return How long the meta tile entity has been in the world for.
     */
    @ZenMethod
    long getTimer();

    /**
     * @return The unlocalized name of the machine, excluding {@code .name} at the end.
     */
    @ZenMethod
    String getMetaName();

    /**
     * @return The unlocalized name of the machine, including {@code .name} at the end.
     */
    @ZenMethod
    String getMetaFullName();

    /**
     * @return The direction the meta tile entity is facing.
     */
    @ZenMethod
    IFacing getFrontFacing();

}
