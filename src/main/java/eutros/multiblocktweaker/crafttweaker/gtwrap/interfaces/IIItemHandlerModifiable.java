package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.forge.items.IItemHandlerModifiable")
@ZenRegister
public interface IIItemHandlerModifiable {

    @ZenMethod
    void setStackInSlot(int i, IItemStack itemStack);

    @ZenMethod
    int getSlots();

    @ZenMethod
    IItemStack getStackInSlot(int i);

    @ZenMethod
    IItemStack insertItem(int i, IItemStack itemStack, boolean b);

    @ZenMethod
    IItemStack extractItem(int i, int i1, boolean b);

    @ZenMethod
    int getSlotLimit(int i);

    @ZenMethod
    boolean isItemValid(int slot, IItemStack stack);

}
