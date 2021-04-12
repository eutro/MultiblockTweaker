package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.gregtech.MultiblockRegistry;
import stanhebben.zenscript.ZenTokener;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.Iterator;
import java.util.List;

/**
 * A bracket handler for getting a custom multiblock.
 * <p>
 * Usage:
 * <p>
 * To get by metadata:
 * {@code <multiblock:{meta}>}
 * <p>
 * To get by resource location:
 * {@code <multiblock:{location}>}
 */
@BracketHandler
@ZenRegister
public class CustomMultiblockBracketHandler implements IBracketHandler {

    private final IJavaMethod intGet;
    private final IJavaMethod stringGet;

    public CustomMultiblockBracketHandler() {
        stringGet = CraftTweakerAPI.getJavaMethod(CustomMultiblockBracketHandler.class, "get", String.class);
        intGet = CraftTweakerAPI.getJavaMethod(CustomMultiblockBracketHandler.class, "get", int.class);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if (tokens.size() < 3) {
            return null;
        }

        Iterator<Token> it = tokens.iterator();
        if (it.next().getValue().equals("multiblock") &&
            it.next().getValue().equals(":")) {
            Token locToken = it.next();
            if (locToken.getType() == ZenTokener.T_INTVALUE) {
                int metaId = Integer.parseInt(locToken.getValue());
                return position -> new ExpressionCallStatic(position, environment, intGet, new ExpressionInt(position, metaId, ZenType.INT));
            } else {
                StringBuilder sb = new StringBuilder(locToken.getValue());
                while (it.hasNext()) {
                    sb.append(it.next().getValue());
                }
                return position -> new ExpressionCallStatic(position, environment, stringGet, new ExpressionString(position, sb.toString()));
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    public static CustomMultiblock get(String loc) {
        return MultiblockRegistry.get(loc);
    }

    @SuppressWarnings("unused")
    public static CustomMultiblock get(int metaId) {
        return MultiblockRegistry.get(metaId);
    }

    @Override
    public Class<CustomMultiblock> getReturnedClass() {
        return CustomMultiblock.class;
    }

    @Override
    public String getRegexMatchingString() {
        return "multiblock:.+";
    }

}

