package snownee.kiwi.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.KiwiModule.Subscriber.Bus;
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.KiwiSpriteUploader;
import snownee.kiwi.ui.data.KXMLLoader;

@KiwiModule(name = "ui")
@KiwiModule.Subscriber(Bus.FORGE)
public final class UIModule extends AbstractModule {

    public static final KXMLLoader KXML_LOADER = new KXMLLoader("kiwi_ui");

    public UIModule() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(UIModule::clientStart));
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientStart(ColorHandlerEvent.Block event) {
        Minecraft minecraft = Minecraft.getInstance();
        IResourceManager resourceManager = minecraft.getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener(KiwiSpriteUploader.GUI_ATLAS);
            ((IReloadableResourceManager) resourceManager).addReloadListener(DefaultDrawables.INSTANCE);
            ((IReloadableResourceManager) resourceManager).addReloadListener(KXML_LOADER);
        }

        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.SLOT_LOCATION);
        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.PANEL_LOCATION);
        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_LOCATION);
        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_ACTIVE_LOCATION);
        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_DISABLED_LOCATION);
    }

}
