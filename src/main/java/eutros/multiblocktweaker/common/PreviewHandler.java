package eutros.multiblocktweaker.common;

import eutros.multiblocktweaker.client.PreviewRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PreviewHandler {

    private PreviewHandler() {
    }

    public static void init() {
        if(Loader.isModLoaded("jei"))
            MinecraftForge.EVENT_BUS.register(new PreviewHandler());
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent evt) {
        if(!(evt instanceof PlayerInteractEvent.LeftClickBlock || evt instanceof PlayerInteractEvent.RightClickBlock))
            return;

        EntityPlayer player = evt.getEntityPlayer();
        if(evt.getSide().isServer() || !evt.getItemStack().isEmpty() || evt.getHand() == EnumHand.OFF_HAND || !player.isSneaking()) return;

        if(PreviewRenderer.INSTANCE.onUse(evt.getWorld(), evt.getPos(), evt instanceof PlayerInteractEvent.RightClickBlock)) {
            player.swingArm(EnumHand.MAIN_HAND);
        }
    }

}
