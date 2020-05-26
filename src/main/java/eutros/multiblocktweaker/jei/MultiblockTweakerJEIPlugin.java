package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

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
    }

}
