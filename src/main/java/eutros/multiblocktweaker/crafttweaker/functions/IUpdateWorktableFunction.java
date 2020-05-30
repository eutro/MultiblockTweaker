package eutros.multiblocktweaker.crafttweaker.functions;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipeLogic;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@FunctionalInterface
@ZenClass("mods.gregtech.recipe.function.IUpdateWorktableFunction")
@ZenRegister
public interface IUpdateWorktableFunction {

    @ZenMethod
    boolean run(IRecipeLogic logic);

}
