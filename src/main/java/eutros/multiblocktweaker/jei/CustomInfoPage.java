package eutros.multiblocktweaker.jei;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.tile.TileControllerCustom;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
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
        List<String> ret = new ArrayList<>();
        String s = "multiblock." + multiblock.loc.getNamespace() + '.' + multiblock.loc.getPath() + ".desc";
        ret.add(I18n.format(s));

        for(int i = 0; !I18n.format(s + i).equals(s + i); i++) {
            ret.add(I18n.format(s));
        }

        return ret.toArray(new String[0]);
    }

}
