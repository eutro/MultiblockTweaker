package eutros.multiblocktweaker.gregtech.recipes;

import eutros.multiblocktweaker.helper.MathHelper;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

public class RecipeMapMultiblock extends RecipeMap<SimpleRecipeBuilder> {

    public static final int DEFAULT_HEIGHT = 166;
    public static final int BASE_UI_HEIGHT = 144;
    public static final int SLOT_SIZE = 18;
    public static final int GRID_WIDTH = 3;
    public static final int BASE_SLOT_Y_BOTTOM = 134;
    private final int UI_HEIGHT;

    public RecipeMapMultiblock(String unlocalizedName, int minInputs, int maxInputs, int minOutputs, int maxOutputs, int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs, SimpleRecipeBuilder defaultRecipe) {
        super(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe);
        UI_HEIGHT = MathHelper.max(
                DEFAULT_HEIGHT,
                BASE_UI_HEIGHT + (maxFluidInputs / GRID_WIDTH + maxOutputs / GRID_WIDTH) * SLOT_SIZE / 2,
                BASE_UI_HEIGHT + (maxFluidInputs / GRID_WIDTH + maxFluidOutputs / GRID_WIDTH) * SLOT_SIZE / 2
        );
    }

    @Override
    public ModularUI.@NotNull Builder createUITemplate(DoubleSupplier progressSupplier, IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176, UI_HEIGHT);
        builder.widget(new ProgressWidget(progressSupplier, 77, UI_HEIGHT - BASE_UI_HEIGHT, 20, 20, this.progressBarTexture, this.moveType));
        this.addInventorySlotGroup(builder, importItems, importFluids, false);
        this.addInventorySlotGroup(builder, exportItems, exportFluids, true);
        return builder;
    }

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
        int startInputsX = isOutputs ? 106 : 69 - itemSlotsToLeft * SLOT_SIZE;
        int startInputsY = UI_HEIGHT - BASE_SLOT_Y_BOTTOM - (int) (itemSlotsToDown / 2.0 * SLOT_SIZE);
        for (int i = 0; i < itemSlotsToDown; i++) {
            for (int j = 0; j < itemSlotsToLeft; j++) {
                int slotIndex = i * itemSlotsToLeft + j;
                int x = startInputsX + SLOT_SIZE * j;
                int y = startInputsY + SLOT_SIZE * i;
                addSlot(builder, x, y, slotIndex, itemHandler, fluidHandler, invertFluids, isOutputs);
            }
        }
        if (fluidInputsCount > 0 || invertFluids) {
            if (itemSlotsToDown >= fluidInputsCount && itemSlotsToLeft < GRID_WIDTH) {
                int startSpecX = isOutputs ? startInputsX + itemSlotsToLeft * SLOT_SIZE : startInputsX - SLOT_SIZE;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int y = startInputsY + SLOT_SIZE * i;
                    addSlot(builder, startSpecX, y, i, itemHandler, fluidHandler, true, isOutputs);
                }
            } else {
                int startSpecY = startInputsY + itemSlotsToDown * SLOT_SIZE;
                for (int i = 0; i < fluidInputsCount; i++) {
                    int x = isOutputs ? startInputsX + SLOT_SIZE * (i % GRID_WIDTH) : startInputsX + (itemSlotsToLeft - (i % GRID_WIDTH) - 1) * SLOT_SIZE;
                    int y = startSpecY + (i / GRID_WIDTH) * SLOT_SIZE;
                    addSlot(builder, x, y, i, itemHandler, fluidHandler, true, isOutputs);
                }
            }
        }
    }

}
