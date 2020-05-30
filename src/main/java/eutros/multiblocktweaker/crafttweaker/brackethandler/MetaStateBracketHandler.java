package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.zenscript.IBracketHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.intellij.lang.annotations.RegExp;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

import java.util.List;

@BracketHandler
@ZenRegister
public class MetaStateBracketHandler implements IBracketHandler {

    private final IJavaMethod method;

    @RegExp
    @Override
    public String getRegexMatchingString() {
        return "metastate:.+";
    }

    @Override
    public Class<IBlockState> getReturnedClass() {
        return IBlockState.class;
    }

    public MetaStateBracketHandler() {
        method = CraftTweakerAPI.getJavaMethod(MetaStateBracketHandler.class, "getBlockState", String.class, int.class);
    }

    @SuppressWarnings("unused")
    public static IBlockState getBlockState(String name, int meta) {
        if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(name))) {
            CraftTweakerAPI.logError("No block found with name '" + name + "'. Air block used instead.");
            return CraftTweakerMC.getBlockState(Blocks.AIR.getDefaultState());
        }

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));

        if(block == null)
            block = Blocks.AIR; // this shouldn't ever happen

        net.minecraft.block.state.IBlockState mcState = block.getDefaultState();
        for(net.minecraft.block.state.IBlockState s : block.getBlockState().getValidStates()) {
            if(block.getMetaFromState(s) == meta) {
                mcState = s;
                break;
            }
        }

        return CraftTweakerMC.getBlockState(mcState);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        IZenSymbol zenSymbol = null;

        String bracketString = tokens.stream().map(Token::getValue).reduce("", String::concat);

        String[] split = bracketString.split(":", 4);

        if (split.length > 1) {
            if ("metastate".equalsIgnoreCase(split[0])) {
                String blockName;
                int meta = 0;
                if (split.length > 2) {
                    blockName = split[1] + ":" + split[2];
                    if (split.length > 3) {
                        try {
                            meta = Integer.parseInt(split[3]);
                        } catch(NumberFormatException e) {
                            CraftTweakerAPI.logError(String.format("Invalid integer passed for metastate bracket handler: %s", split[3]), e);
                        }
                    }
                } else {
                    blockName = split[1];
                }
                if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName))) {
                    return null;
                }
                zenSymbol = new BlockStateReferenceSymbol(environment, blockName, meta);
            }
        }

        return zenSymbol;
    }

    private class BlockStateReferenceSymbol implements IZenSymbol {
        private final IEnvironmentGlobal environment;
        private final String name;
        private final int meta;

        public BlockStateReferenceSymbol(IEnvironmentGlobal environment, String name, int meta) {
            this.environment = environment;
            this.name = name;
            this.meta = meta;
        }

        @Override
        public IPartialExpression instance(ZenPosition position) {
            return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name), new ExpressionInt(position, meta, ZenType.INT));
        }
    }

}
