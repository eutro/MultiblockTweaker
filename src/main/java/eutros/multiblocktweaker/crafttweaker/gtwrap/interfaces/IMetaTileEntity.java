package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.IMetaTileEntity")
@ZenRegister
public interface IMetaTileEntity {

    @NotNull
    MetaTileEntity getInternal();

    @ZenMethod
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

    @ZenMethod
    IWorld getWorld();

    @ZenMethod
    IBlockPos getPos();

    @ZenMethod
    long getTimer();

    @ZenMethod
    String getMetaName();

    @ZenMethod
    String getMetaFullName();

    @ZenMethod
    IFacing getFrontFacing();

}
