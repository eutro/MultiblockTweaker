package eutros.multiblocktweaker.crafttweaker.brackethandler;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import eutros.multiblocktweaker.crafttweaker.gtwrap.impl.MCMetaTileEntity;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IMetaTileEntity;
import eutros.multiblocktweaker.helper.ReflectionHelper;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BracketHandler
@ZenClass("mods.gregtech.MetaTileEntities")
@ZenRegister
public class MetaTileEntityBracketHandler implements IBracketHandler {
    private final static IMetaTileEntity[] EMPTY = new IMetaTileEntity[0];
    private final static Map<String, IMetaTileEntity[]> cache = new HashMap<>();

    private final IJavaMethod method;

    public MetaTileEntityBracketHandler() {
        this.method = CraftTweakerAPI.getJavaMethod(
                MetaTileEntityBracketHandler.class, "get", String.class);
    }
    
    @ZenMethod
    @ZenMemberGetter()
    public static IMetaTileEntity[] get(String member) {
        if (!cache.containsKey(member)) {
            Object mte = ReflectionHelper.getStatic(MetaTileEntities.class, member);
            if (mte instanceof MetaTileEntity) {
                cache.put(member, new IMetaTileEntity[]{new MCMetaTileEntity((MetaTileEntity)mte)});
            } else if (mte instanceof MetaTileEntity[]){
                cache.put(member, Arrays.stream((MetaTileEntity[]) mte).map(MCMetaTileEntity::new).toArray(IMetaTileEntity[]::new));
            }
            cache.put(member, EMPTY);
        }
        return cache.get(member);
    }


    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if ((tokens.size() < 3)) return null;
        if (!tokens.get(0).getValue().equalsIgnoreCase("mte")) return null;
        if (!tokens.get(1).getValue().equals(":")) return null;
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 2; i < tokens.size(); i++) {
            nameBuilder.append(tokens.get(i).getValue());
        }
        return position -> new ExpressionCallStatic(position, environment, method,
                new ExpressionString(position, nameBuilder.toString()));
    }
}
