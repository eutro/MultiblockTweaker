package eutros.multiblocktweaker.crafttweaker.gtwrap.constants;

import crafttweaker.annotations.ZenRegister;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCCubeRenderer;
import gregtech.api.render.Textures;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenClass("mods.gregtech.render.Textures")
@ZenRegister
public class ConstantTextures {

    /** */ @ZenProperty public static final IICubeRenderer MAGIC_ENERGY_ABSORBER = new MCCubeRenderer(Textures.MAGIC_ENERGY_ABSORBER);
    /** */ @ZenProperty public static final IICubeRenderer MAGIC_ENERGY_ABSORBER_ACTIVE = new MCCubeRenderer(Textures.MAGIC_ENERGY_ABSORBER_ACTIVE);
    /** */ @ZenProperty public static final IICubeRenderer MAGIC_ENERGY_CONVERTER = new MCCubeRenderer(Textures.MAGIC_ENERGY_CONVERTER);
    /** */ @ZenProperty public static final IICubeRenderer MAGIC_ENERGY_CONVERTER_ACTIVE = new MCCubeRenderer(Textures.MAGIC_ENERGY_CONVERTER_ACTIVE);
    /** */ @ZenProperty public static final IICubeRenderer BRONZE_PLATED_BRICKS = new MCCubeRenderer(Textures.BRONZE_PLATED_BRICKS);
    /** */ @ZenProperty public static final IICubeRenderer PRIMITIVE_BRICKS = new MCCubeRenderer(Textures.PRIMITIVE_BRICKS);
    /** */ @ZenProperty public static final IICubeRenderer COKE_BRICKS = new MCCubeRenderer(Textures.COKE_BRICKS);
    /** */ @ZenProperty public static final IICubeRenderer HEAT_PROOF_CASING = new MCCubeRenderer(Textures.HEAT_PROOF_CASING);
    /** */ @ZenProperty public static final IICubeRenderer FROST_PROOF_CASING = new MCCubeRenderer(Textures.FROST_PROOF_CASING);
    /** */ @ZenProperty public static final IICubeRenderer SOLID_STEEL_CASING = new MCCubeRenderer(Textures.SOLID_STEEL_CASING);
    /** */ @ZenProperty public static final IICubeRenderer CLEAN_STAINLESS_STEEL_CASING = new MCCubeRenderer(Textures.CLEAN_STAINLESS_STEEL_CASING);
    /** */ @ZenProperty public static final IICubeRenderer STABLE_TITANIUM_CASING = new MCCubeRenderer(Textures.STABLE_TITANIUM_CASING);
    /** */ @ZenProperty public static final IICubeRenderer ROBUST_TUNGSTENSTEEL_CASING = new MCCubeRenderer(Textures.ROBUST_TUNGSTENSTEEL_CASING);
    /** */ @ZenProperty public static final IICubeRenderer BRONZE_FIREBOX = new MCCubeRenderer(Textures.BRONZE_FIREBOX);
    /** */ @ZenProperty public static final IICubeRenderer BRONZE_FIREBOX_ACTIVE = new MCCubeRenderer(Textures.BRONZE_FIREBOX_ACTIVE);
    /** */ @ZenProperty public static final IICubeRenderer STEEL_FIREBOX = new MCCubeRenderer(Textures.STEEL_FIREBOX);
    /** */ @ZenProperty public static final IICubeRenderer STEEL_FIREBOX_ACTIVE = new MCCubeRenderer(Textures.STEEL_FIREBOX_ACTIVE);
    /** */ @ZenProperty public static final IICubeRenderer TITANIUM_FIREBOX = new MCCubeRenderer(Textures.TITANIUM_FIREBOX);
    /** */ @ZenProperty public static final IICubeRenderer TITANIUM_FIREBOX_ACTIVE = new MCCubeRenderer(Textures.TITANIUM_FIREBOX_ACTIVE);
    /** */ @ZenProperty public static final IICubeRenderer TUNGSTENSTEEL_FIREBOX = new MCCubeRenderer(Textures.TUNGSTENSTEEL_FIREBOX);
    /** */ @ZenProperty public static final IICubeRenderer TUNGSTENSTEEL_FIREBOX_ACTIVE = new MCCubeRenderer(Textures.TUNGSTENSTEEL_FIREBOX_ACTIVE);

}
