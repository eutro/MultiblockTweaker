package eutros.multiblocktweaker.crafttweaker.construction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IFacing;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMultiblockShapeInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IBlockInfo;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMultiblockShapeInfo;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Used to create a design to show in JEI or as an in-world preview.
 <p>
 * Used for {@link MultiblockBuilder#addDesign(IMultiblockShapeInfo...)}.
 *
 * @zenClass mods.gregtech.multiblock.FactoryMultiblockShapeInfo
 * @see IMultiblockShapeInfo
 */
@ZenClass("mods.gregtech.multiblock.FactoryMultiblockShapeInfo")
@ZenRegister
public class MultiblockShapeInfoBuilder {

    private MultiblockShapeInfo.Builder inner;

    public MultiblockShapeInfoBuilder() {
        inner = MultiblockShapeInfo.builder();
    }

    /**
     * Start an empty builder.
     * <p>
     * Unlike {@link MultiblockShapeInfoBuilder}, this cannot be rotated.
     *
     * @return An empty builder.
     */
    @ZenMethod
    public static MultiblockShapeInfoBuilder start() {
        return new MultiblockShapeInfoBuilder();
    }

    /**
     * Add a single aisle.
     *
     * @param data The aisle pattern. Each unique character in any string must be defined in {@link #where(String, IBlockInfo)} or its equivalents.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder aisle(String... data) {
        inner = inner.aisle(data);
        return this;
    }

    /**
     * Add a repeated aisle.
     *
     * @param count How many times to repeat the aisle.
     * @param data  The aisle pattern. Each unique character in any string must be defined in {@link #where(String, IBlockInfo)} or its equivalents.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder aisleRepeated(int count, String... data) {
        for (int i = 0; i < count; i++) {
            inner = inner.aisle(data);
        }
        return this;
    }

    /**
     * Define a symbol.
     *
     * @param symbol The character that will represent this value in {@link #aisle(String...)}.
     * @param value  The IBlockInfo to show in the preview.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder where(String symbol, IBlockInfo value) {
        char c = getSymbol(symbol);
        if (c != '\0')
            inner = inner.where(c, value.getInternal());
        return this;
    }

    /**
     * Define a symbol.
     *
     * @param symbol The character that will represent this value in {@link #aisle(String...)}.
     * @param block  The block to show in the preview.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder where(String symbol, IBlock block) {
        Block blocc = CraftTweakerMC.getBlock(block.getDefinition());
        char c = getSymbol(symbol);
        if (c != '\0')
            inner = inner.where(c, blocc.getDefaultState());
        return this;
    }

    /**
     * Define a symbol.
     *
     * @param symbol The character that will represent this value in {@link #aisle(String...)}.
     * @param stack  The item whose block to show in the preview.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder where(String symbol, IItemStack stack) {
        return where(symbol, stack.asBlock());
    }

    /**
     * Define a symbol.
     *
     * @param symbol     The character that will represent this value in {@link #aisle(String...)}.
     * @param blockState The block state to show in the preview.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder where(String symbol, IBlockState blockState) {
        char c = getSymbol(symbol);
        if (c != '\0')
            inner = inner.where(c, CraftTweakerMC.getBlockState(blockState));
        return this;
    }

    /**
     * Define a symbol.
     *
     * @param symbol    The character that will represent this value in {@link #aisle(String...)}.
     * @param te        The machine tile entity to show in the preview.
     * @param frontSide The side the tile entity is facing.
     * @return This builder, for convenience.
     */
    @ZenMethod
    public MultiblockShapeInfoBuilder where(String symbol, IMetaTileEntity te, IFacing frontSide) {
        char c = getSymbol(symbol);
        if (c != '\0')
            inner = inner.where(c, te.getInternal(), (EnumFacing) frontSide.getInternal());
        return this;
    }

    /**
     * Create the {@link IMultiblockShapeInfo}, for use in multiblock creation. Can be called multiple times.
     *
     * @return The built {@link IMultiblockShapeInfo}.
     */
    @ZenMethod
    public IMultiblockShapeInfo build() {
        return new MCMultiblockShapeInfo(inner.build());
    }

    private char getSymbol(String symbol) {
        if (symbol.length() != 1) {
            CraftTweakerAPI.logError("Symbol given is not a single character!");
            return '\0';
        }
        return symbol.charAt(0);
    }

}
