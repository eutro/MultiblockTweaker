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
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenCaster;
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
     * Get an {@link IMetaTileEntity} from the world.
     *
     * @param world The world to get from.
     * @param pos The position to get from.
     * @return The meta tile entity at that position in the world.
     */
    @ZenMethod
    static IMetaTileEntity fromWorldPos(@Nonnull IWorld world, @Nonnull IBlockPos pos){
        TileEntity te = CraftTweakerMC.getWorld(world).getTileEntity(CraftTweakerMC.getBlockPos(pos));
        if (te instanceof MetaTileEntityHolder && ((MetaTileEntityHolder) te).isValid()) {
            return new MCMetaTileEntity(((MetaTileEntityHolder) te).getMetaTileEntity());
        }
        return null;
    }

    /**
     * @return The meta id of this meta tile entity.
     */
    @ZenMethod
    @ZenGetter("metaTileEntityId")
    String getMetaTileEntityId();

    /**
     * @return The world the meta tile entity is in.
     */
    @ZenMethod
    @ZenGetter("world")
    IWorld getWorld();

    /**
     * @return The position the meta tile entity is in.
     */
    @ZenMethod
    @ZenGetter("pos")
    IBlockPos getPos();

    /**
     * @return How long the meta tile entity has been in the world for.
     */
    @ZenMethod
    @ZenGetter("offsetTimer")
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
    @ZenGetter("frontFacing")
    IFacing getFrontFacing();

    /**
     * Mark dirty.
     */
    @ZenMethod
    void markDirty();

    /**
     *
     * @return is the first tick of this tile entity.
     */
    @ZenMethod
    boolean isFirstTick();

    /**
     * Get input redstone signal.
     * 
     * @param side input side.
     * @param ignoreCover ignore the Cover at this side.
     * @return redstone signal.
     */
    @ZenMethod
    int getInputRedstoneSignal(IFacing side, boolean ignoreCover);

    /**
     * 
     * @return is block redstone powered.
     */
    @ZenMethod
    boolean isBlockRedstonePowered();

    /**
     * 
     * @return get actual light value.
     */
    @ZenMethod
    int getActualLightValue();

    /**
     * Called every tick.
     */
    @ZenMethod
    void update();

    /**
     * Get an IItemStack of this tile entity with specific amount.
     * 
     * @param amount amount.
     * @return IItemStack of it.
     */
    @ZenMethod
    IItemStack getStackForm(int amount);

    /**
     * Whether this tile entity represents completely opaque cube
     *
     * @return true if machine is opaque
     */
    @ZenMethod
    boolean isOpaqueCube();

    /**
     * 
     * @return get light opacity of it.
     */
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

    /**
     * Get output redstone signal.
     *
     * @param side output side.
     * @return redstone signal.
     */
    @ZenMethod
    int getOutputRedstoneSignal(IFacing side);

    /**
     * Set output redstone signal.
     *
     * @param side output side.
     * @param strength singal strength.
     */
    @ZenMethod
    void setOutputRedstoneSignal(IFacing side, int strength);

    /**
     * Should be called on server side. Notify surround blocks update.
     */
    @ZenMethod
    void notifyBlockUpdate();

    /**
     * Should be called on client side. Schedule chunk rebuilt.
     */
    @ZenMethod
    void scheduleRenderUpdate();

    /**
     * Set the front facing of it.
     *
     * @param frontFacing front side.
     */
    @ZenMethod
    void setFrontFacing(IFacing frontFacing);

    /**
     * Set painting color of it.
     *
     * @param paintingColor painting color.
     */
    @ZenMethod
    void setPaintingColor(int paintingColor);

    /**
     * Set fragile. Affects the render animation when destroyed.
     *
     * @param fragile is fragile.
     */
    @ZenMethod
    void setFragile(boolean fragile);

    /**
     * Is the facing side a valid side as the front.
     *
     * @param facing facing side.
     * @return is valid.
     */
    @ZenMethod
    boolean isValidFrontFacing(IFacing facing);

    /**
     *
     * @return has front facing side.
     */
    @ZenMethod
    boolean hasFrontFacing();

    /**
     *
     * @return is tile entity valid.
     */
    @ZenMethod
    boolean isValid();

    /**
     *
     * @return get painting color.
     */
    @ZenMethod
    int getPaintingColor();

    /**
     *
     * @return get import items.
     */
    @ZenMethod
    IIItemHandlerModifiable getImportItems();

    /**
     *
     * @return get export items.
     */
    @ZenMethod
    IIItemHandlerModifiable getExportItems();

    /**
     *
     * @return get import fluids.
     */
    @ZenMethod
    IIFluidHandler getImportFluids();

    /**
     *
     * @return get export fluids.
     */
    @ZenMethod
    IIFluidHandler getExportFluids();

    /**
     *
     * @return get notified item output list.
     */
    @ZenMethod
    List<IIItemHandlerModifiable> getNotifiedItemOutputList();

    /**
     *
     * @return get notified item input list.
     */
    @ZenMethod
    List<IIItemHandlerModifiable> getNotifiedItemInputList();

    /**
     *
     * @return get notified fluid input list.
     */
    @ZenMethod
    List<IIFluidHandler> getNotifiedFluidInputList();

    /**
     *
     * @return get notified fluid output list.
     */
    @ZenMethod
    List<IIFluidHandler> getNotifiedFluidOutputList();

    /**
     *
     * @return is fragile.
     */
    @ZenMethod
    boolean isFragile();

    /**
     *
     * @return should drop when destroyed.
     */
    @ZenMethod
    boolean shouldDropWhenDestroyed();

    /**
     * Toggle muffled.
     */
    @ZenMethod
    void toggleMuffled();

    /**
     *
     * @return is muffled
     */
    @ZenMethod
    boolean isMuffled();

    @ZenCaster
    CTTraceabilityPredicate castCTPredicate();
}
