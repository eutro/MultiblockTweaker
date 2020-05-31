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
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        // NORTH

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        tes.draw();

        // SOUTH

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();

        tes.draw();

        // WEST

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        tes.draw();

        // EAST

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();

        tes.draw();

        // TOP

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        tes.draw();

        // BOTTOM

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buff.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        buff.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();

        tes.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

}
