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
import mods.gregtech.recipe.RecipeMap;
import mods.gregtech.render.GuiTextures;
import mods.gregtech.render.MoveType;

import crafttweaker.world.IFacing;
import crafttweaker.text.ITextComponent;

var loc = "multiblock_alloy_smelter";
var meta = 2000; // Choose something that won't conflict. You'll get a warning in the crafttweaker logs if something goes wrong.

var multiblock = Builder.start(loc, meta)
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
                <metastate:gregtech:metal_casing:2>,
                IBlockMatcher.abilityPartPredicate(
                    MultiblockAbility.INPUT_ENERGY,
                    MultiblockAbility.IMPORT_ITEMS,
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
                "CCC")
            .aisle(
                "SCC",
                "E C",
                "CCC")
            .aisle(
                "OCC",
                "CCC",
                "CCC")
            .where("C", <metastate:gregtech:metal_casing:2>)
            .where("S", IBlockInfo.controller(loc))
            .where("I", MetaTileEntities.ITEM_IMPORT_BUS[0], IFacing.west())
            .where("O", MetaTileEntities.ITEM_EXPORT_BUS[0], IFacing.west())
            .where("E", MetaTileEntities.ENERGY_INPUT_HATCH[2], IFacing.west())
            .where(" ", IBlockInfo.EMPTY)
            .build())
    .withPartTooltip(<gregtech:metal_casing:2>, ITextComponent.fromString("Example") as ITextComponent)
    .withRecipeMap(
        // You could use an existing recipe map:
        //    RecipeMap.getByName("alloy_smelter")
        // or create a new one, and copy recipes over:
        FactoryRecipeMap.start(loc)
    		    .minInputs(1)
    		    .maxInputs(2)
    		    .minOutputs(1)
    		    .maxOutputs(1)
    		    .setSlotOverlay(false, false, GuiTextures.FURNACE_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, MoveType.HORIZONTAL)
    		    .build())
    .buildAndRegister();
multiblock.recipeMap.copyAll(RecipeMap.getByName("alloy_smelter")); // Copy all alloy smelter recipes

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.multiblock_alloy_smelter.name",
    "Multiblock Alloy Smelter"
);
game.setLocalization(
    "multiblocktweaker.multiblock.multiblock_alloy_smelter.description",
    "The Multiblock Alloy Smelter is a multiblock that does alloy smelter recipes. Hello, world!"
);

// Don't forget to add a recipe!
recipes.addShaped(
    <gregtech:machine:2000>,
    [
        [<gregtech:cable:71>,         <gregtech:meta_item_2:32487>,         <gregtech:cable:71>],
        [<gregtech:meta_item_2:32487>, <gregtech:metal_casing:2>,  <gregtech:meta_item_2:32487>],
        [<gregtech:cable:71>,         <gregtech:meta_item_2:32487>,         <gregtech:cable:71>]
    ]
);
