package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Used for interacting with inventories.
 */
@ZenClass("mods.forge.items.IItemHandlerModifiable")
@ZenRegister
public interface IIItemHandlerModifiable {

    /**
     * Set the item stack in a slot.
     *
     * @param slot The slot to set.
     * @param itemStack The stack to set it to.
     */
    @ZenMethod
    void setStackInSlot(int slot, IItemStack itemStack);

    /**
     * @return How many slots this has.
     */
    @ZenMethod
    int getSlots();

    /**
     * Get the item stack in a slot.
     *
     * @param slot The slot to get.
     * @return The item stack in it.
     */
    @ZenMethod
    IItemStack getStackInSlot(int slot);

    /**
     * Try to insert an item stack in a specific slot.
     *
     * @param slot The slot to insert into.
     * @param itemStack The item stack to insert.
     * @param simulate Whether the insertion should be only simulated, or actually performed.
     * @return The remaining item stack.
     */
    @ZenMethod
    IItemStack insertItem(int slot, IItemStack itemStack, boolean simulate);

    /**
     * Try to extract from an item stack in a specific slot.
     *
     * @param slot The slot to extract from.
     * @param amount How much to extract.
     * @param simulate Whether the extraction should be only simulated, or actually performed.
     * @return The item stack that was extracted.
     */
    @ZenMethod
    IItemStack extractItem(int slot, int amount, boolean simulate);

    /**
     * @param slot The slot to check.
     * @return How many items can fit into the slot at most.
     */
    @ZenMethod
    int getSlotLimit(int slot);

    /**
     * Check whether an item stack could potentially be inserted into a slot.
     *
     * @param slot The slot to check.
     * @param stack The stack to check.
     * @return Whether the slot is valid for the stack.
     */
    @ZenMethod
    boolean isItemValid(int slot, IItemStack stack);

}
