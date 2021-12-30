#loader preinit // VERY IMPORTANT!!!!

import mods.gregtech.render.ITextureArea;
import mods.gregtech.render.ICubeRenderer;
import mods.gregtech.renderer.OverlayFace;
import crafttweaker.world.IFacing;
import mods.gregtech.ISound;

/**
// This example will show you how to register resources, such as textures, renderers, sounds...
// Since they must be registered in advance (except ITextureArea), you should always make sure to register them in the preinit phase.
**/

print("register MBT ITextureArea"); // You don't actually need to register ITextureArea.
var progress_bar_energy = ITextureArea.fullImage("progress_bar_energy", "multiblocktweaker:textures/gui/progress_bar_energy.png") as ITextureArea; // assets/___/___; (file) dont foget .png

print("register MBT ICubeRenderer");
var full_renderer = ICubeRenderer.simpleOverlay("full_renderer", "multiblocktweaker:full/full") as ICubeRenderer; // assets/___/textures/blocks/___; (file)
var sided_renderer = ICubeRenderer.sidedOverlay("sided_renderer",
    {
        IFacing.up() : "multiblocktweaker:blocks/sided/up", // assets/___/textures/___; (file) !!!!! no blocks
        IFacing.north() : "multiblocktweaker:blocks/sided/north"
    } as string[IFacing]
) as ICubeRenderer;
var working_state_renderer = ICubeRenderer.orientedOverlay("working_state_renderer", "multiblocktweaker:state", OverlayFace.FRONT) as ICubeRenderer; // assets/___/textures/blocks/___; (folder)

print("register MBT ISound"); // NOTE!!!! also register sound info in the sound.json (modid/sounds.json)
var sound = ISound.registerSound("multiblocktweaker:tick.sound") as ISound;

// How to refer them?
// You can use global or static variables of CT. However, we recommend using the following bracket way.

progress_bar_energy = <texture_area:progress_bar_energy>;

full_renderer = <cube_renderer:full_renderer>;

sided_renderer = <cube_renderer:sided_renderer>;

working_state_renderer = <cube_renderer:working_state_renderer>;

sound = <sound:multiblocktweaker:tick.sound>;