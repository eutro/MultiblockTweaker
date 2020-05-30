package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.multiblock.PatternMatchContext;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.multiblock.IPatternMatchContext")
@ZenRegister
public interface IPatternMatchContext {

    @NotNull
    PatternMatchContext getInternal();

    @ZenMethod
    void reset();

    @ZenMethod
    void set(String key, String value);

    @ZenMethod
    int getInt(String key);

    @ZenMethod
    void increment(String key, int value);

    @ZenMethod
    String getOrDefault(String key, String defaultValue);

    @ZenMethod
    String get(String key);

    @ZenMethod
    String getOrPut(String key, String initialValue);

}
