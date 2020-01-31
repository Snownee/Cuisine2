package snownee.cuisine;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.crafting.RecipeSpiceBottleFilling;
import snownee.cuisine.item.SpiceBottleItem;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;

import java.util.concurrent.atomic.AtomicInteger;

@KiwiModule(name = "base",dependencies = "@core")
@KiwiModule.Group("decorations")
@KiwiModule.Subscriber(KiwiModule.Subscriber.Bus.MOD)
public class BaseModule extends AbstractModule {
    public static final SpiceBottleItem SPICE_BOTTLE = new SpiceBottleItem(itemProp());

    public static IRecipeSerializer<?> recipeSpiceBottleFilling;

    @SubscribeEvent
    public void handleRegister(RegistryEvent.Register<IRecipeSerializer<?>> event) {
            IForgeRegistry<IRecipeSerializer<?>> registry =  event.getRegistry();
            registry.register(recipeSpiceBottleFilling = new RecipeSpiceBottleFilling.Serializer().setRegistryName(Cuisine.MODID, "spice_bottle_fill"));
    }

}
