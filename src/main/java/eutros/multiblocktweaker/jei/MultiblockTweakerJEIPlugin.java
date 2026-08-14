package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.MultiblockRegistry;
import gregtech.integration.jei.multiblock.MultiblockInfoCategory;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@JEIPlugin
public class MultiblockTweakerJEIPlugin implements IModPlugin {

    public static IJeiRuntime runtime;

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        int[] ids = MultiblockRegistry.getIDs();
        Arrays.sort(ids);
        for(int id : ids) {
            CustomMultiblock customMultiblock = MultiblockRegistry.get(id);
            if (customMultiblock != null && !customMultiblock.designs.isEmpty()) {
                MultiblockInfoRecipeWrapper wrapper = new MultiblockInfoRecipeWrapper(new CustomInfoPage(customMultiblock));
                MultiblockInfoCategory.multiblockRecipes.put(customMultiblock.getLocation(), wrapper);
            }
        }
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

}
