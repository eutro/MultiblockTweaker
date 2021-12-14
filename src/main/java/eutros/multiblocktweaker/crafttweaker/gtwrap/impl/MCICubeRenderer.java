package eutros.multiblocktweaker.crafttweaker.gtwrap.impl;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import eutros.multiblocktweaker.crafttweaker.gtwrap.interfaces.IICubeRenderer;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class MCICubeRenderer implements IICubeRenderer {

    @NotNull
    protected final ICubeRenderer cube;

    public MCICubeRenderer(@NotNull ICubeRenderer delegate) {
        this.cube = delegate;
    }

    @Override
    public void render(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        this.cube.render(renderState, translation, pipeline);
    }

    @Override
    public TextureAtlasSprite getParticleSprite() {
        return this.cube.getParticleSprite();
    }

    @Override
    public void render(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds) {
        this.cube.render(renderState, translation, pipeline, bounds);
    }

    @Override
    public void renderSided(EnumFacing side, CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        this.cube.renderSided(side, renderState, translation, pipeline);
    }

    @Override
    public void renderSided(EnumFacing side, Cuboid6 bounds, CCRenderState renderState, IVertexOperation[] pipeline, Matrix4 translation) {
        this.cube.renderSided(side, bounds, renderState, pipeline, translation);
    }

    @Override
    public void renderOriented(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds, EnumFacing frontFacing) {
        this.cube.renderOriented(renderState, translation, pipeline, bounds, frontFacing);
    }

    @Override
    public void renderOriented(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, EnumFacing frontFacing) {
        this.cube.renderOriented(renderState, translation, pipeline, frontFacing);
    }

    @Override
    public void renderOrientedState(CCRenderState ccRenderState, Matrix4 matrix4, IVertexOperation[] iVertexOperations, Cuboid6 cuboid6, EnumFacing enumFacing, boolean b, boolean b1) {
        this.cube.renderOrientedState(ccRenderState, matrix4, iVertexOperations, cuboid6, enumFacing, b, b1);
    }

    @Override
    public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, EnumFacing frontFacing, boolean isActive, boolean isWorkingEnabled) {
        this.cube.renderOrientedState(renderState, translation, pipeline, frontFacing, isActive, isWorkingEnabled);
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        this.cube.registerIcons(textureMap);
    }
}
