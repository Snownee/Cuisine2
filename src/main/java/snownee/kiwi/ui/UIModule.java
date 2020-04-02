package snownee.kiwi.ui;

import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.KiwiSpriteUploader;

@KiwiModule(name = "ui")
public final class UIModule extends AbstractModule {
    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientInit(FMLClientSetupEvent event) {
        IResourceManager resourceManager = event.getMinecraftSupplier().get().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener(KiwiSpriteUploader.GUI_ATLAS);
        }

        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.SLOT_LOCATION);
        KiwiSpriteUploader.GUI_ATLAS.registerSprite(DefaultDrawables.PANEL_LOCATION);
    }
}
