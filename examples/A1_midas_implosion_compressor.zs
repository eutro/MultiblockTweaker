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
import crafttweaker.block.IBlockState;

import crafttweaker.world.IBlockPos;
import crafttweaker.world.IFacing;
import crafttweaker.world.IWorld;
import crafttweaker.item.IItemStack;

import mods.gregtech.multiblock.functions.ICheckRecipeFunction;
import mods.gregtech.multiblock.functions.IUpdateFormedValidFunction;
import mods.gregtech.recipe.functions.ICompleteRecipeFunction;

import mods.gregtech.recipe.IRecipeLogic;
import mods.gregtech.recipe.IRecipe;

var loc = "midas_implosion_compressor";

//********** patter + shaps builder **********//
val midas_implosion_compressor = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                    return FactoryBlockPattern.start()
                            .aisle("XXEXX", "CCCCC", "CCCCC", "CCCCC")
                            .aisle("XXXXX", "C   C", "C   C", "CCCCC")
                            .aisle("XXXXX", "C   C", "C   C", "CCCCC")
                            .aisle("XXXXX", "C   C", "C   C", "CCCCC")
                            .aisle("XISOX", "CCCCC", "CCCCC", "CCCCC")
                            .where('S', controller.SELF())
                            .where('I', CTPredicate.abilities(<mte_ability:IMPORT_ITEMS>))
                            .where('O', CTPredicate.abilities(<mte_ability:EXPORT_ITEMS>))
                            .where('E', CTPredicate.abilities(<mte_ability:INPUT_ENERGY>))
                            .where('C', <metastate:gregtech:transparent_casing:1>)
                            .where('X', <blockstate:minecraft:obsidian>)
                            .build();
                 } as IPatternBuilderFunction)
    // .withRecipeMap(<recipemap:electric_blast_furnace>) 
    .withRecipeMap(
        FactoryRecipeMap.start("midas_implosion_compressor") // define our own RecipeMap. And define a recipe property with the key tempurature (<recipe_property:key>).
            .minInputs(1)
            .maxInputs(2)
            .minOutputs(1)
            .maxOutputs(1)
            .build())
    .withBaseTexture(<blockstate:minecraft:tnt>)
    .buildAndRegister();

//********** set optional properties **********//
midas_implosion_compressor.canBeDistinct = false;
midas_implosion_compressor.hasMufflerMechanics = false;
midas_implosion_compressor.hasMaintenanceMechanics = false;

//********** set optional functions **********//
val getSurround = function (pos as IBlockPos, facing as IFacing) as IBlockPos[] {
    val center as IBlockPos = pos.getOffset(facing.opposite, 2).getOffset(IFacing.up(), 1);
    return [
        center,
        center.getOffset(IFacing.north(), 1),
        center.getOffset(IFacing.north(), 1).getOffset(IFacing.west(), 1),
        center.getOffset(IFacing.west(), 1),
        center.getOffset(IFacing.west(), 1).getOffset(IFacing.south(), 1),
        center.getOffset(IFacing.south(), 1),
        center.getOffset(IFacing.south(), 1).getOffset(IFacing.east(), 1),
        center.getOffset(IFacing.east(), 1),
        center.getOffset(IFacing.east(), 1).getOffset(IFacing.north(), 1),
    ] as IBlockPos[];
};

// check current recipe available
midas_implosion_compressor.checkRecipeFunction = function(controller as IControllerTile, recipe as IRecipe, consumeIfSuccess as bool) as bool {
    val world as IWorld = controller.world;
    for pos in getSurround(controller.pos, controller.frontFacing) {
        if (world.getBlock(pos).definition.id != "minecraft:air") {
            return false;
        }
    }
    for pos in getSurround(controller.pos, controller.frontFacing) {
        world.setBlockState(<blockstate:minecraft:stone>, pos);
    }
    return true;
} as ICheckRecipeFunction;

midas_implosion_compressor.updateFormedValidFunction = function(controller as IControllerTile) {
    val world as IWorld = controller.world;
    if (controller.offsetTimer % 20 == 0) {
        if (controller.recipeLogic.isWorking) { // EXPLOSION!!!!!!
            val explosionPos as IBlockPos = controller.pos.getOffset(controller.frontFacing.opposite, 2).getOffset(IFacing.up(), 2);
            world.performExplosion(null, explosionPos.x, explosionPos.y, explosionPos.z, 3, true, false);
        } else { // colect outputs
            for pos in getSurround(controller.pos, controller.frontFacing) {
                if (world.getBlock(pos).definition.id == "minecraft:gold_block") {
                    world.setBlockState(<blockstate:minecraft:air>, pos);
                    controller.outputInventory.addItems(false, [<minecraft:gold_block>] as IItemStack[]);
                    return;
                }
            }
        }
    }
} as IUpdateFormedValidFunction;

midas_implosion_compressor.completeRecipeFunction = function (recipe as IRecipeLogic) as bool {
    val controller as IControllerTile = recipe.metaTileEntity;
    val world as IWorld = controller.world;
    for pos in getSurround(controller.pos, controller.frontFacing) {
        if (world.getBlock(pos).definition.id == "minecraft:stone") {
            world.setBlockState(<blockstate:minecraft:gold_block>, pos);
        }
    }
    return true;
} as ICompleteRecipeFunction;

//********** set recipe logic functions **********//
// set overclocking according to the temperature


//********** add recipes **********//
// add a simple recipe for midas_implosion_compressor.
midas_implosion_compressor
    .recipeMap 
	.recipeBuilder()
    .duration(180)
    .EUt(30)
    .inputs(<minecraft:stone>*8, <minecraft:tnt>)
    .outputs(<minecraft:gold_block>)
    .buildAndRegister();

midas_implosion_compressor
    .recipeMap 
	.recipeBuilder()
    .duration(180)
    .EUt(30)
    .inputs(<minecraft:dirt>*8, <minecraft:tnt>)
    .outputs(<minecraft:grass>)
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.midas_implosion_compressor.name",
    "Midas Imposion Compressor"
);
game.setLocalization(
    "multiblocktweaker.multiblock.midas_implosion_compressor.description",
    "The Midas Imposion Compressor is a multiblock with magic"
);
// Don't forget to add a recipe!
