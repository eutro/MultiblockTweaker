import mods.gregtech.multiblock.Builder;
import mods.gregtech.multiblock.FactoryBlockPattern;
import mods.gregtech.multiblock.RelativeDirection;
import mods.gregtech.multiblock.IBlockMatcher;
import mods.gregtech.multiblock.MultiblockAbility;
import mods.gregtech.multiblock.FactoryMultiblockShapeInfo;
import mods.gregtech.multiblock.IBlockInfo;

import mods.gregtech.MetaTileEntities;
import mods.gregtech.IMetaTileEntity;
import mods.gregtech.recipe.IRecipe;

import mods.gregtech.recipe.FactoryRecipeMap;
import mods.gregtech.recipe.RecipeMap;
import mods.gregtech.recipe.IRecipeLogic;
import mods.gregtech.recipe.functions.ICompleteRecipeFunction;

import crafttweaker.data.IData;
import crafttweaker.world.IFacing;
import crafttweaker.item.IItemStack;
import crafttweaker.item.IItemCondition;

if(loadedMods in "deepmoblearning")
{

// from DML's config
val mobs as int[string] = {
//  mob                 RF/t
    "zombie":           80,
    "skeleton":         80,
    "creeper":          80,
    "spider":           80,
    "slime":            150,
    "witch":            120,
    "blaze":            256,
    "ghast":            372,
    "wither_skeleton":  880,
    "enderman":         512,
    "wither":           2048,
    "dragon":           2560,
    "shulker":          256,
    "guardian":         340
};

val pristine_types as IItemStack[string] = {
//  mob                 pristine type
    "zombie":           <deepmoblearning:living_matter_overworldian>,
    "skeleton":         <deepmoblearning:living_matter_overworldian>,
    "creeper":          <deepmoblearning:living_matter_overworldian>,
    "spider":           <deepmoblearning:living_matter_overworldian>,
    "slime":            <deepmoblearning:living_matter_overworldian>,
    "witch":            <deepmoblearning:living_matter_overworldian>,
    "blaze":            <deepmoblearning:living_matter_hellish>,
    "ghast":            <deepmoblearning:living_matter_hellish>,
    "wither_skeleton":  <deepmoblearning:living_matter_hellish>,
    "enderman":         <deepmoblearning:living_matter_extraterrestrial>,
    "wither":           <deepmoblearning:living_matter_extraterrestrial>,
    "dragon":           <deepmoblearning:living_matter_extraterrestrial>,
    "shulker":          <deepmoblearning:living_matter_extraterrestrial>,
    "guardian":         <deepmoblearning:living_matter_overworldian>
};

val model_prefix = "deepmoblearning:data_model_";
val pristine_prefix = "deepmoblearning:pristine_matter_";

// from DML's config
val pristine_chances as int[] = [
    5,
    11,
    24,
    42
];

// from DML's config
val killMultiplier as int[] = [
    1,
    4,
    10,
    18,
    0 // Max tier, no kill multiplier
];

// from DML's config
val maxExperience as int[] = [
    6   * killMultiplier[0],
    12  * killMultiplier[1],
    30  * killMultiplier[2],
    50  * killMultiplier[3]
];

val name as string = "dml_sim_chamber";
val dml_recipe_map as RecipeMap = FactoryRecipeMap.start(name)
    .minInputs(2)
    .maxInputs(2)
    .minOutputs(2)
    .maxOutputs(2)
    .build();

val sim_chamber_multiblock = Builder.start(name, 2003)
    .withPattern(
        FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.DOWN, RelativeDirection.FRONT)
            .aisle(
                "OOOOO",
                "OGGGO",
                "OG GO",
                "OGGGO",
                "OOSOO")
            .aisle(
                "O   O",
                "GOOOG",
                " O-O ",
                "GOOOG",
                "O   O")
            .aisle(
                "O   O",
                " OOO ",
                " OOO ",
                "COOOC",
                "O   O")
            .aisle(
                "O   O",
                "GOOOG",
                " OOO ",
                "COOOC",
                "O   O")
            .aisle(
                "OOOOO",
                "OG GO",
                "O   O",
                "OGCCO",
                "OOOOO")
            .whereOr("O",
                <minecraft:obsidian>.asBlock() as IBlockMatcher,
                IBlockMatcher.abilityPartPredicate(
                                    MultiblockAbility.INPUT_ENERGY,
                                    MultiblockAbility.IMPORT_ITEMS,
                                    MultiblockAbility.EXPORT_ITEMS))
            .where("C", <minecraft:coal_block>)
            .where("G", <deepmoblearning:infused_ingot_block>)
            .where("-", <deepmoblearning:simulation_chamber>)
            .where("S", IBlockMatcher.controller(name))
            .where(" ", IBlockMatcher.AIR)
            .build())
    .addDesign(
        FactoryMultiblockShapeInfo.start()
            .aisle(
                "OOOOO",
                "OGCCO",
                "O   O",
                "OG GO",
                "OOOOO")
            .aisle(
                "I   O",
                "GOOOG",
                "GOOO ",
                "GOOOG",
                "O   O")
            .aisle(
                "S   O",
                "GOOOC",
                " -OO ",
                "GOOO ",
                "E   O")
            .aisle(
                "0   O",
                "GOOOC",
                "GOOO ",
                "GOOOG",
                "O   O")
            .aisle(
                "OOOOO",
                "OGCCO",
                "O   O",
                "OG GO",
                "OOOOO")
            .where("O", <minecraft:obsidian>)
            .where("C", <minecraft:coal_block>)
            .where("G", <deepmoblearning:infused_ingot_block>)
            .where("-", <deepmoblearning:simulation_chamber>)
            .where("S", IBlockInfo.controller(name))
            .where(" ", IBlockInfo.EMPTY)
            .where("I", MetaTileEntities.ITEM_IMPORT_BUS[0], IFacing.west())
            .where("0", MetaTileEntities.ITEM_EXPORT_BUS[0], IFacing.west())
            .where("E", MetaTileEntities.ENERGY_INPUT_HATCH[2], IFacing.west())
            .build())
    .withRecipeMap(dml_recipe_map)
    .buildAndRegister();

