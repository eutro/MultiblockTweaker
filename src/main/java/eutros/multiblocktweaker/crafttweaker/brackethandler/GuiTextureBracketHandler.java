package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCTextureArea;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.ITextureArea;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.resources.TextureArea;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BracketHandler
@ZenRegister
public class GuiTextureBracketHandler implements IBracketHandler {
    private final static Map<String, ITextureArea> cache = new HashMap<>();

    private final IJavaMethod method;

    public GuiTextureBracketHandler() {
        this.method = CraftTweakerAPI.getJavaMethod(GuiTextureBracketHandler.class, "get", String.class);
    }

    @ZenMethod
    public static ITextureArea get(String member) {
        if (!cache.containsKey(member)) {
            TextureArea textureArea = ReflectionHelper.getStatic(GuiTextures.class, member);
            if (textureArea == null) {
                cache.put(member, ITextureArea.fullImage(member));
            } else {
                cache.put(member, new MCTextureArea(textureArea));
            }
        }
        return cache.get(member);
    }


    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if ((tokens.size() < 3)) return null;
        if (!tokens.get(0).getValue().equalsIgnoreCase("gt_texture")) return null;
        if (!tokens.get(1).getValue().equals(":")) return null;
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 2; i < tokens.size(); i++) {
            nameBuilder.append(tokens.get(i).getValue());
        }
        return position -> new ExpressionCallStatic(position, environment, method,
                new ExpressionString(position, nameBuilder.toString()));
    }
}
