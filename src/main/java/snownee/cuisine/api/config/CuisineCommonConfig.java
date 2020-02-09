package snownee.cuisine.api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public final class CuisineCommonConfig {

    public static final ForgeConfigSpec spec;

    public static boolean debug = true; //TODO change to false on release

    private static BooleanValue debugVal;

    static {
        spec = new ForgeConfigSpec.Builder().configure(CuisineCommonConfig::new).getRight();
    }

    private CuisineCommonConfig(ForgeConfigSpec.Builder builder) {
        debugVal = builder.define("debugMode", debug);
    }

    public static void refresh() {
        debug = debugVal.get();
    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.Reloading event) {
        ((CommentedFileConfig) event.getConfig().getConfigData()).load();
        refresh();
    }
}