val DATA_MODEL_MAXIMUM_TIER = 4;

val asInt = function(data as IData) as int {
    return isNull(data) ? 0 : data as int;
};

sim_chamber_multiblock.completeRecipe = function(logic as IRecipeLogic) {
    for slot, stack in logic.inputInventory {
        if(!isNull(stack) && stack.definition.id.startsWith(model_prefix)) {
            var tier = asInt(stack.tag.tier);
            var simulationCount = asInt(stack.tag.simulationCount) + 1;
            var killCount = asInt(stack.tag.killCount);

            if(tier < DATA_MODEL_MAXIMUM_TIER) {
                val roof = maxExperience[tier];
                val killExperience = killCount * killMultiplier[tier];

                if(killExperience + simulationCount >= roof) {
                    killCount = 0;
                    simulationCount = 0;
                    tier += 1;
                }
            }

            logic.inputInventory.setStackInSlot(
                slot,
                stack.withTag(
                    stack.tag + ({
                        tier: tier,
                        simulationCount: simulationCount,
                        totalSimulationCount: asInt(stack.tag.totalSimulationCount) + 1,
                        killCount: killCount
                    } as IData)
                )
            );
            return;
        }
    }
};

val withTier = function(tier as int) as IItemCondition {
    return function(stack as IItemStack) as bool {
        return asInt(stack.tag.tier) == tier;
    } as IItemCondition;
};

for mob, cost in mobs {
    for tier, pristine_chance in pristine_chances {
        dml_recipe_map.recipeBuilder()
            .duration(301)
            .EUt(cost / 4 as int)
            .inputs(<deepmoblearning:polymer_clay>)
            .notConsumable(itemUtils.getItem(model_prefix + mob).withTag({tier: tier + 1}, false).only(withTier(tier + 1)))
            .outputs(pristine_types[mob])
            .chancedOutput(itemUtils.getItem(pristine_prefix + mob), pristine_chance * 100, 0)
            .buildAndRegister();
    }
}

}