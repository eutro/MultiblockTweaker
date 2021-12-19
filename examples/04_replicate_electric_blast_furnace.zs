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

import mods.gregtech.predicate.IPredicate;
import mods.gregtech.predicate.ICandidates;
import mods.gregtech.multiblock.IBlockWorldState;
import crafttweaker.block.IBlockState;

import mods.gregtech.multiblock.IMultiblockShapeInfo;
import mods.gregtech.multiblock.FactoryMultiblockShapeInfo;
import mods.gregtech.multiblock.IBlockInfo;
import crafttweaker.world.IFacing;
import mods.gregtech.render.MoveType;

import crafttweaker.data.IData;
import mods.gregtech.multiblock.functions.IInvalidateStructureFunction;
import mods.gregtech.multiblock.functions.IFormStructureFunction;
import mods.gregtech.multiblock.IPatternMatchContext;
import mods.gregtech.multiblock.functions.ICheckRecipeFunction;
import mods.gregtech.recipe.IRecipe;
import mods.gregtech.multiblock.functions.IDisplayTextFunction;
import crafttweaker.formatting.IFormattedText;

import mods.gregtech.recipe.functions.IRunOverclockingLogicFunction;
import mods.gregtech.recipe.IRecipeLogic;
import mods.gregtech.recipe.functions.IGetRealValueFunction;
import mods.gregtech.recipe.CustomRecipeProperty;

/** 
// This example is one of the most difficult to customize. We learn how to build a machine exactly like CEu's EBF.
// Sounds easy, just a simple multi-block with some I/O buses and hatches?
// In fact, the complete EBF is quite complicated. We need to implement pattern predicates, previews of different coils, temperature logic.....
// Don't worry, let's take it one step at a time. 
// Once you get this example, I believe you have basically mastered the custom multi-block in MBT.
**/

var loc = "copy_electric_blast_furnace";

val wire_coil_temperature_map = {
    "gregtech:wire_coil:0" : 1800,
    "gregtech:wire_coil:1" : 2700,
    "gregtech:wire_coil:2" : 3600,
    "gregtech:wire_coil:3" : 4500,
    "gregtech:wire_coil:4" : 5400,
    "gregtech:wire_coil:5" : 7200,
    "gregtech:wire_coil:6" : 9001,
    "gregtech:wire_coil:7" : 10800,
} as int[string];

