package eutros.multiblocktweaker.crafttweaker.expanders;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCRecipe;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IRecipe;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.recipes.crafttweaker.CTRecipe;
import stanhebben.zenscript.annotations.ZenCaster;
import stanhebben.zenscript.annotations.ZenExpansion;

@ZenRegister
@ZenExpansion("mods.gregtech.recipe.Recipe")
public class ExpandRecipe {

    @ZenCaster
    public static IRecipe asIRecipe(CTRecipe recipe) {
        return new MCRecipe(ReflectionHelper.getPrivate(CTRecipe.class, "backingRecipe", recipe));
    }

}
