package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import crafttweaker.mc1120.CraftTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIFluidHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIItemHandlerModifiable;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MCMetaTileEntity implements IMetaTileEntity {

    @NotNull
    private final MetaTileEntity inner;

    public MCMetaTileEntity(@NotNull MetaTileEntity inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public MetaTileEntity getInternal() {
        return inner;
    }

    public IWorld getWorld() {
        return CraftTweakerMC.getIWorld(inner.getWorld());
    }

    public IBlockPos getPos() {
        return CraftTweakerMC.getIBlockPos(inner.getPos());
    }

    public long getOffsetTimer() {
        return inner.getOffsetTimer();
    }

    public String getMetaName() {
        return inner.getMetaName();
    }

    public String getMetaFullName() {
        return inner.getMetaFullName();
    }

    public IFacing getFrontFacing() {
        return CraftTweakerMC.getIFacing(inner.getFrontFacing());
    }

    @Override
    public void markDirty() {
        inner.markDirty();
    }

    @Override
    public boolean isFirstTick() {
        return inner.isFirstTick();
    }

    @Override
    public int getInputRedstoneSignal(IFacing side, boolean ignoreCover) {
        return inner.getInputRedstoneSignal(CraftTweakerMC.getFacing(side), ignoreCover);
    }

    @Override
    public boolean isBlockRedstonePowered() {
        return inner.isBlockRedstonePowered();
    }

    @Override
    public int getActualLightValue() {
        return inner.getActualLightValue();
    }

    @Override
    public void update() {
        inner.update();
    }

    @Override
    public IItemStack getStackForm(int amount) {
        return CraftTweakerMC.getIItemStack(inner.getStackForm(amount));
    }

    @Override
    public boolean isOpaqueCube() {
        return inner.isOpaqueCube();
    }

    @Override
    public int getLightOpacity() {
        return inner.getLightOpacity();
    }

    @Override
    public String getHarvestTool() {
        return inner.getHarvestTool();
    }

    @Override
    public int getHarvestLevel() {
        return inner.getHarvestLevel();
    }

    @Override
    public int getOutputRedstoneSignal(IFacing side) {
        return inner.getOutputRedstoneSignal(CraftTweakerMC.getFacing(side));
    }

    @Override
    public void setOutputRedstoneSignal(IFacing side, int strength) {
        inner.setOutputRedstoneSignal(CraftTweakerMC.getFacing(side), strength);
    }

    @Override
    public void notifyBlockUpdate() {
        inner.notifyBlockUpdate();
    }

    @Override
    public void scheduleRenderUpdate() {
        inner.scheduleRenderUpdate();
    }

    @Override
    public void setFrontFacing(IFacing frontFacing) {
        inner.setFrontFacing(CraftTweakerMC.getFacing(frontFacing));
    }

    @Override
    public void setPaintingColor(int paintingColor) {
        inner.setPaintingColor(paintingColor);
    }

    @Override
    public void setFragile(boolean fragile) {
        inner.setFragile(fragile);
    }

    @Override
    public boolean isValidFrontFacing(IFacing facing) {
        return inner.isValidFrontFacing(CraftTweakerMC.getFacing(facing));
    }

    @Override
    public boolean hasFrontFacing() {
        return inner.hasFrontFacing();
    }

    @Override
    public boolean isValid() {
        return inner.isValid();
    }

    @Override
    public int getPaintingColor() {
        return inner.getPaintingColor();
    }

    @Override
    public IIItemHandlerModifiable getImportItems() {
        return new MCIItemHandlerModifiable(inner.getImportItems());
    }

    @Override
    public IIItemHandlerModifiable getExportItems() {
        return new MCIItemHandlerModifiable(inner.getExportItems());
    }

    @Override
    public IIFluidHandler getImportFluids() {
        return new MCIFluidHandler(inner.getImportFluids());
    }

    @Override
    public IIFluidHandler getExportFluids() {
        return new MCIFluidHandler(inner.getExportFluids());
    }

    @Override
    public List<IIItemHandlerModifiable> getNotifiedItemOutputList() {
        return inner.getNotifiedItemOutputList().stream().map(MCIItemHandlerModifiable::new).collect(
                Collectors.toList());
    }

    @Override
    public List<IIItemHandlerModifiable> getNotifiedItemInputList() {
        return inner.getNotifiedItemInputList().stream().map(MCIItemHandlerModifiable::new).collect(
                Collectors.toList());
    }

    @Override
    public List<IIFluidHandler> getNotifiedFluidInputList() {
        return inner.getNotifiedFluidInputList().stream().map(MCIFluidHandler::new).collect(
                Collectors.toList());
    }

    @Override
    public List<IIFluidHandler> getNotifiedFluidOutputList() {
        return inner.getNotifiedFluidOutputList().stream().map(MCIFluidHandler::new).collect(
                Collectors.toList());
    }

    @Override
    public boolean isFragile() {
        return inner.isFragile();
    }

    @Override
    public boolean shouldDropWhenDestroyed() {
        return inner.shouldDropWhenDestroyed();
    }

    @Override
    public void toggleMuffled() {
        inner.toggleMuffled();
    }

    @Override
    public boolean isMuffled() {
        return inner.isMuffled();
    }

}