//********** pattern + shaps builder **********//
val multiblockBuild = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                    // block pos checking + write temperature to context
                    val coilPredicate = function(blockWorldState as IBlockWorldState) as bool {
                        val blockState as IBlockState = blockWorldState.state;
                        if (blockState.block.definition.id == "gregtech:wire_coil") { // check whether the block is the coil.
                            val id as string = blockState.block.definition.id + ":" + blockState.withProperty("active", "false").meta;
                            if (id != blockWorldState.matchContext.getOrDefault("CoilType", id)) { // check if all coils are of the same type
                                blockWorldState.setError("all coils should be the same type"); // set the error info if failed. btw, you'd better use the lang.files
                                return false;
                            } else {
                                blockWorldState.matchContext.set("CoilType", id);
                                blockWorldState.matchContext.setInt("coils_temperature", wire_coil_temperature_map[id]);
                            }
                            // Some blocks that extends of {@link VariantActiveBlock} can change texture based on the multi-block working state.
                            // If you want to keep this effect (e.g., emissive active blocks), their position need to be collected during pattern checking.
                            // VBBlock in CEu: <gregtech:boiler_casing> <gregtech:fusion_casing> <gregtech:transparent_casing> <gregtech:multiblock_casing> <gregtech:wire_coil>
                            blockWorldState.matchContext.addVABlock(blockWorldState.pos);
                            return true;
                        }
                        return false;
                    } as IPredicate;
                    // block pos candidates
                    val coilCandidates = function() as IBlockInfo[] {
                        val candidates as IBlockInfo[] = [
                            <metastate:gregtech:wire_coil:0>,
                            <metastate:gregtech:wire_coil:1>,
                            <metastate:gregtech:wire_coil:2>,
                            <metastate:gregtech:wire_coil:3>,
                            <metastate:gregtech:wire_coil:4>,
                            <metastate:gregtech:wire_coil:5>,
                            <metastate:gregtech:wire_coil:6>,
                            <metastate:gregtech:wire_coil:7>
                        ];
                        return candidates;
                    } as ICandidates;
                    val COILS as CTPredicate = CTPredicate(coilPredicate, coilCandidates).addTooltips("all coils should be the same type");

                    return FactoryBlockPattern.start()
                            .aisle("XXX", "CCC", "CCC", "XXX")
                            .aisle("XXX", "C#C", "C#C", "XMX")
                            .aisle("XSX", "CCC", "CCC", "XXX")
                            .where('S', controller.self())
                            .where('X', (<metastate:gregtech:metal_casing:2> as CTPredicate).setMinGlobalLimited(9) // same as CTPredicate.states(<metastate:gregtech:metal_casing:2>).setMinGlobalLimited(9)
                                    | controller.autoAbilities(true, true, true, true, true, true, false))
                            .where('M', <mte_ability:MUFFLER_HATCH>)
                            // .where('C', CTPredicate.COILS()) 
                            // In fact, you can directly use the code commented above, which is the built-in predicate for the coils.
                            // In order to demonstrate the power of MBT, we implement it in zs.
                            .where('C', COILS)
                            .where('#', CTPredicate.AIR)
                            .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(
        FactoryRecipeMap.start("copy_electric_blast_furnace", <recipe_property:ebf_temperature>) // define our own RecipeMap. And define a recipe property with the key tempurature (<recipe_property:key>).
            // .setDefaultRecipe(<recipemap:electric_blast_furnace>.recipeBuilder) // Set the default recipe builder, that will be copied in order to add new recipes.
            .minInputs(1)
            .maxInputs(3)
            .minOutputs(1)
            .maxOutputs(2)
            .maxFluidInputs(1)
            .maxFluidOutputs(1)
            .setProgressBar(<texture_area:PROGRESS_BAR_COMPRESS>, MoveType.HORIZONTAL) // Let's try a different texture.
            .setSound(<sound:gregtech:tick.furnace>) // CEu sound is reused here. mods.gregtech.ISound.registerSound("modid:xxxxx") to register the sound in #loader preinit.
            .build())
    .withBaseTexture(<metastate:gregtech:metal_casing:2>);

// add custom jei previews for different coils pages, tho CEu will automatically create previews, but you may want to add them yourself. 
val shapeBuilder = FactoryMultiblockShapeInfo.start()
    .aisle("XEM", "CCC", "CCC", "XXX")
    .aisle("FXD", "C#C", "C#C", "XHX")
    .aisle("ISO", "CCC", "CCC", "XXX")
    .where('X', <metastate:gregtech:metal_casing:2>)
    .where('S', IBlockInfo.controller(loc, IFacing.south())) // CONTROLLER IBlockInfo.controller(loc, IFacing) pay attention to the position of IFacing
    .where('#', IBlockInfo.EMPTY)
    .where('E', <mte:ENERGY_INPUT_HATCH>[2], IFacing.north()) // MV ENERGY_INPUT_HATCH
    .where('I', <mte:ITEM_IMPORT_BUS>[1], IFacing.south())
    .where('O', <mte:ITEM_EXPORT_BUS>[1], IFacing.south())
    .where('F', <mte:FLUID_IMPORT_HATCH>[1], IFacing.west())
    .where('D', <mte:FLUID_EXPORT_HATCH>[1], IFacing.east())
    .where('H', <mte:MUFFLER_HATCH>[1], IFacing.up())
    .where('M', <mte:MAINTENANCE_HATCH>[0], IFacing.north());
val copy_electric_blast_furnace =multiblockBuild
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:0>).build()) // set the coil block and build
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:1>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:2>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:3>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:4>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:5>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:6>).build())
    .addDesign(shapeBuilder.where('C', <metastate:gregtech:wire_coil:7>).build())
    .buildAndRegister();

//********** set optional properties **********//
copy_electric_blast_furnace.canBeDistinct = false;
copy_electric_blast_furnace.hasMufflerMechanics = true;

//********** set optional functions **********//
val V = [8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432, 134217728, 536870912] as long[];

val getTierByVoltage = function (voltage as long) as int{
   var tier as int = 1;
   while (tier < V.length) {
       if (voltage == V[tier]) {
           return tier;
       } else if (voltage < V[tier]) {
           return ((tier - 1 > 0) ? tier - 1 : 0);
       }
       tier += 1;
   }
   return ((V.length - 1 > tier) ? tier : V.length - 1);
};

