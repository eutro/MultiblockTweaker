// No special #loader, just use the default crafttweaker one.

import mods.gregtech.multiblock.Builder;
import mods.gregtech.multiblock.FactoryBlockPattern;
import mods.gregtech.multiblock.RelativeDirection;
import mods.gregtech.multiblock.IBlockMatcher;
import mods.gregtech.multiblock.MultiblockAbility;
import mods.gregtech.multiblock.FactoryMultiblockShapeInfo;
import mods.gregtech.multiblock.IBlockInfo;

import mods.gregtech.MetaTileEntities;

import mods.gregtech.recipe.FactoryRecipeMap;

import crafttweaker.world.IFacing;

var loc = "magic_miner";
var meta = 2001; // Choose something that won't conflict. You'll get a warning in the crafttweaker logs if something goes wrong.

var magic_miner = Builder.start(loc, meta)
    .withPattern(
        FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.DOWN, RelativeDirection.FRONT)
            .aisle(
                "CCC",
                "CCC",
                "CSC")
            .aisle(
                "CCC",
                "C C",
                "CCC")
            .aisle(
                "CCC",
                "CCC",
                "CCC")
            .whereOr("C",
                <metastate:gregtech:metal_casing:3>,
                IBlockMatcher.abilityPartPredicate(
                    MultiblockAbility.INPUT_ENERGY,
                    MultiblockAbility.IMPORT_ITEMS,
                    MultiblockAbility.IMPORT_FLUIDS,
                    MultiblockAbility.EXPORT_ITEMS
                ))
            .where("S", IBlockMatcher.controller(loc))
            .where(" ", IBlockMatcher.ANY)
            .build())
    .addDesign(
        FactoryMultiblockShapeInfo.start()
            .aisle(
                "ICC",
                "CCC",
                "ECC")
            .aisle(
                "SCC",
                "C C",
                "CCC")
            .aisle(
                "OCC",
                "CCC",
                "FCC")
            .where("C", <metastate:gregtech:metal_casing:3>)
            .where("S", IBlockInfo.controller(loc))
            .where("I", MetaTileEntities.ITEM_IMPORT_BUS[0], IFacing.west())
            .where("F", MetaTileEntities.FLUID_IMPORT_HATCH[0], IFacing.west())
            .where("O", MetaTileEntities.ITEM_EXPORT_BUS[0], IFacing.west())
            .where("E", MetaTileEntities.ENERGY_INPUT_HATCH[2], IFacing.west())
            .where(" ", IBlockInfo.EMPTY)
            .build())
    .clearTooltips(true)
    .withRecipeMap(
        FactoryRecipeMap.start(loc)
		    .minInputs(1)
		    .maxInputs(1)
		    .minOutputs(3)
		    .maxOutputs(27)
		    .maxFluidInputs(1)
		    .build())
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.magic_miner.name",
    "Magic Miner"
);
game.setLocalization(
    "multiblocktweaker.multiblock.magic_miner.description",
    "The Magic Miner is a multiblock that mines ores from nothing."
);
game.setLocalization(
    "recipemap.magic_miner.name",
    "Magic Miner"
);

// Don't forget to add a recipe!
recipes.addShaped(
    <gregtech:machine:2001>,
    [
        [<gregtech:cable:71>,         <gregtech:meta_item_2:32487>,         <gregtech:cable:71>],
        [<gregtech:meta_item_2:32487>, <gregtech:metal_casing:3>,  <gregtech:meta_item_2:32487>],
        [<gregtech:cable:71>,         <gregtech:meta_item_2:32487>,         <gregtech:cable:71>]
    ]
);

<multiblock:multiblocktweaker:magic_miner> // The Bracket Handler can also be used to refer to it
    .recipeMap
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
