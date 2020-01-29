package snownee.cuisine;

import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.data.CuisineDataManager;
import snownee.cuisine.impl.bonus.EffectsBonus;
import snownee.cuisine.impl.bonus.NewMaterialBonus;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.KiwiModule.Subscriber.Bus;

@KiwiModule(name = "core")
@KiwiModule.Subscriber(value = Bus.FORGE)
public final class CoreModule extends AbstractModule {

    private CuisineDataManager<Material> materialManager;
    private CuisineDataManager<Spice> spiceManager;
    private CuisineDataManager<CuisineFood> foodManager;
    private CuisineDataManager<CuisineRecipe> recipeManager;

    @Override
    protected void preInit() {
        CuisineRegistries.class.hashCode();

        CuisineAPI.registerBonusAdapter("effect", new EffectsBonus.Adapter());
        CuisineAPI.registerBonusAdapter("new_material", new NewMaterialBonus.Adapter());
    }

    @SubscribeEvent
    protected void serverInit(FMLServerAboutToStartEvent event) {
        if (materialManager == null) {
            materialManager = new CuisineDataManager("cuisine_material", CuisineRegistries.MATERIALS);
            spiceManager = new CuisineDataManager("cuisine_spice", CuisineRegistries.SPICES);
            foodManager = new CuisineDataManager("cuisine_food", CuisineRegistries.FOODS);
            recipeManager = new CuisineDataManager("cuisine_recipe", CuisineRegistries.RECIPES);
        }
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener(materialManager);
        manager.addReloadListener(spiceManager);
        manager.addReloadListener(foodManager);
        manager.addReloadListener(recipeManager);
    }
}
