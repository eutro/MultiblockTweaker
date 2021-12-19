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

import mods.gregtech.multiblock.functions.IGetBaseTextureFunction;
import mods.gregtech.multiblock.IIMultiblockPart;
import mods.gregtech.render.ICubeRenderer;
import mods.gregtech.render.MoveType;

/**
// Please use it with 05_register.zs
**/

var loc = "register_test";

val register_test = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                       return FactoryBlockPattern.start()
                          .aisle("CCC", "CCC", "CCC")
                          .aisle("CCC", "C C", "CMC")
                          .aisle("CSC", "CCC", "CCC")
                          .where('S', controller.self())
                          .where("C", CTPredicate.states(<metastate:gregtech:metal_casing:3>) | controller.autoAbilities(true, false, true, true, true, false, false))
                          .where('M', controller.autoAbilities(false, false, false, false, false, false, true)) // same as CTPredicate.abilities(<mte_ability:MUFFLER_HATCH>)
                          .where(' ', CTPredicate.AIR)
                          .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(
        FactoryRecipeMap.start("register_test") // create a RecipeMap.
            .minInputs(1)
            .maxInputs(1)
            .minOutputs(3)
            .maxOutputs(10)
            .maxFluidInputs(1)
            .setProgressBar(<texture_area:progress_bar_energy>, MoveType.HORIZONTAL) // register texture.
            .setSound(<sound:multiblocktweaker:tick.sound>)
            .build())
    .withBaseTexture(<cube_renderer:full_renderer>) // Looking for existing renderers in CEu. but yeah, you can also use <metastate:gregtech:metal_casing:3> here
    .buildAndRegister();

// set optional properties
register_test.hasMaintenanceMechanics = false;
register_test.hasMufflerMechanics = true;

// use our register renderer
register_test.frontOverlay = <cube_renderer:working_state_renderer>;

// more flexible custom renderer
register_test.getBaseTextureFunction = function (controller as IControllerTile, multiPart as IIMultiblockPart) as ICubeRenderer {
    if (isNull(multiPart)) { // if isNull, its the controller.
        return <cube_renderer:sided_renderer>;
    }
    return register_test.baseTexture; // <cube_renderer:full_renderer>
} as IGetBaseTextureFunction;

// add a simple recipe for our register_test RecipeMap.
register_test 
    .recipeMap // Please refer to the CEu Wiki for details on how to add recipes.
	.recipeBuilder()
    .duration(500)
    .EUt(500)
    .inputs(<minecraft:chest>)
    .fluidInputs(<liquid:water> * 8000)
    .outputs(<minecraft:grass> * 64,
             <minecraft:glass> * 64,
             <minecraft:tnt> * 64)
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "mbt.machine.register_test.name",
    "Register Test"
);
game.setLocalization(
    "mbt.multiblock.multiblock_dt.description",
    "The Register Test is a multiblock that test registers."
);
game.setLocalization(
    "recipemap.register_test.name",
    "Register Test"
);

// Don't forget to add a recipe!
