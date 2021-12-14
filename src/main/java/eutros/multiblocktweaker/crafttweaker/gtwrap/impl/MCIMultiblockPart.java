package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IIMultiblockPart;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MCIMultiblockPart implements IIMultiblockPart {

    private final IMultiblockPart inner;

    public MCIMultiblockPart(IMultiblockPart inner) {
        this.inner = inner;
    }

    @Override
    @NotNull
    public IMultiblockPart getInternal() {
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

}
