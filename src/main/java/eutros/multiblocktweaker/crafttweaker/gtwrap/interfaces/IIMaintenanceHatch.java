package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.capability.IMaintenanceHatch;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A IMultiblockPart block.
 *
 * @zenClass mods.gregtech.IMetaTileEntity
 * @see IMultiblockPart
 */
@ZenClass("mods.gregtech.multiblock.IIMaintenanceHatchI")
@ZenRegister
public interface IIMaintenanceHatch {

    /**
     * @return true if this is a Full-Auto Maintenance Hatch, false otherwise.
     */
    @ZenMethod
    @ZenGetter("isFullAuto")
    boolean isFullAuto();

    /**
     * Sets this Maintenance Hatch as being duct taped
     * @param isTaped is the state of the hatch being taped or not
     */
    @ZenMethod
    @ZenSetter("setTaped")
    void setTaped(boolean isTaped);

    /**
     * Stores maintenance data to this MetaTileEntity
     * @param maintenanceProblems is the byte value representing the problems
     * @param timeActive is the int value representing the total time the parent multiblock has been active
     */
    @ZenMethod
    void storeMaintenanceData(byte maintenanceProblems, int timeActive);

    /**
     *
     * @return whether this maintenance hatch has maintenance data
     */
    @ZenMethod
    @ZenGetter("hasMaintenanceData")
    boolean hasMaintenanceData();

    /**
     * reads this MetaTileEntity's maintenance data
     * @return Tuple of Byte, Integer corresponding to the maintenance problems, and total time active
     */
    @ZenMethod
    int[] readMaintenanceData();

    @ZenMethod
    @ZenGetter("durationMultiplier")
    double getDurationMultiplier();

    @ZenMethod
    @ZenGetter("timeMultiplier")
    double getTimeMultiplier();

    @ZenMethod
    @ZenGetter("startWithoutProblems")
    boolean startWithoutProblems();
}
