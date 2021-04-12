package eutros.multiblocktweaker.gregtech.cuberenderer;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.google.common.base.Preconditions;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SidedCubeRenderer implements ICubeRenderer {

    private Map<EnumFacing, ResourceLocation> sides;
    private Map<EnumFacing, TextureAtlasSprite> sprites;
    private TextureAtlasSprite particles;

    public SidedCubeRenderer(Map<EnumFacing, ResourceLocation> sides) {
        if (FMLCommonHandler.instance().getSide().isServer())
            return;
        this.sides = sides;
        if (MapHolder.map != null) {
            sprites = sides.keySet().stream()
                    .collect(Collectors.toMap(Function.identity(),
                            r -> MapHolder.map.getAtlasSprite(sides.get(r).toString())));
            particles = sprites.get(EnumFacing.UP);
        } else {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public static <T> EnumMap<EnumFacing, T> fillBlanks(Map<EnumFacing, T> map) {
        Preconditions.checkNotNull(
                map.get(EnumFacing.UP),
                "UP side has no texture! " +
                "Consider using mods.gregtech.multiblock.Builder#withTexture to specify the texture explicitly."
        );
        EnumMap<EnumFacing, T> retMap = new EnumMap<>(map);

        retMap.computeIfAbsent(EnumFacing.DOWN, k -> retMap.get(EnumFacing.UP));

        retMap.computeIfAbsent(EnumFacing.NORTH,
                a -> retMap.computeIfAbsent(EnumFacing.EAST,
                        b -> retMap.computeIfAbsent(EnumFacing.SOUTH,
                                c -> retMap.computeIfAbsent(EnumFacing.WEST, k -> retMap.get(EnumFacing.UP)))));

        retMap.computeIfAbsent(EnumFacing.WEST, k -> retMap.get(EnumFacing.NORTH));
        retMap.computeIfAbsent(EnumFacing.SOUTH, k -> retMap.get(EnumFacing.NORTH));
        retMap.computeIfAbsent(EnumFacing.EAST, k -> retMap.get(EnumFacing.NORTH));
        return retMap;
    }

    public SidedCubeRenderer(IBlockState state) {
        if (FMLCommonHandler.instance().getSide().isServer())
            return;
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = brd.getModelForState(state);
        long rand = new Random().nextLong();
        particles = model.getParticleTexture();
        sprites = fillBlanks(
                Stream.concat(Stream.of(new EnumFacing[] { null }),
                        Arrays.stream(EnumFacing.values()))
                        .map(f -> model.getQuads(state, f, rand))
                        .map(List::stream)
                        .map(Stream::findFirst)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toMap(BakedQuad::getFace, BakedQuad::getSprite))
        );
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void preStitch(TextureStitchEvent.Pre evt) {
        sprites = sides.keySet().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        f -> evt.getMap().registerSprite(sides.get(f))));
        particles = sprites.get(EnumFacing.UP);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getParticleSprite() {
        return particles;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(CCRenderState state, Matrix4 translate, IVertexOperation[] ops, Cuboid6 cuboid) {
        for (EnumFacing side : EnumFacing.values()) {
            Textures.renderFace(state, translate, ops, side, cuboid, sprites.get(side));
        }
    }

}
