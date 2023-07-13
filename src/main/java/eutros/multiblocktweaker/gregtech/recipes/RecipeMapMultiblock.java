package eutros.multiblocktweaker.gregtech.recipes;

import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.ModularUI;
import gregtech.api.recipes.RecipeMap;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.DoubleSupplier;

public class RecipeMapMultiblock extends RecipeMap<CustomRecipeBuilder> {

    public RecipeMapMultiblock(String unlocalizedName, int maxInputs, int maxOutputs, int maxFluidInputs, int maxFluidOutputs, CustomRecipeBuilder defaultRecipe, boolean isHidden) {
        super(unlocalizedName, maxInputs, maxOutputs, maxFluidInputs, maxFluidOutputs, defaultRecipe, isHidden);
    }

    @Override
    public ModularUI.Builder createJeiUITemplate(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids, int yOffset) {
        return super.createJeiUITemplate(importItems, exportItems, importFluids, exportFluids, yOffset);
    }

    @Override
    public ModularUI.Builder createUITemplate(DoubleSupplier progressSupplier, IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, FluidTankList importFluids, FluidTankList exportFluids, int yOffset) {
        return super.createUITemplate(progressSupplier, importItems, exportItems, importFluids, exportFluids, yOffset);
    }

}
