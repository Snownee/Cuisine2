package snownee.kiwi.ui;

import mezz.jei.events.EventBusHelper;
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
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.KiwiSpriteUploader;

@KiwiModule(name = "ui")
public final class UIModule extends AbstractModule {
    public UIModule() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> clientStart(modEventBus));
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientStart(IEventBus modEventBus) {
        EventBusHelper.addListener(modEventBus, ColorHandlerEvent.Block.class, setupEvent -> {
            Minecraft minecraft = Minecraft.getInstance();
            IResourceManager resourceManager = minecraft.getResourceManager();
            if (resourceManager instanceof IReloadableResourceManager) {
                ((IReloadableResourceManager) resourceManager).addReloadListener(KiwiSpriteUploader.GUI_ATLAS);
                ((IReloadableResourceManager) resourceManager).addReloadListener(DefaultDrawables.INSTANCE);
            }

            KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.SLOT_LOCATION);
            KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.PANEL_LOCATION);
            KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_LOCATION);
            KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_ACTIVE_LOCATION);
            KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.BTN_DISABLED_LOCATION);
        });
    }

}
