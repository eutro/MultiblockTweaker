package eutros.multiblocktweaker.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static void drawColouredAABB(AxisAlignedBB aabb, float r, float g, float b, float a) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buff = tes.getBuffer();

        GlStateManager.disableTexture2D();
        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // NORTH
        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        // SOUTH
        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();

        // WEST
        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        // EAST
        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();

        // TOP
        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        // BOTTOM
        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();

        tes.draw();
        GlStateManager.enableTexture2D();
    }

}
