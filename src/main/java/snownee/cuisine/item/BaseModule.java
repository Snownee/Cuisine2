package snownee.cuisine.item;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import snownee.cuisine.Cuisine;
import snownee.cuisine.crafting.RecipeSpiceBottleFilling;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiModule;

@KiwiModule(name = "base",dependencies = "@core")
@KiwiModule.Group("decorations")
@KiwiModule.Subscriber(KiwiModule.Subscriber.Bus.MOD)
public class BaseModule extends AbstractModule {
    public static final ItemSpiceBottle SPICE_BOTTLE = new ItemSpiceBottle(itemProp());

    public static IRecipeSerializer<?> recipeSpiceBottleFilling;

    @SubscribeEvent
    public void handleRegister(RegistryEvent.Register<IRecipeSerializer<?>> event) {
            IForgeRegistry<IRecipeSerializer<?>> registry =  event.getRegistry();
            registry.register(recipeSpiceBottleFilling = new RecipeSpiceBottleFilling.Serializer().setRegistryName(Cuisine.MODID, "spice_bottle_fill"));
    }
}
