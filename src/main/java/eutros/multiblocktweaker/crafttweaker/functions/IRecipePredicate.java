package eutros.multiblocktweaker.crafttweaker.functions;


import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IRecipePredicate")
@ZenRegister
public interface IRecipePredicate {

    @ZenMethod
    boolean test(IMetaTileEntity controller, IRecipe recipe, boolean consumeIfSuccess);

}

