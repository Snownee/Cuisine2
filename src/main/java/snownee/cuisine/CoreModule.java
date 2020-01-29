package snownee.cuisine;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tags.Tag;
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
            materialManager = new CuisineDataManager("cuisine_material", CuisineRegistries.MATERIALS).setCallback(CoreModule::buildMaterialMap);
            spiceManager = new CuisineDataManager("cuisine_spice", CuisineRegistries.SPICES).setCallback(CoreModule::buildSpiceMap);
            foodManager = new CuisineDataManager("cuisine_food", CuisineRegistries.FOODS).setCallback(CoreModule::buildFoodMap);
            recipeManager = new CuisineDataManager("cuisine_recipe", CuisineRegistries.RECIPES);
        }
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener(materialManager);
        manager.addReloadListener(spiceManager);
        manager.addReloadListener(foodManager);
        manager.addReloadListener(recipeManager);
    }

    static Map<Item, Material> item2Material = Maps.newHashMap();
    static Map<Item, Spice> item2Spice = Maps.newHashMap();
    static Map<Fluid, Spice> fluid2Spice = Maps.newHashMap();
    static BiMap<Item, CuisineFood> item2Food = HashBiMap.create();
    static BiMap<Block, CuisineFood> block2Food = HashBiMap.create();

    private static void buildMaterialMap() {
        item2Material.clear();
        for (Material material : CuisineRegistries.MATERIALS.getValues()) {
            for (Item item : material.getItems()) {
                item2Material.put(item, material);
            }
            for (Tag<Item> tag : material.getTags()) {
                for (Item item : tag.getAllElements()) {
                    item2Material.put(item, material);
                }
            }
        }
    }

    private static void buildSpiceMap() {
        item2Spice.clear();
        fluid2Spice.clear();
        for (Spice spice : CuisineRegistries.SPICES.getValues()) {
            for (Item item : spice.getItems()) {
                item2Spice.put(item, spice);
            }
            for (Tag<Item> tag : spice.getTags()) {
                for (Item item : tag.getAllElements()) {
                    item2Spice.put(item, spice);
                }
            }
            for (Fluid fluid : spice.getFluids()) {
                fluid2Spice.put(fluid, spice);
            }
            for (Tag<Fluid> tag : spice.getFluidTags()) {
                for (Fluid fluid : tag.getAllElements()) {
                    fluid2Spice.put(fluid, spice);
                }
            }
        }
    }

    private static void buildFoodMap() {
        item2Food.clear();
        block2Food.clear();
        for (CuisineFood food : CuisineRegistries.FOODS.getValues()) {
            if (food.getItem() != null) {
                item2Food.put(food.getItem(), food);
            }
            if (food.getBlock() != null) {
                block2Food.put(food.getBlock(), food);
            }
        }
    }
}
