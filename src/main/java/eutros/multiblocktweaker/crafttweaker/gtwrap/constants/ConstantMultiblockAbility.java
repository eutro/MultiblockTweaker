package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMultiblockAbility;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockAbility;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.gregtech.multiblock.MultiblockAbility")
@ZenRegister
public class ConstantMultiblockAbility {
    Map<String, IMultiblockAbility> cache = new HashMap<>();

    @ZenMethod
    @ZenMemberGetter()
    IMultiblockAbility get(String member) {
        if (!cache.containsKey(member)) {
            MultiblockAbility<?> ability = ReflectionHelper.getStatic(MultiblockAbility.class, member);
            cache.put(member, ability == null ? null : new MCMultiblockAbility<>(ability));
        }
        return cache.get(member);
    }
}
