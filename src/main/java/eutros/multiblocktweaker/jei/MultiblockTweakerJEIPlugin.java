package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class MultiblockTweakerJEIPlugin implements IModPlugin {

    public static IJeiRuntime runtime;

    @Override
    public void register(@NotNull IModRegistry registry) {
        List<MultiblockInfoRecipeWrapper> recipeList = new ArrayList<>();
        for(CustomMultiblock customMultiblock : MultiblockRegistry.getAll()) {
            if(!customMultiblock.designs.isEmpty()) {
                recipeList.add(new MultiblockInfoRecipeWrapper(new CustomInfoPage(customMultiblock)));
            }
        }

        registry.addRecipes(recipeList, "gregtech:multiblock_info");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

}
