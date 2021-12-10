package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IPatternMatchContext;
import gregtech.api.pattern.PatternMatchContext;
import org.jetbrains.annotations.NotNull;

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
    public void set(String key, String value) {
        inner.set(key, value);
    }

    @Override
    public void setInt(String key, int value) {
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
    public String getOrDefault(String key, String defaultValue) {
        return inner.getOrDefault(key, defaultValue);
    }

    @Override
    public String get(String key) {
        return inner.get(key);
    }

    @Override
    public String getOrPut(String key, String initialValue) {
        return inner.getOrPut(key, initialValue);
    }

}
