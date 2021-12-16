package eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.functions.IDrawInfoFunction;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCSound;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.*;

import java.util.Objects;

/**
 * Can be used for recipe property.
 *
 * @zenClass mods.gregtech.ISound
 */
@ZenClass("mods.gregtech.recipe.IRecipeProperty")
@ZenRegister
public interface IRecipeProperty {
    RecipeProperty<?> getInternal();

    @ZenMethod
    @Nullable
    static IRecipeProperty byKey(String key) {
        return null;
    }

    /**
     * draw the property info in the gui
     */
    @ZenMethod
    @ZenSetter("drawInfoFunction")
    void setDrawInfoFunction(IDrawInfoFunction drawInfoFunction);

    @ZenMethod
    default String getKey() {
        return getInternal().getKey();
    }

    /**
     * if the property should display any information in JEI
     */
    @ZenMethod
    @ZenGetter("isHidden")
    default boolean isHidden() {
        return getInternal().isHidden();
    }

    /**
     * if the property should display any information in JEI
     */
    @ZenMethod
    @ZenSetter("isHidden")
    void setHidden(boolean isHidden);
}
