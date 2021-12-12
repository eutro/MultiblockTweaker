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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.MetaTileEntities")
@ZenRegister
public class ConstantMetaTileEntities {
    private final static IMetaTileEntity[] EMPTY = new IMetaTileEntity[0];
    private final static Map<String, IMetaTileEntity[]> cache = new HashMap<>();

    @ZenMethod
    @ZenMemberGetter()
    public static IMetaTileEntity[] get(String member) {
        if (!cache.containsKey(member)) {
            Object mte = ReflectionHelper.getStatic(MetaTileEntities.class, member);
            if (mte instanceof MetaTileEntity) {
                cache.put(member, new IMetaTileEntity[]{new MCMetaTileEntity((MetaTileEntity)mte)});
            } else if (mte instanceof MetaTileEntity[]){
                cache.put(member, Arrays.stream((MetaTileEntity[]) mte).map(MCMetaTileEntity::new).toArray(IMetaTileEntity[]::new));
            }
            cache.put(member, EMPTY);
        }
        return cache.get(member);
    }

}
