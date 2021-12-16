package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import eutros.multiblocktweaker.crafttweaker.CustomMultiblock;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCICubeRenderer;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import eutros.multiblocktweaker.gregtech.recipes.CustomRecipeProperty;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.client.renderer.texture.Textures;
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
public class RecipePropertyBracketHandler implements IBracketHandler {
    private final static Map<String, CustomRecipeProperty> cache = new HashMap<>();

    private final IJavaMethod method;

    public RecipePropertyBracketHandler() {
        this.method = CraftTweakerAPI.getJavaMethod(RecipePropertyBracketHandler.class, "get", String.class);
    }

    @ZenMethod
    public static CustomRecipeProperty get(String member) {
        if (!cache.containsKey(member)) {
            cache.put(member, new CustomRecipeProperty(member));
        }
        return cache.get(member);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if ((tokens.size() < 3)) return null;
        if (!tokens.get(0).getValue().equalsIgnoreCase("recipe_property")) return null;
        if (!tokens.get(1).getValue().equals(":")) return null;
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 2; i < tokens.size(); i++) {
            nameBuilder.append(tokens.get(i).getValue());
        }
        return position -> new ExpressionCallStatic(position, environment, method,
                new ExpressionString(position, nameBuilder.toString()));
    }
}
