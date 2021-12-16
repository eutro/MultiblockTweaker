package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.brackethandler.MetaTileEntityBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A GregTech meta tile entity.
 *
 * @zenClass mods.gregtech.IMetaTileEntity
 * @see MetaTileEntityBracketHandler
 */
@ZenClass("mods.gregtech.IMetaTileEntity")
@ZenRegister
public interface IMetaTileEntity {

    @NotNull
    MetaTileEntity getInternal();

    /**
     * Get a meta tile entity by its ID.
     *
     * @param id The ID of the meta tile entity.
     * @return The meta tile entity referenced by the ID, or null.
     */
    @ZenMethod
    @Nullable
    static IMetaTileEntity byId(@NotNull String id) {
        ResourceLocation loc = new ResourceLocation(id);

        if (loc.getNamespace().equals("minecraft")) {
            loc = new ResourceLocation(MultiblockTweaker.MOD_ID, loc.getPath());
        }

        MetaTileEntity te = GregTechAPI.MTE_REGISTRY.getObject(loc);

        if (te != null)
            return new MCMetaTileEntity(te);

        return null;
    }

    /**
     * get {@link IMetaTileEntity} from world.
     * @param world world
     * @param pos blockpos
     * @return mte;
     */
    @ZenMethod
    default IMetaTileEntity fromWorldPos(@Nonnull IWorld world, @Nonnull IBlockPos pos){
        TileEntity te = CraftTweakerMC.getWorld(world).getTileEntity(CraftTweakerMC.getBlockPos(pos));
        if (te instanceof MetaTileEntityHolder && ((MetaTileEntityHolder) te).isValid()) {
            return new MCMetaTileEntity(((MetaTileEntityHolder) te).getMetaTileEntity());
        }
        return null;
    }

    /**
     * @return The meta id of this mte.
     */
    @ZenMethod
    @ZenGetter("metaTileEntityId")
    String getMetaTileEntityId();

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
    long getOffsetTimer();

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

    @ZenMethod
    void markDirty();

    @ZenMethod
    boolean isFirstTick();

    @ZenMethod
    int getInputRedstoneSignal(IFacing side, boolean ignoreCover);

    @ZenMethod
    boolean isBlockRedstonePowered();

    @ZenMethod
    int getActualLightValue();

    @ZenMethod
    void update();

    @ZenMethod
    IItemStack getStackForm(int amount);

    /**
     * Whether this tile entity represents completely opaque cube
     *
     * @return true if machine is opaque
     */
    @ZenMethod
    boolean isOpaqueCube();

    @ZenMethod
    int getLightOpacity();

    /**
     * @return tool required to dismantle this meta tile entity properly
     */
    @ZenMethod
    String getHarvestTool();

    /**
     * @return minimal level of tool required to dismantle this meta tile entity properly
     */
    @ZenMethod
    int getHarvestLevel();

    @ZenMethod
    int getOutputRedstoneSignal(IFacing side);

    @ZenMethod
    void setOutputRedstoneSignal(IFacing side, int strength);

    @ZenMethod
    void notifyBlockUpdate();

    @ZenMethod
    void scheduleRenderUpdate();

    @ZenMethod
    void setFrontFacing(IFacing frontFacing);

    @ZenMethod
    void setPaintingColor(int paintingColor);

    @ZenMethod
    void setFragile(boolean fragile);

    @ZenMethod
    boolean isValidFrontFacing(IFacing facing);

    @ZenMethod
    boolean hasFrontFacing();

    @ZenMethod
    boolean isValid();

    @ZenMethod
    int getPaintingColor();

    @ZenMethod
    IIItemHandlerModifiable getImportItems();

    @ZenMethod
    IIItemHandlerModifiable getExportItems();

    @ZenMethod
    IIFluidHandler getImportFluids();

    @ZenMethod
    IIFluidHandler getExportFluids();

    @ZenMethod
    List<IIItemHandlerModifiable> getNotifiedItemOutputList();

    @ZenMethod
    List<IIItemHandlerModifiable> getNotifiedItemInputList();

    @ZenMethod
    List<IIFluidHandler> getNotifiedFluidInputList();

    @ZenMethod
    List<IIFluidHandler> getNotifiedFluidOutputList();

    @ZenMethod
    boolean isFragile();

    @ZenMethod
    boolean shouldDropWhenDestroyed();

    @ZenMethod
    void toggleMuffled();

    @ZenMethod
    boolean isMuffled();
}
