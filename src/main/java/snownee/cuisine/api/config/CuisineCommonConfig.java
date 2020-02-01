package snownee.cuisine.api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public final class CuisineCommonConfig {

    public static final ForgeConfigSpec spec;

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
