package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MCMultiblockAbility<T> implements IMultiblockAbility {

    private final MultiblockAbility<T> inner;

    public MCMultiblockAbility(MultiblockAbility<T> inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public MultiblockAbility<T> getInternal() {
        return inner;
    }

    @Override
    public List<IMetaTileEntity> getMetaTileEntities() {
        return MultiblockAbility.REGISTRY.get(inner).stream().map(MCMetaTileEntity::new).collect(Collectors.toList());
    }

}
