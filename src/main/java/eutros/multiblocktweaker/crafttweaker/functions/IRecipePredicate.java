package eutros.multiblocktweaker.crafttweaker.functions;


import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.IRecipePredicate")
@ZenRegister
public interface IRecipePredicate
{

	@ZenMethod
	boolean test( IRecipeLogic logic, IRecipe recipe );

}

