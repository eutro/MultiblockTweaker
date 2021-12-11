package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.MetaTileEntities")
@ZenRegister
public class ConstantMetaTileEntities {
    Map<String, IMetaTileEntity> cache = new HashMap<>();

    @ZenMethod
    @ZenMemberGetter()
    IMetaTileEntity get(String member) {
        if (!cache.containsKey(member)) {
            MetaTileEntity mte = ReflectionHelper
                    .getStatic(MetaTileEntities.class, member);
            cache.put(member, mte == null ? null : new MCMetaTileEntity(mte));
        }
        return cache.get(member);
    }
}
