package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.multiblock.PatternMatchContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MCPatternMatchContext implements IPatternMatchContext {

    @NotNull
    private final PatternMatchContext inner;

    public MCPatternMatchContext(@NotNull PatternMatchContext inner) {
        this.inner = inner;
    }

    @NotNull
    @Override
    public PatternMatchContext getInternal() {
        return inner;
    }

    @Override
    public void reset() {
        inner.reset();
    }

    @Override
    public void set(String key, Object value) {
        inner.set(key, value);
    }

    @Override
    public int getInt(String key) {
        return inner.getInt(key);
    }

    @Override
    public void increment(String key, int value) {
        inner.increment(key, value);
    }

    @Override
    public <T> T getOrDefault(String key, T defaultValue) {
        return inner.getOrDefault(key, defaultValue);
    }

    @Override
    public <T> T get(String key) {
        return inner.get(key);
    }

    @Override
    public <T> T getOrCreate(String key, Supplier<T> creator) {
        return inner.getOrCreate(key, creator);
    }

    @Override
    public <T> T getOrPut(String key, T initialValue) {
        return inner.getOrPut(key, initialValue);
    }

}
