package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCControllerTile;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import javax.annotation.Nonnull;
import java.util.List;

@ZenClass("mods.gregtech.IControllerTile")
@ZenRegister
public interface IControllerTile extends IMetaTileEntity {

    @Override
    @NotNull TileControllerCustom getInternal();

    /**
     * @return The {@link CustomMultiblock} that this is the controller for.
     */
    @ZenMethod
    @ZenGetter("multiblock")
    CustomMultiblock getMultiblock();

    /**
     * get {@link IControllerTile} from world.
     * @param world world
     * @param pos blockpos
     * @return mte;
     */
    @ZenMethod
    default IControllerTile fromWorldPos(@Nonnull IWorld world, @Nonnull IBlockPos pos){
        TileEntity te = CraftTweakerMC.getWorld(world).getTileEntity(CraftTweakerMC.getBlockPos(pos));
        if (te instanceof MetaTileEntityHolder && ((MetaTileEntityHolder) te).isValid()) {
            MetaTileEntity mte = ((MetaTileEntityHolder) te).getMetaTileEntity();
            return mte instanceof TileControllerCustom ? new MCControllerTile((TileControllerCustom)mte) : null;
        }
        return null;
    }

    // **********************MultiblockControllerBase
    @ZenMethod
    @ZenGetter("multiblockParts")
    List<IIMultiblockPart> getMultiblockParts();

    @ZenMethod
    @ZenGetter("canShare")
    boolean canShare();

    @ZenMethod
    @ZenGetter("matchingShapes")
    List<IMultiblockShapeInfo> getMatchingShapes();

    @ZenMethod
    @ZenGetter("frontOverlay")
    IICubeRenderer getFrontOverlay();

    @ZenMethod
    IBlockPattern createStructurePattern();

    @ZenMethod
    boolean shouldRenderOverlay(IIMultiblockPart sourcePart);

    @ZenMethod
    IICubeRenderer getBaseTexture(IIMultiblockPart part);

    @ZenMethod
    void checkStructurePattern();

    @ZenMethod
    void formStructure(IPatternMatchContext context);

    @ZenMethod
    int getLightValueForPart(IIMultiblockPart sourcePart);

    @ZenMethod
    void invalidateStructure();

    @ZenMethod
    void updateFormedValid();

    @ZenMethod
    Object[] getAbilities(IMultiblockAbility ability);

    @ZenMethod
    boolean isStructureFormed();

    /**
     * Store extra data for retrieval with {@link #getExtraData()}
     * <p>
     * This will be stored in the tile's NBT when the world is saved,
     * so will persist even through restarts.
     *
     * @param data The extra data to store on the controller.
     * @zenSetter extraData
     */
    @ZenMethod
    @ZenSetter("extraData")
    void setExtraData(IData data);

    /**
     * Retrieve extra data stored with {@link #setExtraData(IData)}
     *
     * @return The extra data stored on this controller.
     */
    @ZenMethod
    @ZenGetter("extraData")
    IData getExtraData();

    @ZenMethod
    @ZenGetter("autoAbilities")
    CTTraceabilityPredicate autoAbilities();

    @ZenMethod
    CTTraceabilityPredicate autoAbilities(boolean checkEnergyIn,
                                        boolean checkMaintainer,
                                        boolean checkItemIn,
                                        boolean checkItemOut,
                                        boolean checkFluidIn,
                                        boolean checkFluidOut,
                                        boolean checkMuffler);
    @ZenMethod
    CTTraceabilityPredicate SELF();

    // ***********************************MultiblockWithDisplayBase


    /**
     * Sets the maintenance problem corresponding to index to fixed
     *
     * @param index of the maintenance problem
     */
    @ZenMethod
    void setMaintenanceFixed(int index);

    /**
     * Used to cause a single random maintenance problem
     */
    @ZenMethod
    void causeMaintenanceProblems();

    /**
     * @return the byte value representing the maintenance problems
     */
    @ZenMethod
    byte getMaintenanceProblems();

    /**
     * @return the amount of maintenance problems the multiblock has
     */
    @ZenMethod
    int getNumMaintenanceProblems();

    /**
     * @return whether the multiblock has any maintenance problems
     */
    @ZenMethod
    boolean hasMaintenanceProblems();

    /**
     * @return whether this multiblock has maintenance mechanics
     */
    @ZenMethod
    boolean hasMaintenanceMechanics();

    @ZenMethod
    boolean hasMufflerMechanics();

    /**
     * Stores the taped state of the maintenance hatch
     *
     * @param isTaped is whether the maintenance hatch is taped or not
     */
    @ZenMethod
    void storeTaped(boolean isTaped);

    /**
     * Outputs the recovery items into the muffler hatch
     */
    @ZenMethod
    void outputRecoveryItems();

    @ZenMethod
    void outputRecoveryItems(int parallel);

    /**
     * @return whether the muffler hatch's front face is free
     */
    @ZenMethod
    boolean isMufflerFaceFree();

    /**
     * @return whether the current multiblock is active or not
     */
    @ZenMethod
    boolean isActive();

    // **********************************RecipeMapMultiblockController

    @ZenMethod
    @ZenGetter("energyContainer")
    IIEnergyContainer getEnergyContainer();

    @ZenMethod
    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInputInventory();

    @ZenMethod
    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutputInventory();

    @ZenMethod
    @ZenGetter("inputFluidInventory")
    IIMultipleTankHandler getInputFluidInventory();

    @ZenMethod
    @ZenGetter("outputFluidInventory")
    IIMultipleTankHandler getOutputFluidInventory();

    @ZenMethod
    boolean checkRecipe(IRecipe recipe, boolean consumeIfSuccess);

    @ZenMethod
    void replaceVariantBlocksActive(boolean isActive);

    @ZenMethod
    boolean canBeDistinct();

    @ZenMethod
    boolean isDistinct();

    @ZenMethod
    boolean canCreateSound();

    @ZenMethod
    RecipeMap<?>[] getAvailableRecipeMaps();

    @ZenMethod
    int getRecipeMapIndex();

    @ZenMethod
    void addRecipeMaps(RecipeMap<?>... recipeMaps);

    @ZenMethod
    void setRecipeMapIndex(int index);

    @ZenMethod
    RecipeMap<?> getCurrentRecipeMap();

    @ZenMethod
    List<IBlockPos> getVariantActiveBlocks();
}
