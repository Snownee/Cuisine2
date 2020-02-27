package snownee.cuisine.api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public final class CuisineCommonConfig {

    public static final ForgeConfigSpec spec;

    public static boolean debug = true; //TODO change to false on release

    public static int multiblockMaxConnectionSize = 32;

    private static BooleanValue debugVal;

    private static IntValue multiblockMaxConnectionSizeVal;

    static {
        spec = new ForgeConfigSpec.Builder().configure(CuisineCommonConfig::new).getRight();
    }

    private CuisineCommonConfig(ForgeConfigSpec.Builder builder) {
        debugVal = builder.define("debugMode", debug);

        builder.push("base");
        multiblockMaxConnectionSizeVal = builder.defineInRange("multiblockMaxConnectionSize", multiblockMaxConnectionSize, 1, 128);
    }

    public static void refresh() {
        debug = debugVal.get();
        multiblockMaxConnectionSize = multiblockMaxConnectionSizeVal.get();
    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.Reloading event) {
        ((CommentedFileConfig) event.getConfig().getConfigData()).load();
        refresh();
    }
}
