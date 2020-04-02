package snownee.kiwi.client;

import java.util.function.Predicate;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import snownee.kiwi.Kiwi;
import snownee.kiwi.client.element.NineSliceDrawable;

public enum DefaultDrawables implements ISelectiveResourceReloadListener {

    INSTANCE;

    public static final ResourceLocation SLOT_LOCATION = new ResourceLocation(Kiwi.MODID, "textures/gui/slot.png");
    public static final ResourceLocation PANEL_LOCATION = new ResourceLocation(Kiwi.MODID, "textures/gui/panel.png");

    private static NineSliceDrawable slot;
    private static NineSliceDrawable panel;

    public static NineSliceDrawable getSlot() {
        return slot;
    }

    public static NineSliceDrawable getPanel() {
        return panel;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        KiwiSpriteUploader atlas = KiwiSpriteUploader.GUI_ATLAS;
        TextureAtlasSprite slotSprite = atlas.getSprite(SLOT_LOCATION);
        slot = new NineSliceDrawable(slotSprite, 3, 3, 1, 2, 1, 2, DrawMode.STRETCH);
        TextureAtlasSprite panelSprite = atlas.getSprite(PANEL_LOCATION);
        panel = new NineSliceDrawable(panelSprite, 9, 9, 4, 5, 4, 5, DrawMode.STRETCH);
    }
}
