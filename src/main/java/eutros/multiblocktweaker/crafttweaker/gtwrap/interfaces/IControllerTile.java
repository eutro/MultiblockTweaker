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

/**
 * A IControllerTile block of the multiblock controller.
 *
 * @zenClass mods.gregtech.IControllerTile
 */
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
     * Get {@link IControllerTile} from world.
     *
     * @param world The world to get from.
     * @param pos The position to get from.
     * @return The controller at that position in the world.
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

    // --- MultiblockControllerBase

    /**
     * Get parts of this multiblock controller.
     * @return Parts list of this controller.
     */
    @ZenMethod
    @ZenGetter("multiblockParts")
    List<IIMultiblockPart> getMultiblockParts();

    /**
     *
     * @return Can parts of this controller be shared.
     */
    @ZenMethod
    @ZenGetter("canShare")
    boolean canShare();

    /**
     *
     * @return The shapes demo of this controller.
     */
    @ZenMethod
    @ZenGetter("matchingShapes")
    List<IMultiblockShapeInfo> getMatchingShapes();

    /**
     *
     * @return FrontOverlay renderer.
     */
    @ZenMethod
    @ZenGetter("frontOverlay")
    IICubeRenderer getFrontOverlay();

    /**
     *
     * @return The structure pattern of this controller.
     */
    @ZenMethod
    IBlockPattern createStructurePattern();

    /**
     * Should render base texture for specific part.
     * @param sourcePart The part block of this controller.
     * @return result.
     */
    @ZenMethod
    boolean shouldRenderOverlay(IIMultiblockPart sourcePart);

    /**
     * Get base texture of specific part.
     * @param part The part block of this controller.
     * @return Texture renderer.
     */
    @ZenMethod
    IICubeRenderer getBaseTexture(IIMultiblockPart part);

    /**
     * Check structure is formed.
     */
    @ZenMethod
    void checkStructurePattern();

    /**
     * Called when the structure is formed.
     *
     * @param context Pattern context.
     */
    @ZenMethod
    void formStructure(IPatternMatchContext context);

    /**
     * Get the light value of a specific part.
     * @param sourcePart The part block of this controller.
     * @return Light value.
     */
    @ZenMethod
    int getLightValueForPart(IIMultiblockPart sourcePart);

    /**
     * Called when the structure is invalidated.
     */
    @ZenMethod
    void invalidateStructure();

    /**
     * Called every tick, when the structure is formed.
     */
    @ZenMethod
    void updateFormedValid();

    /**
     * //TODO unavailable.
     * @param ability ability.
     * @return IAny is un available.
     */
    @ZenMethod
    Object[] getAbilities(IMultiblockAbility ability);

    /**
     * Is structure formed.
     *
     * @return result.
     */
    @ZenMethod
    @ZenGetter
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

    /**
     * Automatically generate ability predicates according to the RecipeMap.
     *
     * @return Predicate according to the RecipeMap.
     */
    @ZenMethod
    @ZenGetter("autoAbilities")
    CTTraceabilityPredicate autoAbilities();

    /**
     * Automatically generate (optional) ability predicates according to the RecipeMap.
     *
     * @param checkEnergyIn Should check Energy Input.
     * @param checkMaintainer Should check Maintainer.
     * @param checkItemIn Should check Item Input.
     * @param checkItemOut Should check Item Output.
     * @param checkFluidIn Should check Fluid Input.
     * @param checkFluidOut Should check Fluid Output.
     * @param checkMuffler Should check Muffler.
     * @return Predicate according to the RecipeMap.
     */
    @ZenMethod
    CTTraceabilityPredicate autoAbilities(boolean checkEnergyIn,
                                        boolean checkMaintainer,
                                        boolean checkItemIn,
                                        boolean checkItemOut,
                                        boolean checkFluidIn,
                                        boolean checkFluidOut,
                                        boolean checkMuffler);

    /**
     * Predicate of this controller. You should always use this to define the controller of the mltiblock.
     *
     * @return Predicate of this controller.
     */
    @ZenMethod
    @ZenGetter("SELF")
    CTTraceabilityPredicate self();

    // --- MultiblockWithDisplayBase

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
    @ZenGetter
    boolean hasMaintenanceProblems();

    /**
     * @return whether this multiblock has maintenance mechanics
     */
    @ZenMethod
    @ZenGetter
    boolean hasMaintenanceMechanics();

    /**
     *
     * @return whether this multiblock has muffler mechanics
     */
    @ZenMethod
    @ZenGetter
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
    @ZenGetter
    boolean isMufflerFaceFree();

    /**
     * @return whether the current multiblock is active or not
     */
    @ZenMethod
    @ZenGetter
    boolean isActive();

    // --- RecipeMapMultiblockController

    /**
     *
     * @return Energy container.
     */
    @ZenMethod
    @ZenGetter("energyContainer")
    IIEnergyContainer getEnergyContainer();

    /**
     *
     * @return Input inventory.
     */
    @ZenMethod
    @ZenGetter("inputInventory")
    IIItemHandlerModifiable getInputInventory();

    /**
     *
     * @return Output inventory.
     */
    @ZenMethod
    @ZenGetter("outputInventory")
    IIItemHandlerModifiable getOutputInventory();

    /**
     *
     * @return Input fluid inventory.
     */
    @ZenMethod
    @ZenGetter("inputFluidInventory")
    IIMultipleTankHandler getInputFluidInventory();

    /**
     *
     * @return Output fluid inventory.
     */
    @ZenMethod
    @ZenGetter("outputFluidInventory")
    IIMultipleTankHandler getOutputFluidInventory();

    /**
     * The recipe logic handler of this multiblock.
     * 
     * @return Recipe logic.
     */
    @ZenMethod
    @ZenGetter("recipeLogic")
    IRecipeLogic getRecipeLogic();

    /**
     * Checking whether the found recipe is available.
     * 
     * @param recipe recipe be found.
     * @param consumeIfSuccess is consume if success
     * @return result
     */
    @ZenMethod
    boolean checkRecipe(IRecipe recipe, boolean consumeIfSuccess);

    /**
     * Change active state for all VariantBlock {@link gregtech.common.blocks.VariantActiveBlock#ACTIVE} blocks. 
     * which are blocks of this multiblock.
     * 
     * @param isActive New state.
     */
    @ZenMethod
    void replaceVariantBlocksActive(boolean isActive);

    /**
     * 
     * @return Can input buses be distinct.
     */
    @ZenMethod
    @ZenGetter
    boolean canBeDistinct();

    /**
     * 
     * @return Is distinct.
     */
    @ZenMethod
    @ZenGetter
    boolean isDistinct();

    /**
     * 
     * @return Get all available RecipeMaps.
     */
    @ZenMethod
    RecipeMap<?> getRecipeMap();

    /**
     * @return Get position list of all variant active blocks
     */
    @ZenGetter("variantActiveBlocks")
    List<IBlockPos> getVariantActiveBlocks();
}
