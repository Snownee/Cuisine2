package snownee.kiwi.client;

import java.util.function.Predicate;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import snownee.kiwi.Kiwi;
import snownee.kiwi.client.element.NineSliceDrawable;

public enum DefaultDrawables implements ISelectiveResourceReloadListener {

    INSTANCE;

    public static final ResourceLocation SLOT_LOCATION = new ResourceLocation(Kiwi.MODID, "slot");
    public static final ResourceLocation PANEL_LOCATION = new ResourceLocation(Kiwi.MODID, "panel");
    public static final ResourceLocation BTN_LOCATION = new ResourceLocation(Kiwi.MODID, "btn");
    public static final ResourceLocation BTN_ACTIVE_LOCATION = new ResourceLocation(Kiwi.MODID, "btn_active");
    public static final ResourceLocation BTN_DISABLED_LOCATION = new ResourceLocation(Kiwi.MODID, "btn_disabled");

    private static NineSliceDrawable slot;
    private static NineSliceDrawable panel;
    private static NineSliceDrawable btn;
    private static NineSliceDrawable btnActive;
    private static NineSliceDrawable btnDisabled;

    public static NineSliceDrawable getSlot() {
        return slot;
    }

    public static NineSliceDrawable getPanel() {
        return panel;
    }

    public static NineSliceDrawable getBtn() {
        return btn;
    }

    public static NineSliceDrawable getBtnActive() {
        return btnActive;
    }

    public static NineSliceDrawable getBtnDisabled() {
        return btnDisabled;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (!resourcePredicate.test(VanillaResourceType.TEXTURES)) {
            return;
        }
        KiwiSpriteUploader atlas = KiwiSpriteUploader.GUI_ATLAS;
        TextureAtlasSprite slotSprite = atlas.getSprite(SLOT_LOCATION);
        slot = new NineSliceDrawable(slotSprite, 3, 3, 1, 1, 1, 1, DrawMode.STRETCH);
        TextureAtlasSprite panelSprite = atlas.getSprite(PANEL_LOCATION);
        panel = new NineSliceDrawable(panelSprite, 9, 9, 4, 4, 4, 4, DrawMode.STRETCH);
        TextureAtlasSprite btnSprite = atlas.getSprite(BTN_LOCATION);
        btn = new NineSliceDrawable(btnSprite, 20, 20, 2, 2, 2, 3, DrawMode.REPEAT);
        TextureAtlasSprite btnActiveSprite = atlas.getSprite(BTN_ACTIVE_LOCATION);
        btnActive = new NineSliceDrawable(btnActiveSprite, 20, 20, 2, 2, 2, 3, DrawMode.REPEAT);
        TextureAtlasSprite btnDisabledSprite = atlas.getSprite(BTN_DISABLED_LOCATION);
        btnDisabled = new NineSliceDrawable(btnDisabledSprite, 20, 20, 1, 1, 1, 1, DrawMode.REPEAT);
    }
}
