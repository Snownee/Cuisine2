package snownee.cuisine;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(bus = Bus.MOD)
public final class CuisineCommonConfig {

    static final ForgeConfigSpec spec;

    static {
        spec = new ForgeConfigSpec.Builder().configure(CuisineCommonConfig::new).getRight();
    }

    private CuisineCommonConfig(ForgeConfigSpec.Builder builder) {

    }

    public static void refresh() {

    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.Reloading event) {
        ((CommentedFileConfig) event.getConfig().getConfigData()).load();
        refresh();
    }
}
