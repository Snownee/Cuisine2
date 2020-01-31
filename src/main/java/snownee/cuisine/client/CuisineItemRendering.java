package snownee.cuisine.client;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import snownee.cuisine.BaseModule;
import snownee.cuisine.Cuisine;
import snownee.cuisine.api.CuisineAPI;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = Cuisine.MODID, value = Dist.CLIENT)
public class CuisineItemRendering {
    @SubscribeEvent
    public static void onItemColorsInit(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        AtomicInteger ret = new AtomicInteger(0xffffff);
        itemColors.register((stack, tintIndex) -> {
            if(tintIndex==1){
                CuisineAPI.findSpice(stack).ifPresent(i->{
                    String color = i.getColor();
                    System.out.println(color);
                    if (color!=null){
                        ret.set(Integer.parseInt(color, 16));
                    }
                });
            }
            return ret.get();
        }, BaseModule.SPICE_BOTTLE);
    }
}