// read temperature when structure formed
copy_electric_blast_furnace.formStructureFunction = function (controller as IControllerTile, context as IPatternMatchContext) {
   var blastFurnaceTemperature as int = context.getInt("coils_temperature");
   blastFurnaceTemperature += max(100 * (getTierByVoltage(controller.energyContainer.inputVoltage as long) - 2), 0); // voltage overclock temperature
   controller.setExtraData(blastFurnaceTemperature as IData); // IData: set blastFurnaceTemperature
} as IFormStructureFunction;

// clear temperature when structure is invalid
copy_electric_blast_furnace.invalidateStructureFunction = function (controller as IControllerTile) {
   controller.setExtraData((0 as IData)); // IData: blastFurnaceTemperature = 0
} as IInvalidateStructureFunction;

// check current recipe available according to the temperature.
copy_electric_blast_furnace.checkRecipeFunction = function(controller as IControllerTile, recipe as IRecipe, consumeIfSuccess as bool) as bool {
    val blastFurnaceTemperature as int = controller.getExtraData().asInt();
    val recipeTemperature = recipe.getIntegerProperty("ebf_temperature"); // read temperature from <recipe_property:ebf_temperature>
    return blastFurnaceTemperature >= recipeTemperature;
} as ICheckRecipeFunction;

// add display msgs in the controller gui
copy_electric_blast_furnace.displayTextFunction = function(controller as IControllerTile) as IFormattedText[] {
    if (controller.isStructureFormed()) {
        val blastFurnaceTemperature as int = controller.getExtraData().asInt();
        val info as IFormattedText = format.red("Max Temperature: " + blastFurnaceTemperature + "K");
        return ([info] as IFormattedText[]);
    }
    return null;
} as IDisplayTextFunction;

//********** set recipe logic functions **********//
// set overclocking according to the temperature
copy_electric_blast_furnace.runOverclockingLogic = function(recipeLogic as IRecipeLogic, recipe as IRecipe, negativeEU as bool, maxOverclocks as int) as int[] {
    val recipeEU as int = recipe.getEUt();
    val maxVoltage as long = recipeLogic.maxVoltage;
    val dur as int = recipe.getDuration();
    val temperature as int = recipeLogic.metaTileEntity.getExtraData().asInt();
    val recipe_temperature as int = recipe.getIntegerProperty("ebf_temperature"); // read temperature from <recipe_property:ebf_temperature>
    // To avoid the complexity of the code, the built-in function is called here.
    // heatingCoilOverclockingLogic() and standardOverclockingLogic() are two built-in overclocking functions of CEu.
    return IRecipeLogic.heatingCoilOverclockingLogic(recipeEU, maxVoltage, dur, maxOverclocks, temperature, recipe_temperature);
} as IRunOverclockingLogicFunction;

//********** set Recipe Property **********//
// set tips in JEI
<recipe_property:ebf_temperature>.isHidden = false; // is hidden in JEI.
<recipe_property:ebf_temperature>.color = -1; // set color
<recipe_property:ebf_temperature>.getRealValueFunction = function (recipeProperty as CustomRecipeProperty, value as string) as IFormattedText {
    // val temperature = (value as IData)asInt(); // this can be used to convert type
    return format.red(value);
} as IGetRealValueFunction;

//********** add recipes **********//
// add a simple recipe for our magic_miner RecipeMap.
copy_electric_blast_furnace
    .recipeMap 
	.recipeBuilder()
    .duration(500)
    .EUt(500)
    .inputs(<metaitem:dustChalcopyrite>, 
            <metaitem:dustSiliconDioxide> * 3)
    .fluidInputs(<liquid:oxygen> * 3000)
    .outputs(<metaitem:dustCupricOxide>,
             <metaitem:dustFerrosilite>)
    .fluidOutputs(<liquid:sulfur_dioxide> * 3000)
    .property("ebf_temperature", 4321) // key as the <recipe_property:ebf_temperature> defined
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "gregtech.recipe.ebf_temperature",
    "Temperature: %sK"
);
game.setLocalization(
    "multiblocktweaker.machine.copy_electric_blast_furnace.name",
    "Copy Electric Blast Furnace"
);
game.setLocalization(
    "multiblocktweaker.multiblock.copy_electric_blast_furnace.description",
    "The Copy Electric Blast Furnace is a multiblock that copy from ebf"
);
// Don't forget to add a recipe!
