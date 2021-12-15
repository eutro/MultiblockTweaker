// No special #loader, just use the default crafttweaker one.

import mods.gregtech.multiblock.Builder;
import mods.gregtech.multiblock.FactoryBlockPattern;
import mods.gregtech.multiblock.RelativeDirection;
import mods.gregtech.multiblock.functions.IPatternBuilderFunction;
import mods.gregtech.IControllerTile;
import mods.gregtech.multiblock.CTPredicate;
import mods.gregtech.multiblock.IBlockPattern;
import mods.gregtech.recipe.FactoryRecipeMap;
import mods.gregtech.recipe.RecipeMap;

var loc = "mbt:magic_miner";

val magic_miner = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                       return FactoryBlockPattern.start()
                          .aisle("CCC", "CCC", "CCC")
                          .aisle("CCC", "C C", "CMC")
                          .aisle("CSC", "CCC", "CCC")
                          .where('S', controller.SELF())
                          .where("C", CTPredicate.states(<metastate:gregtech:metal_casing:3>)
                                      | CTPredicate.abilities(<mte_ability:IMPORT_ITEMS>).setMinGlobalLimited(1).setPreviewCount(1) // There is at least one IMPORT_ITEMS bus. JEI preview shows only one.
                                      | CTPredicate.abilities(<mte_ability:EXPORT_ITEMS>).setMinGlobalLimited(1).setPreviewCount(1)
                                      | CTPredicate.abilities(<mte_ability:IMPORT_FLUIDS>).setMinGlobalLimited(1).setPreviewCount(1)
                                      | CTPredicate.abilities(<mte_ability:INPUT_ENERGY>).setMinGlobalLimited(1).setMaxGlobalLimited(3).setPreviewCount(1) // There is at least one INPUT_ENERGY hatch and no more than three of it. JEI preview shows only one.
                          )
                                      /*
                                        Although the above predicate matcher works well, we recommend using controller.autoAbilities() to automatically generate ability predicates according to the RecipeMap.
                                        Besides, If you want to generate separate predicates you can also use it. autoAbilities(boolean checkEnergyIn, boolean checkMaintainer, boolean checkItemIn, boolean checkItemOut, boolean checkFluidIn, boolean checkFluidOut, boolean checkMuffler);
                                        So anyway, this predicate can be simplified like this:
                                            .where("C", CTPredicate.states(<metastate:gregtech:metal_casing:3>) | controller.autoAbilities(true, false, true, true, true, false, false))
                                      */
                          .where('M', controller.autoAbilities(false, false, false, false, false, false, true)) // same as CTPredicate.abilities(<mte_ability:MUFFLER_HATCH>)
                          .where(' ', CTPredicate.AIR())
                          .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(
        FactoryRecipeMap.start("magic_miner") // create a RecipeMap.
            .minInputs(1)
            .maxInputs(1)
            .minOutputs(3)
            .maxOutputs(27)
            .maxFluidInputs(1)
            .build())
    .withBaseTexture(<cube_renderer:FROST_PROOF_CASING>) // Looking for existing renderers in CEu. but yeah, you can also use <metastate:gregtech:metal_casing:3> here
    .withMaintenance(false)
    .withMuffler(true)
    .buildAndRegister();

// add a simple recipe for our magic_miner RecipeMap.
magic_miner // The Bracket Handler can also be used to refer to it. (<multiblock:mbt:magic_miner>)
    .recipeMap // Please refer to the CEu Wiki for details on how to add recipes.
	.recipeBuilder()
    .duration(500)
    .EUt(500)
    .inputs(<minecraft:chest>)
    .fluidInputs(<liquid:water> * 8000)
    .outputs(<gregtech:ore_cassiterite_0:3> * 64,
             <gregtech:ore_redstone_0> * 64,
             <gregtech:ore_nickel_0> * 64,
             <gregtech:ore_rutile_0> * 64,
             <gregtech:ore_rutile_0> * 64,
             <gregtech:ore_uraninite_0:3> * 64,
             <gregtech:ore_galena_0> * 64,
             <gregtech:ore_galena_0> * 64,
             <gregtech:ore_salt_0> * 64)
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "mbt.machine.magic_miner.name",
    "Magic Miner"
);
game.setLocalization(
    "kilabash.multiblock.multiblock_dt.description",
    "The Magic Miner is a multiblock that mines ores from nothing."
);
game.setLocalization(
    "recipemap.magic_miner.name",
    "Magic Miner"
);
// Don't forget to add a recipe!
