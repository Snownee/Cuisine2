package snownee.cuisine.client;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.item.SpiceBottleItem;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public final class CuisineItemRendering {

    private CuisineItemRendering() {}

    @SubscribeEvent
    public static void onItemColorsInit(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> {
            if (tintIndex == 0) { //FIXME:CHACK ME
                return SpiceBottleItem.getSpice(stack).map(ColorLookup::get).orElse(-1);
            }
            return -1;
        }, BaseModule.SPICE_BOTTLE);
    }
}
