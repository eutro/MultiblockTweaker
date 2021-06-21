package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;

import java.util.List;

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
}
