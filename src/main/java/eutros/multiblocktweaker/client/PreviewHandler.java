package eutros.multiblocktweaker.client;

import gregtech.api.block.machines.BlockMachine;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.annotation.Nullable;

public class PreviewHandler {

    private PreviewHandler() {
    }

    public static void init() {
        if (Loader.isModLoaded("jei"))
            MinecraftForge.EVENT_BUS.register(new PreviewHandler());
    }

    @Nullable
    public static MultiblockControllerBase getMetaController(World world, @Nullable BlockPos targetPos) {
        if (targetPos == null) return null;
        MetaTileEntity mte = BlockMachine.getMetaTileEntity(world, targetPos);
        if (!(mte instanceof MultiblockControllerBase)) {
            return null;
        }
        return (MultiblockControllerBase) mte;
    }

    @SubscribeEvent
    public void onMouse(InputEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();

        boolean left = false;
        boolean right = false;
        if (mc.gameSettings.keyBindAttack.isPressed()) {
            ++mc.gameSettings.keyBindAttack.pressTime;
            left = true;
        } else if (mc.gameSettings.keyBindUseItem.isPressed()) {
            ++mc.gameSettings.keyBindUseItem.pressTime;
            right = true;
        } else return;

        if (mc.world == null
            || mc.player == null
            || mc.objectMouseOver == null
            || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK
            || getMetaController(mc.world, mc.objectMouseOver.getBlockPos()) == null
            || (!mc.player.getHeldItemMainhand().isEmpty() || !mc.player.isSneaking())
            || !PreviewRenderer.INSTANCE.onUse(mc.world, mc.objectMouseOver.getBlockPos(), right)) return;

        mc.player.swingArm(EnumHand.MAIN_HAND);

        if (left) unPress(mc.gameSettings.keyBindAttack);
        if (right) unPress(mc.gameSettings.keyBindUseItem);
    }

    private void unPress(KeyBinding key) {
        KeyBinding.setKeyBindState(key.getKeyCode(), false);
        key.pressTime = 0;
    }

}
