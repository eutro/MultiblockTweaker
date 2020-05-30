package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.api.recipes.Recipe;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class MultiblockTweakerJEIPlugin implements IModPlugin {

    public static IJeiRuntime runtime;

    @Override
    public void register(@NotNull IModRegistry registry) {
        List<CustomInfoRecipeWrapper> recipeList = new ArrayList<>();
        for(CustomMultiblock customMultiblock : MultiblockRegistry.getAll()) {
            if(!customMultiblock.designs.isEmpty()) {
                recipeList.add(new CustomInfoRecipeWrapper(new CustomInfoPage(customMultiblock)));
            }
        }

        registry.addRecipes(recipeList, "gregtech:multiblock_info");

        for(CustomMultiblock multiblock : MultiblockRegistry.getAll()) {
            List<CustomRecipeWrapper> recipes = new ArrayList<>();
            for(Recipe recipe : multiblock.recipeMap.getRecipeList()) { // override all the default GT recipes
                if(recipe.hasValidInputsForDisplay()) {
                    recipes.add(new CustomRecipeWrapper(multiblock.recipeMap, recipe));
                }
            }
            registry.addRecipes(recipes, "gregtech:" + multiblock.recipeMap.getUnlocalizedName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;

        IRecipeRegistry rr = runtime.getRecipeRegistry();

        for(CustomMultiblock multiblock : MultiblockRegistry.getAll()) {
            String uid = "gregtech:" + multiblock.recipeMap.getUnlocalizedName();
            IRecipeCategory<IRecipeWrapper> category = rr.getRecipeCategory(uid);
            if(category != null) {
                for(IRecipeWrapper recipe : rr.getRecipeWrappers(category)) { // hide all of GT's recipes for our multiblocks
                    if(!(recipe instanceof CustomRecipeWrapper)) {
                        rr.hideRecipe(recipe, uid);
                    }
                }
            }
        }
    }

}
