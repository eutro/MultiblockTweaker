package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.crafttweaker.gtwrap.constants.ConstantMetaTileEntities;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * A IMultiblockPart block.
 *
 * @zenClass mods.gregtech.IMetaTileEntity
 * @see gregtech.api.metatileentity.multiblock.IMultiblockPart
 */
@ZenClass("mods.gregtech.multiblock.IIMultiblockPart")
@ZenRegister
public interface IIMultiblockPart {

    @NotNull
    IMultiblockPart getInternal();

    /**
     * Ability of this part.
     * @return if it's not a part with ability return null, else return it's ability.
     */
    @ZenMethod
    @Nullable
    IMultiblockAbility getAbility();

    /**
     * Can part be shared between different multi-blocks.
     */
    @ZenMethod
    boolean canPartShare();


    /**
     * Is the part attached to the multi-block.
     */
    @ZenMethod
    boolean isAttachedToMultiBlock();
}
