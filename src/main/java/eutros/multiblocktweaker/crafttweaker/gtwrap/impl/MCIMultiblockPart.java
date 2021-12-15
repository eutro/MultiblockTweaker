package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultiblockPart;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityMultiblockPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MCIMultiblockPart extends MCMetaTileEntity implements IIMultiblockPart {

    private final MetaTileEntityMultiblockPart inner;

    public MCIMultiblockPart(MetaTileEntityMultiblockPart inner) {
        super(inner);
        this.inner = inner;
    }

    @Override
    @NotNull
    public MetaTileEntityMultiblockPart getInternal() {
        return inner;
    }

    @Nullable
    @Override
    public IMultiblockAbility getAbility() {
        return inner instanceof IMultiblockAbilityPart ? new MCMultiblockAbility<>((((IMultiblockAbilityPart<?>) inner).getAbility())) : null;
    }

    @Override
    public boolean canPartShare() {
        return inner.canPartShare();
    }

    @Override
    public boolean isAttachedToMultiBlock() {
        return inner.isAttachedToMultiBlock();
    }

    @Override
    public int getTier() {
        return inner.getTier();
    }

}
