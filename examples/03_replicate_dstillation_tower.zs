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
import mods.gregtech.IMetaTileEntity;

var loc = "copy_distillation_tower";

val copy_distillation_tower = Builder.start(loc) // automatic allocation ID
    .withPattern(function(controller as IControllerTile) as IBlockPattern {
                    var export_fluids_mtes as IMetaTileEntity[] = [];
                    for mte in <mte_ability:EXPORT_FLUIDS>.getMetaTileEntities() {
                        if (mte.metaTileEntityId != "gregtech:fluid_hatch.import_4x" &&
                                mte.metaTileEntityId != "gregtech:fluid_hatch.import_9x" &&
                                mte.metaTileEntityId != "gregtech:fluid_hatch.export_4x" &&
                                mte.metaTileEntityId != "gregtech:fluid_hatch.export_9x") {
                            export_fluids_mtes += mte;
                        }
                    }
                    return FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.FRONT, RelativeDirection.UP)
                              .aisle("YSY", "YFY", "YYY")
                              .aisle("XXX", "X#X", "XXX").setRepeatable(1, 11)
                              .aisle("XXX", "XXX", "XXX")
                              .where('S', controller.SELF())
                              .where('F', CTPredicate.abilities(<mte_ability:IMPORT_FLUIDS>))
                              .where('Y', CTPredicate.states(<metastate:gregtech:metal_casing:5>)
                                      | CTPredicate.abilities(<mte_ability:EXPORT_ITEMS>).setMinGlobalLimited(1)
                                      | CTPredicate.abilities(<mte_ability:INPUT_ENERGY>).setMinGlobalLimited(1))
                              .where('X', CTPredicate.states(<metastate:gregtech:metal_casing:5>)
                                      | CTPredicate.metaTileEntities(export_fluids_mtes).setMinLayerLimited(1).setMaxLayerLimited(1)
                                      | controller.autoAbilities(false, true, false, false, false, false, false))
                              .where('#', CTPredicate.AIR())
                              .build();
                 } as IPatternBuilderFunction)
    .withRecipeMap(<recipemap:distillation_tower>)
    .withBaseTexture(<metastate:gregtech:metal_casing:5>)
    .withMaintenance(true)
    .buildAndRegister();

// These are best specified in .lang files, since these may not work properly.
game.setLocalization(
    "multiblocktweaker.machine.copy_distillation_tower.name",
    "Copy Distillation Tower"
);
game.setLocalization(
    "multiblocktweaker.multiblock.copy_distillation_tower.description",
    "The Copy Distillation Tower is a multiblock that copy from dt"
);
// Don't forget to add a recipe!