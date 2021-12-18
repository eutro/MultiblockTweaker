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
import crafttweaker.item.IIngredient;

import mods.gregtech.multiblock.functions.ICheckRecipeFunction;
import mods.gregtech.multiblock.functions.IUpdateFormedValidFunction;
import mods.gregtech.recipe.functions.ICompleteRecipeFunction;

import mods.gregtech.recipe.IRecipeLogic;
import mods.gregtech.recipe.IRecipe;

var loc = "midas_implosion_compressor";

/** 
// As the previous examples go from simple to advanced, I'm sure you've mastered the MBT. 
// In fact, there are many APIs not shown in the examples, please refer to wiki.
// We want you to use your imagination, and MBT can actually do a lot of interesting things.
// As an appendix page, we'll implement a Midas touch implosion compressor. See how it works in the game.
**/

//********** pattern builder **********//
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

// check current recipe available, and place inputs bocks.
midas_implosion_compressor.checkRecipeFunction = function(controller as IControllerTile, recipe as IRecipe, consumeIfSuccess as bool) as bool {
    val world as IWorld = controller.world;
    for pos in getSurround(controller.pos, controller.frontFacing) {
        if (!world.isAirBlock(pos)) {
            return false;
        }
    }
    val inputs = (recipe.getInputs() as IIngredient[])[0].items as IItemStack[];
    val blockstate as IBlockState = inputs[0].asBlock().definition.getStateFromMeta(inputs[0].metadata);
    for pos in getSurround(controller.pos, controller.frontFacing) {
        world.setBlockState(blockstate, pos);
    }
    return true;
} as ICheckRecipeFunction;

// if the machine is working --- explosion. if the machine is not working and not active (have a recipe but not working, e.g. hasEnergyProblem) --- collect outputs.
midas_implosion_compressor.updateFormedValidFunction = function(controller as IControllerTile) {
    val world as IWorld = controller.world;
    if (controller.offsetTimer % 20 == 0) {
        if (controller.recipeLogic.isWorking) { // EXPLOSION!!!!!!
            val explosionPos as IBlockPos = controller.pos.getOffset(controller.frontFacing.opposite, 2).getOffset(IFacing.up(), 2);
            world.performExplosion(null, explosionPos.x, explosionPos.y, explosionPos.z, 3, true, false);
        } else if (!controller.recipeLogic.isActive) { // collect outputs
            for pos in getSurround(controller.pos, controller.frontFacing) {
                if (!world.isAirBlock(pos)) {
                    val item = world.getPickedBlock(pos, null, null) as IItemStack;
                    world.setBlockState(<blockstate:minecraft:air>, pos);
                    controller.outputInventory.addItems(false, [item] as IItemStack[]);
                    return;
                }
            }
        }
    }
} as IUpdateFormedValidFunction;

// When the recipe is complete, place the block according to the recipe output.
midas_implosion_compressor.completeRecipeFunction = function (recipeLogic as IRecipeLogic) as bool {
    val controller as IControllerTile = recipeLogic.metaTileEntity;
    val world as IWorld = controller.world;
    val outputs = recipeLogic.itemOutputs as IItemStack[];
    val blockstate as IBlockState = outputs[0].asBlock().definition.getStateFromMeta(outputs[0].metadata);
    for pos in getSurround(controller.pos, controller.frontFacing) {
         world.setBlockState(blockstate, pos);
    }
    recipeLogic.itemOutputs = ([] as IItemStack[]); // we have collected outputs ourselves, so remove the recipe outputs.
    return true;
} as ICompleteRecipeFunction;

//********** set recipe logic functions **********//
// set overclocking according to the temperature


//********** add recipes **********//
// add some recipes for midas_implosion_compressor.
midas_implosion_compressor
    .recipeMap 
	.recipeBuilder()
    .duration(180)
    .EUt(30)
    .inputs(<minecraft:stone>*9, <minecraft:tnt>)
    .outputs(<minecraft:gold_block>)
    .buildAndRegister();

midas_implosion_compressor
    .recipeMap 
	.recipeBuilder()
    .duration(180)
    .EUt(30)
    .inputs(<minecraft:dirt>*9, <minecraft:tnt>)
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
