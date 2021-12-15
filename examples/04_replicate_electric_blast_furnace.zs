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

import mods.gregtech.multiblock.functions.IInvalidateStructure;
import mods.gregtech.multiblock.functions.IFormStructureFunction;

var loc = "copy_electric_blast_furnace";

val copy_electric_blast_furnace = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                    return FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.FRONT, RelativeDirection.UP)
                              .aisle("XXX", "CCC", "CCC", "XXX")
                              .aisle("XXX", "C#C", "C#C", "XMX")
                              .aisle("XSX", "CCC", "CCC", "XXX")
                              .where('S', controller.SELF())
                              .where('X', CTPredicate.states(<metastate:gregtech:metal_casing:2>).setMinGlobalLimited(9)
                                      | controller.autoAbilities(true, true, true, true, true, true, false))
                              .where('M', CTPredicate.abilities(MultiblockAbility.MUFFLER_HATCH))
                              .where('C', CTPredicate.COILS())
                              .where('#', CTPredicate.AIR())
                              .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(<recipemap:electric_blast_furnace>)
    .withBaseTexture(<cube_renderer:BLAST_FURNACE_OVERLAY>)
    .buildAndRegister();

// set optional properties
copy_electric_blast_furnace.canBeDistinct = false;
copy_electric_blast_furnace.hasMufflerMechanics = true;

// set optional functions
copy_electric_blast_furnace.invalidateStructureFunction = function (controller as IControllerTile) {
    controller.setExtraData(0); // IData: blastFurnaceTemperature = 0
} as IInvalidateStructure;

val V = [8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432, 134217728, 536870912] as long[];

val getTierByVoltage = function (voltage as long) as byte{
    val tier = 1 as byte;
    while (tier < V.length) {
        if (voltage == V[tier]) {
            return tier;
        } else if (voltage < V[tier]) {
            return ((tier - 1 > 0) ? tier - 1 : 0) as byte;
        }
        tier += 1;
    }
    return ((V.length - 1 > tier) ? tier : V.length - 1) as byte;
};

copy_electric_blast_furnace.formStructureFunction = function (controller as IControllerTile, context as IPatternMatchContext) {
    val coils_temperature = context.getInt("coils_temperature");
    var blastFurnaceTemperature += 100 * Math.max(0, getTierByVoltage(controller.energyContainer.inputVoltage) - 2);
    if (blastFurnaceTemperature < 0) blastFurnaceTemperature = 0;
    controller.setExtraData(blastFurnaceTemperature); // IData: set blastFurnaceTemperature
} as IFormStructureFunction;

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.copy_electric_blast_furnace.name",
    "Copy Electric Blast Furnace"
);
game.setLocalization(
    "multiblocktweaker.multiblock.copy_electric_blast_furnace.description",
    "The Copy Electric Blast Furnace is a multiblock that copy from ebf"
);
// Don't forget to add a recipe!
