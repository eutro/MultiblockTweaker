package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.data.IData;
import crafttweaker.api.world.IBlockPos;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockPattern;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIEnergyContainer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIItemHandlerModifiable;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultiblockPart;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultipleTankHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockShapeInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import eutros.multiblocktweaker.crafttweaker.predicate.CTTraceabilityPredicate;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.recipes.RecipeMap;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MCControllerTile extends MCMetaTileEntity implements IControllerTile {

    public MCControllerTile(@NotNull TileControllerCustom inner) {
        super(inner);
    }

    @Override
    @NotNull
    public TileControllerCustom getInternal() {
        return (TileControllerCustom) super.getInternal();
    }

    @Override
    public CustomMultiblock getMultiblock() {
        return getInternal().multiblock;
    }

    @Override
    public IIEnergyContainer getEnergyContainer() {
        return new MCIEnergyContainer(getInternal().getEnergyContainer());
    }


    @Override
    public IIItemHandlerModifiable getInputInventory() {
        return new MCIItemHandlerModifiable(getInternal().getInputInventory());
    }

    @Override
    public IIItemHandlerModifiable getOutputInventory() {
        return new MCIItemHandlerModifiable(getInternal().getOutputInventory());
    }

    @Override
    public IIMultipleTankHandler getInputFluidInventory() {
        return new MCIMultipleTankHandler(getInternal().getInputFluidInventory());
    }

    @Override
    public IIMultipleTankHandler getOutputFluidInventory() {
        return new MCIMultipleTankHandler(getInternal().getOutputFluidInventory());
    }

    @Override
    public IRecipeLogic getRecipeLogic() {
        return getInternal().getRecipeLogic();
    }

    @Override
    public boolean checkRecipe(IRecipe recipe, boolean consumeIfSuccess) {
        return getInternal().checkRecipe(recipe.getInner(), consumeIfSuccess);
    }

    @Override
    public void replaceVariantBlocksActive(boolean isActive) {
        getInternal().replaceVariantBlocksActive(isActive);
    }

    @Override
    public boolean canBeDistinct() {
        return getInternal().canBeDistinct();
    }

    @Override
    public boolean isDistinct() {
        return getInternal().isDistinct();
    }

    @Override
    public boolean canCreateSound() {
        return getInternal().canCreateSound();
    }

    @Override
    public RecipeMap<?>[] getAvailableRecipeMaps() {
        return getInternal().getAvailableRecipeMaps();
    }

    @Override
    public int getRecipeMapIndex() {
        return getInternal().getRecipeMapIndex();
    }

    @Override
    public void addRecipeMaps(RecipeMap<?>... recipeMaps) {
        getInternal().addRecipeMaps(recipeMaps);
    }

    @Override
    public void setRecipeMapIndex(int index) {
        getInternal().setRecipeMapIndex(index);
    }

    @Override
    public RecipeMap<?> getCurrentRecipeMap() {
        return  getInternal().getCurrentRecipeMap();
    }

    @Override
    public List<IBlockPos> getVariantActiveBlocks() {
        return getInternal().getVariantActiveBlocks();
    }

    @Override
    public List<IIMultiblockPart> getMultiblockParts() {
        return getInternal().getMultiblockParts().stream()
                .filter(MetaTileEntityMultiblockPart.class::isInstance)
                .map(MetaTileEntityMultiblockPart.class::cast)
                .map(MCIMultiblockPart::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canShare() {
        return getInternal().canShare();
    }

    @Override
    public List<IMultiblockShapeInfo> getMatchingShapes() {
        return getInternal().getMatchingShapes().stream().map(MCMultiblockShapeInfo::new).collect(
                Collectors.toList());
    }

    @Override
    public IICubeRenderer getFrontOverlay() {
        return new MCICubeRenderer(getInternal().getFrontOverlay());
    }

    @Override
    public IBlockPattern createStructurePattern() {
        return new MCBlockPattern(getInternal().createStructurePattern());
    }

    @Override
    public boolean shouldRenderOverlay(IIMultiblockPart sourcePart) {
        return getInternal().shouldRenderOverlay(sourcePart.getInternal());
    }

    @Override
    public IICubeRenderer getBaseTexture(IIMultiblockPart part) {
        return new MCICubeRenderer(getInternal().getBaseTexture(part.getInternal()));
    }

    @Override
    public void checkStructurePattern() {
        getInternal().checkStructurePattern();
    }

    @Override
    public void formStructure(IPatternMatchContext context) {
        getInternal().formStructure(context.getInternal());
    }

    @Override
    public int getLightValueForPart(IIMultiblockPart sourcePart) {
        return getInternal().getLightValueForPart(sourcePart.getInternal());
    }

    @Override
    public void invalidateStructure() {
        getInternal().invalidateStructure();
    }

    @Override
    public void updateFormedValid() {
        getInternal().updateFormedValid();
    }

    @Override
    public void update() {
        getInternal().update();
    }

    @Override
    public Object[] getAbilities(IMultiblockAbility ability) {
        return getInternal().getAbilities(ability.getInternal()).toArray();
    }

    @Override
    public boolean isStructureFormed() {
        return getInternal().isStructureFormed();
    }

    @Override
    public IData getExtraData() {
        return getInternal().persistentData;
    }

    @Override
    public CTTraceabilityPredicate autoAbilities() {
        return new CTTraceabilityPredicate(getInternal().autoAbilities());
    }

    @Override
    public CTTraceabilityPredicate autoAbilities(boolean checkEnergyIn,
                                                 boolean checkMaintainer,
                                                 boolean checkItemIn,
                                                 boolean checkItemOut,
                                                 boolean checkFluidIn,
                                                 boolean checkFluidOut,
                                                 boolean checkMuffler) {
        return new CTTraceabilityPredicate(
                getInternal().autoAbilities(checkEnergyIn,
                        checkMaintainer,
                        checkItemIn,
                        checkItemOut,
                        checkFluidIn,
                        checkFluidOut,
                        checkMuffler));
    }

    @Override
    public CTTraceabilityPredicate self() {
        return new CTTraceabilityPredicate(getInternal().selfPredicate());
    }

    @Override
    public void setMaintenanceFixed(int index) {
        getInternal().setMaintenanceFixed(index);
    }

    @Override
    public void causeMaintenanceProblems() {
        getInternal().causeMaintenanceProblems();
    }

    @Override
    public byte getMaintenanceProblems() {
        return getInternal().getMaintenanceProblems();
    }

    @Override
    public int getNumMaintenanceProblems() {
        return getInternal().getNumMaintenanceProblems();
    }

    @Override
    public boolean hasMaintenanceProblems() {
        return getInternal().hasMaintenanceProblems();
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return getInternal().hasMaintenanceMechanics();
    }

    @Override
    public boolean hasMufflerMechanics() {
        return getInternal().hasMufflerMechanics();
    }

    @Override
    public void storeTaped(boolean isTaped) {
        getInternal().storeTaped(isTaped);
    }

    @Override
    public void outputRecoveryItems() {
        getInternal().outputRecoveryItems();
    }

    @Override
    public void outputRecoveryItems(int parallel) {
        getInternal().outputRecoveryItems(parallel);
    }

    @Override
    public boolean isMufflerFaceFree() {
        return getInternal().isMufflerFaceFree();
    }

    @Override
    public boolean isActive() {
        return getInternal().isActive();
    }

    @Override
    public void setExtraData(IData data) {
        getInternal().persistentData = data;
    }

}
