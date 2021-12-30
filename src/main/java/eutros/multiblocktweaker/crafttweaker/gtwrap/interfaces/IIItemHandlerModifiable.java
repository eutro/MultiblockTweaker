package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gregtech.api.util.GTHashMaps;
import gregtech.api.util.ItemStackKey;
import gregtech.api.util.OverlayedItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used for interacting with inventories.
 *
 * @zenClass mods.forge.items.IItemHandlerModifiable
 */
@ZenClass("mods.forge.items.IItemHandlerModifiable")
@IterableSimple("crafttweaker.item.IItemStack")
@ZenRegister
public interface IIItemHandlerModifiable extends Iterable<IItemStack> {

    IItemHandlerModifiable getInner();
    
    /**
     * Set the item stack in a slot.
     *
     * @param slot      The slot to set.
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
     * @param slot      The slot to insert into.
     * @param itemStack The item stack to insert.
     * @param simulate  Whether the insertion should be only simulated, or actually performed.
     * @return The remaining item stack.
     */
    @ZenMethod
    IItemStack insertItem(int slot, IItemStack itemStack, boolean simulate);

    /**
     * Try to extract from an item stack in a specific slot.
     *
     * @param slot     The slot to extract from.
     * @param amount   How much to extract.
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
     * @param slot  The slot to check.
     * @param stack The stack to check.
     * @return Whether the slot is valid for the stack.
     */
    @ZenMethod
    boolean isItemValid(int slot, IItemStack stack);

    /**
     * Simulates the insertion of items into a target inventory, then optionally performs the insertion.
     * <p>
     * Simulating will not modify any of the input parameters. Insertion will either succeed completely, or fail
     * without modifying anything.
     * This method should be called with {@code simulate} {@code true} first, then {@code simulate} {@code false},
     * only if it returned {@code true}.
     *
     * @param simulate whether to simulate ({@code true}) or actually perform the insertion ({@code false})
     * @param itemStacks    the items to insert into {@code handler}.
     * @return {@code true} if the insertion succeeded, {@code false} otherwise.
     */
    @ZenMethod
    default boolean addItems(boolean simulate, List<IItemStack> itemStacks){
        IItemHandlerModifiable handler = this.getInner();
        List<ItemStack> items = itemStacks.stream().map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
        if (simulate) {
            OverlayedItemHandler overlayedItemHandler = new OverlayedItemHandler(handler);
            HashMap<ItemStackKey, Integer> stackKeyMap = GTHashMaps.fromItemStackCollection(items);

            for (Map.Entry<ItemStackKey, Integer> entry : stackKeyMap.entrySet()) {
                int amountToInsert = entry.getValue();
                int amount = overlayedItemHandler.insertStackedItemStackKey(entry.getKey(), amountToInsert);
                if (amount > 0) {
                    return false;
                }
            }
            return true;
        }

        // perform the merge.
        items.forEach(stack -> ItemHandlerHelper
                .insertItemStacked(handler, stack, false));
        return true;
    }
}
