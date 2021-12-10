package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.pattern.PatternMatchContext;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * The context of a match. Can be obtained from {@link IBlockWorldState#getMatchContext()} or {@link IBlockWorldState#getLayerContext()}.
 *
 * @zenClass mods.gregtech.multiblock.IPatternMatchContext
 */
@ZenClass("mods.gregtech.multiblock.IPatternMatchContext")
@ZenRegister
public interface IPatternMatchContext {

    @NotNull
    PatternMatchContext getInternal();

    /**
     * Reset the context to its default state.
     */
    @ZenMethod
    void reset();

    /**
     * Set a key's value.
     *
     * @param key   The key whose value should be set.
     * @param value The value to set it to.
     */
    @ZenMethod
    void set(String key, String value);

    /**
     * Set a key's value.
     *
     * @param key   The key whose value should be set.
     * @param value The value to set it to.
     */
    @ZenMethod
    void setInt(String key, int value);

    /**
     * Increment an integer value.
     *
     * @param key   The key whose value should be incremented.
     * @param value The value to increment by.
     */
    @ZenMethod
    void increment(String key, int value);

    /**
     * Get the string referenced by a key, or the fallback value given.
     *
     * @param key          The key whose value should be returned
     * @param defaultValue The value to return if the key is not present.
     * @return The value referenced by the key, or default value if the key wasn't present.
     */
    @ZenMethod
    String getOrDefault(String key, String defaultValue);

    /**
     * Get the string referenced by a key.
     *
     * @param key The key to get.
     * @return The string referenced by a key.
     */
    @ZenMethod
    String get(String key);

    /**
     * Get the integer referenced by a key, or 0 if it wasn't set before.
     *
     * @param key The key to get.
     * @return The integer referenced by a key, or 0 if it wasn't set before.
     */
    @ZenMethod
    int getInt(String key);

    /**
     * Get the string referenced by a key, put the fallback value given if the key was not present.
     *
     * @param key          The key whose value should be returned
     * @param initialValue The value to set and return if the key is not present.
     * @return The value referenced by the key, or initial value if the key wasn't present.
     */
    @ZenMethod
    String getOrPut(String key, String initialValue);

}
