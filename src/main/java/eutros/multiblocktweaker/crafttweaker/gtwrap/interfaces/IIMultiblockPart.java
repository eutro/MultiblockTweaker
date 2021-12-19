package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityMultiblockPart;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * A IMultiblockPart block.
 *
 * @zenClass mods.gregtech.multiblock.IIMultiblockPart
 * @see gregtech.api.metatileentity.multiblock.IMultiblockPart
 */
@ZenClass("mods.gregtech.multiblock.IIMultiblockPart")
@ZenRegister
public interface IIMultiblockPart extends IMetaTileEntity{

    @NotNull
    MetaTileEntityMultiblockPart getInternal();

    /**
     * Ability of this part.
     * @return if it's not a part with ability return null, else return it's ability.
     */
    @ZenMethod
    @Nullable
    IMultiblockAbility getAbility();

    /**
     *
     * @return Can part be shared between different multi-blocks.
     */
    @ZenMethod
    boolean canPartShare();


    /**
     *
     * @return  Is the part attached to the multi-block.
     */
    @ZenMethod
    boolean isAttachedToMultiBlock();


    /**
     *
     * @return  tier of this part.
     */
    @ZenMethod
    @ZenGetter("tier")
    int getTier();
}
