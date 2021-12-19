package eutros.multiblocktweaker.gregtech.recipes;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.functions.IGetRealValueFunction;
import gregtech.api.recipes.recipeproperties.RecipeProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * The custom RecipeProperty for recipes.
 *
 * @zenClass mods.gregtech.recipe.CustomRecipeProperty
 */
@ZenClass("mods.gregtech.recipe.CustomRecipeProperty")
@ZenRegister
public class CustomRecipeProperty extends RecipeProperty<Object> {

    /**
     * The {@link IGetRealValueFunction} this RecipeProperty has.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public IGetRealValueFunction getRealValueFunction;
    /**
     * The color of tooltips when it's shown in JEI.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public Integer color;
    /**
     * Are tooltips hidden in JEI.
     * <p>
     * Should be set using the ZenSetter.
     */
    @ZenProperty
    public boolean isHidden;

    public CustomRecipeProperty(String key) {
        super(key, Object.class);
    }

    /**
     * Get the key of this RecipeProperty.
     *
     * @return The key of this RecipeProperty.
     */
    @Override
    @ZenMethod
    @ZenGetter("key")
    public String getKey() {
        return super.getKey();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
        String result = value.toString();
        if (getRealValueFunction != null) {
            try {
                result = getRealValueFunction.map(this, result).getText();
            } catch (RuntimeException t) {
                CraftTweakerAPI.logError(String.format("Couldn't run %s function of RecipeProperty %s.", getRealValueFunction, getKey()), t);
                getRealValueFunction = null;
            }
        }
        minecraft.fontRenderer.drawString(I18n.format("gregtech.recipe." + getKey(),
                result),
                x, y,
                this.color == null ? color : this.color);
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public boolean isOfType(Class<?> otherType) {
        return false;
    }
}
