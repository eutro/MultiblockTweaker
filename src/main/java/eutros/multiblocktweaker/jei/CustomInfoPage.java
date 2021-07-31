package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Map;

public class CustomInfoPage extends MultiblockInfoPage {

    private final CustomMultiblock multiblock;

    public CustomInfoPage(CustomMultiblock multiblock) {
        this.multiblock = multiblock;
    }

    @Override
    public MultiblockControllerBase getController() {
        return new TileControllerCustom(multiblock);
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        return multiblock.designs;
    }

    @Override
    public String[] getDescription() {
        String s = multiblock.loc.getResourceDomain() + ".multiblock." + multiblock.loc.getResourcePath() + ".description";
        return new String[] { I18n.format(s) };
    }

    @Override
    public float getDefaultZoom() {
        return multiblock.zoom;
    }

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();

        Map<ItemStack, List<ITextComponent>> tooltipMap = getBlockTooltipMap();

        //Clear existing tooltips if desired
        if(multiblock.clearTooltips) {
            tooltipMap.clear();
        }

        Map<ItemStack, List<ITextComponent>> additionalTooltips = multiblock.tooltipMap;
        for(Map.Entry<ItemStack, List<ITextComponent>> tooltip : additionalTooltips.entrySet()) {
            for(ITextComponent component : tooltip.getValue()) {
                addBlockTooltip(tooltip.getKey(), component);
            }
        }
    }
}
