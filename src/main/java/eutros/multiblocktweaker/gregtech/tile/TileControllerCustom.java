package eutros.multiblocktweaker.gregtech.tile;

import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.render.ICubeRenderer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TileControllerCustom extends RecipeMapMultiblockController {

    private final CustomMultiblock multiblock;

    public TileControllerCustom(@Nonnull CustomMultiblock multiblock) {
        super(multiblock.loc, multiblock.recipeMap);
        this.multiblock = multiblock;
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return Objects.requireNonNull(MultiblockRegistry.get(metaTileEntityId)).pattern;
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart part) {
        return multiblock.texture;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new TileControllerCustom(multiblock);
    }

}
