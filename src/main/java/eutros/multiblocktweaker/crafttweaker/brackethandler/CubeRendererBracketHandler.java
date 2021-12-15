package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import stanhebben.zenscript.annotations.ZenClass;
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
@ZenClass("mods.gregtech.render.CubeRenderers")
@ZenRegister
public class CubeRendererBracketHandler implements IBracketHandler {
    private final static Map<String, IICubeRenderer> cache = new HashMap<>();
    private final IJavaMethod method;

    public CubeRendererBracketHandler() {
        this.method = CraftTweakerAPI.getJavaMethod(CubeRendererBracketHandler.class, "get", String.class);
    }

    @ZenMethod
    public static IICubeRenderer get(String member) {
        if (!cache.containsKey(member)) {
            ICubeRenderer cubeRenderer = ReflectionHelper.getStatic(Textures.class, member);
            cache.put(member, cubeRenderer == null ? null : new MCICubeRenderer(cubeRenderer));
        }
        return cache.get(member);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if ((tokens.size() < 3)) return null;
        if (!tokens.get(0).getValue().equalsIgnoreCase("cube_renderer")) return null;
        if (!tokens.get(1).getValue().equals(":")) return null;
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 2; i < tokens.size(); i++) {
            nameBuilder.append(tokens.get(i).getValue());
        }
        return position -> new ExpressionCallStatic(position, environment, method,
                new ExpressionString(position, nameBuilder.toString()));
    }
}
