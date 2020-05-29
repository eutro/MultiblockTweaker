package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.api.recipes.Recipe;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import java.util.List;
import java.util.stream.Collectors;

@JEIPlugin
public class MultiblockTweakerJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(MultiblockRegistry.getAll().stream()
                        .filter(m -> !m.designs.isEmpty())
                        .map(CustomInfoPage::new)
                        .map(MultiblockInfoRecipeWrapper::new)
                        .collect(Collectors.toList()),
                "gregtech:multiblock_info");

        MultiblockRegistry.getAll().stream() // TODO hide old recipes from JEI only
                .map(c -> c.recipeMap)
                .forEach(m -> {
                            List<CustomRecipeWrapper> recipes = m.getRecipeList().stream()
                                    .filter(Recipe::hasValidInputsForDisplay)
                                    .map(recipe -> new CustomRecipeWrapper(m, recipe))
                                    .collect(Collectors.toList());
                            registry.addRecipes(recipes, "gregtech:" + m.getUnlocalizedName());
                        });
    }

}
