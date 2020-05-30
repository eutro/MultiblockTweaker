package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@FunctionalInterface
@ZenClass("mods.gregtech.recipe.functions.ICompleteRecipeFunction")
@ZenRegister
public interface ICompleteRecipeFunction {

    @ZenMethod
    void run(IRecipeLogic logic);

}
