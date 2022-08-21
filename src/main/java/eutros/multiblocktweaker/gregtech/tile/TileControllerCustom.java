package eutros.multiblocktweaker.gregtech.tile;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.IData;
import crafttweaker.api.formatting.IFormattedText;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.mc1120.world.MCBlockPos;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.functions.IPatternBuilderFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCIMultiblockPart;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCPatternMatchContext;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCRecipe;
import eutros.multiblocktweaker.gregtech.recipes.CustomMultiblockRecipeLogic;
import eutros.multiblocktweaker.gregtech.renderer.IBlockStateRenderer;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.PatternStringError;
import gregtech.api.recipes.Recipe;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileControllerCustom extends RecipeMapMultiblockController {

    public static final String TAG_PERSISTENT = MultiblockTweaker.MOD_ID + ":persistent";
    public final CustomMultiblock multiblock;
    // remove on error
    private IPatternBuilderFunction patternBuilderFunction;

    @Nullable
    public IData persistentData;

    public TileControllerCustom(@Nonnull CustomMultiblock multiblock) {
        super(multiblock.loc, multiblock.recipeMap);
        this.multiblock = multiblock;
        this.recipeMapWorkable = new CustomMultiblockRecipeLogic(this);
        patternBuilderFunction = multiblock.pattern;
    }

    public CustomMultiblockRecipeLogic getRecipeLogic(){
        return (CustomMultiblockRecipeLogic) recipeMapWorkable;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed() &&
            recipeMapWorkable.isWorkingEnabled() &&
            recipeMapWorkable.isActive()) {
            int eut = recipeMapWorkable.getRecipeEUt();
            if (eut < 0) {
                textList.add(new TextComponentTranslation("gregtech.multiblock.generation_eu", Math.min(-eut, energyContainer.getOutputVoltage())));
            }
        }

        if (multiblock.displayTextFunction == null) return;

        try {
            IFormattedText[] added = multiblock.displayTextFunction.addDisplayText(new MCControllerTile(this));
            if (added != null) {
                for (IFormattedText component : added) {
                    textList.add(new TextComponentString(component.getText()));
                }
            }
        } catch (RuntimeException e) {
            logFailure("displayTextFunction", e);
            multiblock.displayTextFunction = null;
        }

    }

    @Override
    public void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.energyContainer = new EnergyContainerList(
                Stream.of(MultiblockAbility.INPUT_ENERGY,
                        MultiblockAbility.OUTPUT_ENERGY)
                        .map(this::getAbilities)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );
        for (IMultiblockPart part : getMultiblockParts()) {
            if (part instanceof MetaTileEntity) {
                ICubeRenderer renderer = getBaseTexture(part);
                if (renderer instanceof IBlockStateRenderer && !((IBlockStateRenderer) renderer).state.isOpaqueCube()) {
                    IBlockState blockState = getWorld().getBlockState(((MetaTileEntity) part).getPos());
                    if (blockState.getValue(BlockMachine.OPAQUE)) {
                        getWorld().setBlockState(((MetaTileEntity) part).getPos(), blockState.withProperty(BlockMachine.OPAQUE, false));
                    }
                }
            }
        }

        if (multiblock.formStructureFunction == null) return;

        try {
            multiblock.formStructureFunction.formStructure(new MCControllerTile(this), new MCPatternMatchContext(context));
        } catch (RuntimeException e) {
            logFailure("formStructureFunction", e);
            multiblock.formStructureFunction = null;
        }
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        if (multiblock.invalidateStructureFunction != null) {
            try {
                multiblock.invalidateStructureFunction.run(new MCControllerTile(this));
            } catch (RuntimeException e) {
                logFailure("invalidateStructureFunction", e);
                multiblock.invalidateStructureFunction = null;
            }
        }
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        if (multiblock.checkRecipeFunction == null) return true;

        try {
            return multiblock.checkRecipeFunction.test(
                    new MCControllerTile(this),
                    new MCRecipe(recipe),
                    consumeIfSuccess
            );
        } catch (RuntimeException e) {
            logFailure("recipePredicate", e);
            multiblock.checkRecipeFunction = null;
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        ICubeRenderer renderer = getBaseTexture(null);
        if (renderer instanceof IBlockStateRenderer) {
            return ((IBlockStateRenderer) renderer).state.isOpaqueCube();
        }
        return super.isOpaqueCube();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (multiblock.removalFunction == null) return;

        try {
            multiblock.removalFunction.onRemoval(new MCControllerTile(this));
        } catch (RuntimeException e) {
            logFailure("removalFunction", e);
            multiblock.removalFunction = null;
        }
    }

    private void logFailure(String func, Throwable t) {
        CraftTweakerAPI.logError(String.format("Couldn't run %s function of %s.", func, multiblock), t);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if  (multiblock.addInformationFunction != null) {
            try {
                tooltip.addAll(Arrays.stream(multiblock.addInformationFunction.addTips(new MCControllerTile(this))).collect(Collectors.toList()));
            } catch (RuntimeException e) {
                logFailure("addInformationFunction", e);
                multiblock.addInformationFunction = null;
            }
        }
    }

    @Override
    public void updateFormedValid() {
        super.updateFormedValid();
        if (multiblock.updateFormedValidFunction != null) {
            try {
                multiblock.updateFormedValidFunction.run(new MCControllerTile(this));
            } catch (RuntimeException e) {
                logFailure("updateFormedValidFunction", e);
                multiblock.updateFormedValidFunction = null;
            }
        }
    }

    @Override
    public void causeMaintenanceProblems() {
        super.causeMaintenanceProblems();
    }

    @Override
    public BlockPattern createStructurePattern() {
        if (patternBuilderFunction != null) {
            try {
                return patternBuilderFunction.build(new MCControllerTile(this)).getInternal();
            } catch (RuntimeException e) {
                logFailure("pattern", e);
                patternBuilderFunction = null;
            }
        }
        return FactoryBlockPattern.start()
                .aisle("S", "E")
                .where('E', tilePredicate((worldState, mte) -> {
                    worldState.setError(new PatternStringError("MBT controller pattern error"));
                    return false;
                }, null))
                .where('S', selfPredicate()).build();
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        if (multiblock.designs != null) {
            return multiblock.designs;
        }
        return super.getMatchingShapes();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart part) {
        if (multiblock.getBaseTextureFunction != null) {
            try {
                return multiblock.getBaseTextureFunction.get(new MCControllerTile(this), part instanceof MetaTileEntityMultiblockPart ? new MCIMultiblockPart((MetaTileEntityMultiblockPart) part) : null);
            } catch (RuntimeException e) {
                logFailure("getBaseTextureFunction", e);
                multiblock.getBaseTextureFunction = null;
            }
        }
        return multiblock.baseTexture;
    }

    @NotNull
    @Override
    public ICubeRenderer getFrontOverlay() {
        return multiblock.frontOverlay == null ? super.getFrontOverlay() : multiblock.frontOverlay;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity holder) {
        return new TileControllerCustom(multiblock);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);

        if (persistentData != null)
            data.setTag(TAG_PERSISTENT, CraftTweakerMC.getNBT(persistentData));

        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);

        persistentData = CraftTweakerMC.getIData(data.getTag(TAG_PERSISTENT));
    }

    @Override
    public void replaceVariantBlocksActive(boolean isActive) {
        super.replaceVariantBlocksActive(isActive);
    }

    public List<IBlockPos> getVariantActiveBlocks() {
        return variantActiveBlocks.stream().map(MCBlockPos::new).collect(
                Collectors.toList());
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return multiblock.hasMaintenanceMechanics == null ? super.hasMaintenanceMechanics() : multiblock.hasMaintenanceMechanics;
    }

    @Override
    public boolean hasMufflerMechanics() {
        return multiblock.hasMufflerMechanics == null ? super.hasMufflerMechanics() : multiblock.hasMufflerMechanics;
    }

    @Override
    protected boolean allowSameFluidFillForOutputs() {
        return multiblock.allowSameFluidFillForOutputs == null ? super.allowSameFluidFillForOutputs() : multiblock.allowSameFluidFillForOutputs;
    }

    @Override
    public boolean canBeDistinct() {
        return multiblock.canBeDistinct == null ? super.canBeDistinct() : multiblock.canBeDistinct;
    }

    @Override
    protected boolean shouldShowVoidingModeButton() {
        return multiblock.shouldShowVoidingModeButton == null ? super.shouldShowVoidingModeButton() : multiblock.shouldShowVoidingModeButton;
    }
}
