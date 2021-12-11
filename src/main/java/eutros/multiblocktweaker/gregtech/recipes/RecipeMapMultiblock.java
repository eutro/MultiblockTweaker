package eutros.multiblocktweaker.gregtech.recipes;

import eutros.multiblocktweaker.helper.MathHelper;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.*;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.DoubleSupplier;

public class RecipeMapMultiblock extends RecipeMap<CustomRecipeBuilder> {

    public static final int SLOT_SIZE = 18;
    public static final int GRID_WIDTH = 3;
    public static final int LINE_DIFF = 10;
    private final int slotsHeight;

    public RecipeMapMultiblock(String unlocalizedName, int minInputs, int maxInputs, int minOutputs, int maxOutputs, int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs, CustomRecipeBuilder defaultRecipe, boolean isHidden) {
        super(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden);

        slotsHeight = MathHelper.max(
                64,
                getSlotsHeight(maxInputs, maxFluidInputs),
                getSlotsHeight(maxOutputs, maxFluidOutputs)
        );
    }

    private static int getSlotsHeight(int items, int fluids) {
        int[] grid = determineSlotsGrid(items);
        return 10 + SLOT_SIZE * (grid[1] + (fluids > 0 ?
                (grid[1] >= fluids && grid[0] < 3 ?
                        fluids - 1 :
                        (int) Math.ceil(fluids / (double) GRID_WIDTH)) :
                0));
    }

    @Nonnull
    public ModularUI.Builder createUITemplate(DoubleSupplier progressSupplier, IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND,
                176,
                (slotsHeight + (getRecipeList().parallelStream()
                                        .map(Recipe::getRecipePropertyStorage)
                                        .map(RecipePropertyStorage::getRecipePropertyKeys)
                                        .mapToInt(Set::size)
                                        .max()
                                        .orElse(0) + 4) * LINE_DIFF) * 3 / 2); // For some godforsaken reason the JEI category multiplies by 2/3
        builder.widget(new ProgressWidget(progressSupplier, 77, slotsHeight / 2 - 10, 20, 20, progressBarTexture, moveType));
        addInventorySlotGroup(builder, importItems, importFluids, false);
        addInventorySlotGroup(builder, exportItems, exportFluids, true);
        return builder;
    }

    // what is even going on here
    @SuppressWarnings("ConstantConditions")
    protected void addInventorySlotGroup(ModularUI.Builder builder, IItemHandlerModifiable itemHandler, FluidTankList fluidHandler, boolean isOutputs) {
        int itemInputsCount = itemHandler.getSlots();
        int fluidInputsCount = fluidHandler.getTanks();
        boolean invertFluids = false;
        if (itemInputsCount == 0) {
            int tmp = itemInputsCount;
            itemInputsCount = fluidInputsCount;
            fluidInputsCount = tmp;
            invertFluids = true;
        }
        int[] inputSlotGrid = determineSlotsGrid(itemInputsCount);
        int itemSlotsToLeft = inputSlotGrid[0];
        int itemSlotsToDown = inputSlotGrid[1];
        int startInputsX = isOutputs ? 106 : 69 - itemSlotsToLeft * 18;
        //             replace hard-coded 32...
        int startInputsY = slotsHeight / 2 - (int) (itemSlotsToDown / 2.0 * SLOT_SIZE);
        for (int i = 0; i < itemSlotsToDown; i++) {
            for (int j = 0; j < itemSlotsToLeft; j++) {
                int slotIndex = i * itemSlotsToLeft + j;
                int x = startInputsX + 18 * j;
                int y = startInputsY + 18 * i;
                addSlot(builder, x, y, slotIndex, itemHandler, fluidHandler, invertFluids, isOutputs);
            }
        }
        if (fluidInputsCount > 0 || invertFluids) {
            if (itemSlotsToDown >= fluidInputsCount && itemSlotsToLeft < 3) {
                int startSpecX = isOutputs ? startInputsX + itemSlotsToLeft * 18 : startInputsX - 18;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int y = startInputsY + 18 * i;
                    addSlot(builder, startSpecX, y, i, itemHandler, fluidHandler, !invertFluids, isOutputs);
                }
            } else {
                int startSpecY = startInputsY + itemSlotsToDown * 18;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int x = isOutputs ?
                            startInputsX + 18 * (i % 3) :
                            startInputsX + itemSlotsToLeft * 18 - 18 - 18 * (i % 3);
                    int y = startSpecY + (i / 3) * 18;
                    addSlot(builder, x, y, i, itemHandler, fluidHandler, !invertFluids, isOutputs);
                }
            }
        }
    }

}
