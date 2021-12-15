// No special #loader, just use the default crafttweaker one.

import mods.gregtech.multiblock.Builder;
import mods.gregtech.multiblock.FactoryBlockPattern;
import mods.gregtech.multiblock.RelativeDirection;
import mods.gregtech.multiblock.functions.IPatternBuilderFunction;
import mods.gregtech.IControllerTile;
import mods.gregtech.multiblock.CTPredicate;
import mods.gregtech.multiblock.IBlockPattern;
// import mods.gregtech.multiblock.MultiblockAbility;

import mods.gregtech.MetaTileEntities;

import mods.gregtech.recipe.RecipeMap;

import crafttweaker.world.IFacing;
import crafttweaker.text.ITextComponent;

var loc = "multiblock_ddddd_smelter";

Builder.start(loc)
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                       return FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.DOWN, RelativeDirection.FRONT)
                           .aisle(
                               "KKK",
                               "KKK",
                               "KSK")
                           .aisle(
                               "CCC",
                               "C C",
                               "CCC")
                           .aisle(
                               "CCC",
                               "CCC",
                               "CCC")
                           .where("K", CTPredicate.states(<blockstate:minecraft:grass>).or(controller.autoAbilities))
                           .where("C", <blockstate:minecraft:grass>)
                           .where("S", controller.SELF())
                           .where(" ", CTPredicate.ANY())
                           .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(RecipeMap.getByName("bender")) // Just use an already existing recipe map.
    .withBaseTexture(<blockstate:minecraft:grass>)
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.multiblock_ddddd_smelter.name",
    "Multiblock ddddd Smelter"
);
game.setLocalization(
    "multiblocktweaker.multiblock.multiblock_ddddd_smelter.description",
    "The Multiblock ddddd Smelter is a multiblock that does ddddd smelter recipes. Hello, world!"
);

// Don't forget to add a recipe!
