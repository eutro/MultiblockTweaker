package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.multiblock.PatternMatchContext;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

@ZenClass("mods.gregtech.multiblock.IPatternMatchContext")
@ZenRegister
public interface IPatternMatchContext {

    @NotNull
    PatternMatchContext getInternal();

    @ZenMethod
    void reset();

    @ZenMethod
    void set(String key, Object value);

    @ZenMethod
    int getInt(String key);

    @ZenMethod
    void increment(String key, int value);

    @ZenMethod
    <T> T getOrDefault(String key, T defaultValue);

    @ZenMethod
    <T> T get(String key);

    @ZenMethod
    <T> T getOrCreate(String key, Supplier<T> creator);

    @ZenMethod
    <T> T getOrPut(String key, T initialValue);

}
