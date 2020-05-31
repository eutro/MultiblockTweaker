package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.recipe.IRecipe")
@ZenRegister
public interface IRecipe {

    @ZenMethod
    boolean matches(boolean consumeIfSuccessful, IItemStack[] inputs, ILiquidStack[] fluidInputs);

    @ZenMethod
    IIngredient[] getInputs();

    @ZenMethod
    IItemStack[] getOutputs();

    @ZenMethod
    ILiquidStack[] getFluidInputs();

    @ZenMethod
    boolean hasInputFluid(ILiquidStack fluid);

    @ZenMethod
    ILiquidStack[] getFluidOutputs();

    @ZenMethod
    int getDuration();

    @ZenMethod
    int getEUt();

    @ZenMethod
    boolean isHidden();

    @ZenMethod
    boolean hasValidInputsForDisplay();

    @ZenMethod
    String[] getPropertyKeys();

    @ZenMethod
    boolean getBooleanProperty(String key);

    @ZenMethod
    int getIntegerProperty(String key);

    @ZenMethod
    String getProperty(String key);

}
