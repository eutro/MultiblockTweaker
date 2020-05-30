package eutros.multiblocktweaker.jei;

import gregtech.api.render.scene.WorldSceneRenderer;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2d;

public class CustomInfoRecipeWrapper extends MultiblockInfoRecipeWrapper {

    private static final double SCALE_PER_PIXEL = 1.05;
    private int lastX;
    private int lastY;
    private double scale = 1;

    private Vector2d offset = new Vector2d();

    public CustomInfoRecipeWrapper(MultiblockInfoPage infoPage) {
        super(infoPage);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
        boolean inScene = mouseX >= 0 && mouseY >= 0 && mouseX < recipeWidth && mouseY < recipeWidth;

        if(inScene) {
            if(Mouse.isButtonDown(1)) {
                int deltaY = lastY - mouseY;
                if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    scale *= Math.pow(SCALE_PER_PIXEL, deltaY);
                } else {
                    offset.x += lastX - mouseX;
                    offset.y += deltaY;
                }
            }
        }

        lastX = mouseX;
        lastY = mouseY;
    }

    @Override
    public void preRenderScene(WorldSceneRenderer renderer) {
        double offsetScale = 0.5;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(offset.x * offsetScale, offset.y * offsetScale, 0);
        super.preRenderScene(renderer);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        super.getIngredients(ingredients);
        resetTransforms();
    }

    private void resetTransforms() {
        offset.set(0, 0);
        scale = 1;
    }

}
