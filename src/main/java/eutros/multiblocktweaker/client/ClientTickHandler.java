package eutros.multiblocktweaker.client;

import eutros.multiblocktweaker.MultiblockTweaker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT,
        modid = MultiblockTweaker.MOD_ID)
public class ClientTickHandler {

    public static int ticksInGame = 0;
    public static float partialTicks = 0;
    public static float total = 0;
    public static float delta = 0;

    private ClientTickHandler() {
    }

    private static void calcDelta() {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (!Minecraft.getMinecraft().isGamePaused()) {
                partialTicks = event.renderTickTime;
            }
        } else {
            calcDelta();
        }
    }

    @SubscribeEvent
    public static void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!Minecraft.getMinecraft().isGamePaused()) {
                ++ticksInGame;
                partialTicks = 0;
            }
        }
    }

}
