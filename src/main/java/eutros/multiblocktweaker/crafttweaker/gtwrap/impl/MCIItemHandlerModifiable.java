package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIItemHandlerModifiable;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class MCIItemHandlerModifiable implements IIItemHandlerModifiable {

    private final IItemHandlerModifiable inner;

    public MCIItemHandlerModifiable(IItemHandlerModifiable inner) {
        this.inner = inner;
    }

    @Override
    public void setStackInSlot(int i, IItemStack itemStack) {
        inner.setStackInSlot(i, CraftTweakerMC.getItemStack(itemStack));
    }

    @Override
    public int getSlots() {
        return inner.getSlots();
    }

    @Override
    @Nonnull
    public IItemStack getStackInSlot(int i) {
        return CraftTweakerMC.getIItemStack(inner.getStackInSlot(i));
    }

    @Override
    @Nonnull
    public IItemStack insertItem(int i, IItemStack itemStack, boolean b) {
        return CraftTweakerMC.getIItemStack(inner.insertItem(i, CraftTweakerMC.getItemStack(itemStack), b));
    }

    @Override
    @Nonnull
    public IItemStack extractItem(int i, int i1, boolean b) {
        return CraftTweakerMC.getIItemStack(inner.extractItem(i, i1, b));
    }

    @Override
    public int getSlotLimit(int i) {
        return inner.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int slot, IItemStack stack) {
        return inner.isItemValid(slot, CraftTweakerMC.getItemStack(stack));
    }

}
