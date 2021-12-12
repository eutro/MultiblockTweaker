package eutros.multiblocktweaker.gregtech.tile;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.IData;
import crafttweaker.api.formatting.IFormattedText;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.functions.IAddInformationFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IDisplayTextFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IFormStructureFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IPatternBuilderFunction;
import eutros.multiblocktweaker.crafttweaker.functions.IRecipePredicate;
import eutros.multiblocktweaker.crafttweaker.functions.IRemovalFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCControllerTile;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCPatternMatchContext;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCRecipe;
import eutros.multiblocktweaker.gregtech.recipes.CustomMultiblockRecipeLogic;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileControllerCustom extends RecipeMapMultiblockController {

    public static final String TAG_PERSISTENT = MultiblockTweaker.MOD_ID + ":persistent";
    public final CustomMultiblock multiblock;
    // remove on error
    private IFormStructureFunction formStructureFunction;
    private IDisplayTextFunction displayTextFunction;
    private IAddInformationFunction addInformationFunction;
    private IRemovalFunction removalFunction;
    private IRecipePredicate recipePredicate;
    private IPatternBuilderFunction patternBuilderFunction;

    @Nullable
    public IData persistentData;

    public TileControllerCustom(@Nonnull CustomMultiblock multiblock) {
        super(multiblock.loc, multiblock.recipeMap);
        this.multiblock = multiblock;
        this.recipeMapWorkable = new CustomMultiblockRecipeLogic(this,
                multiblock.update,
                multiblock.updateWorktable,
                multiblock.setupRecipe,
                multiblock.completeRecipe
        );
        displayTextFunction = multiblock.displayTextFunction;
        removalFunction = multiblock.removalFunction;
        recipePredicate = multiblock.recipePredicate;
        formStructureFunction = multiblock.formStructureFunction;
        addInformationFunction = multiblock.addInformationFunction;
        patternBuilderFunction = multiblock.pattern;
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

        if (displayTextFunction == null) return;

        try {
            List<IFormattedText> added = displayTextFunction.addDisplayText(new MCControllerTile(this));
            if (added != null) {
                for (IFormattedText component : added) {
                    textList.add(new TextComponentString(component.getText()));
                }
            }
        } catch (RuntimeException e) {
            logFailure("displayTextFunction", e);
            displayTextFunction = null;
        }

    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.energyContainer = new EnergyContainerList(
                Stream.of(MultiblockAbility.INPUT_ENERGY,
                        MultiblockAbility.OUTPUT_ENERGY)
                        .map(this::getAbilities)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );

        if (formStructureFunction == null) return;

        try {
            formStructureFunction.formStructure(new MCControllerTile(this), new MCPatternMatchContext(context));
        } catch (RuntimeException e) {
            logFailure("formStructureFunction", e);
            formStructureFunction = null;
        }
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        if (recipePredicate == null) return true;

        try {
            return recipePredicate.test(
                    new MCControllerTile(this),
                    new MCRecipe(recipe),
                    consumeIfSuccess
            );
        } catch (RuntimeException e) {
            logFailure("recipePredicate", e);
            recipePredicate = null;
        }
        return true;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (removalFunction == null) return;

        try {
            removalFunction.onRemoval(new MCControllerTile(this));
        } catch (RuntimeException e) {
            logFailure("removalFunction", e);
            removalFunction = null;
        }
    }

    private void logFailure(String func, Throwable t) {
        CraftTweakerAPI.logError(String.format("Couldn't run %s function of %s.", func, multiblock), t);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if  (addInformationFunction != null) {
            try {
                tooltip.addAll(addInformationFunction.addTips(new MCControllerTile(this)));
            } catch (RuntimeException e) {
                logFailure("addInformationFunction", e);
                addInformationFunction = null;
            }
        }
    }

    @Override
    protected BlockPattern createStructurePattern() {
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
        if (multiblock.designs != null && multiblock.designs.size() > 0) {
            return multiblock.designs;
        }
        return super.getMatchingShapes();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart part) {
        return multiblock.baseTexture;
    }

    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return multiblock.frontOverlay == null ? super.getFrontOverlay() : multiblock.frontOverlay;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
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

}
