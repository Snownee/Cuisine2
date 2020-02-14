package snownee.cuisine;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.config.CuisineClientConfig;
import snownee.cuisine.api.config.CuisineCommonConfig;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.cap.CuisineCapabilitiesInternal;
import snownee.cuisine.data.CuisineDataManager;
import snownee.cuisine.data.CuisineRecipeManager;
import snownee.cuisine.data.DeferredReloadListener;
import snownee.cuisine.data.DeferredReloadListener.LoadingStage;
import snownee.cuisine.data.network.SSyncRecordPacket;
import snownee.cuisine.data.network.SSyncRegistryPacket;
import snownee.cuisine.data.network.SSyncTagsPacket;
import snownee.cuisine.data.research.ResearchData;
import snownee.cuisine.data.tag.CuisineNetworkTagManager;
import snownee.cuisine.debug.DebugItemGroup;
import snownee.cuisine.impl.bonus.EffectsBonus;
import snownee.cuisine.impl.bonus.NewMaterialBonus;
import snownee.cuisine.impl.rule.CountRegistryRecipeRule;
import snownee.cuisine.util.ForgeRegistryArgument;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.network.NetworkChannel;
import snownee.kiwi.util.Util;

@KiwiModule(name = "core")
@KiwiModule.Subscriber
public final class CoreModule extends AbstractModule {

    static CuisineDataManager<Material> materialManager;
    static CuisineDataManager<Spice> spiceManager;
    static CuisineDataManager<CuisineFood> foodManager;
    static CuisineRecipeManager recipeManager;

    static CuisineNetworkTagManager networkTagManager;

    static ResearchData researchData = new ResearchData();

    public CoreModule() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CuisineCommonConfig.spec);
        modEventBus.register(CuisineCommonConfig.class);
        if (FMLEnvironment.dist.isClient()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CuisineClientConfig.spec);
            modEventBus.register(CuisineClientConfig.class);
        }
    }

    @Override
    protected void preInit() {
        if (Kiwi.isLoaded(Util.RL("kiwi:test"))) {
            CuisineCommonConfig.debug = true;
        }

        CuisineRegistries.class.hashCode();

        NetworkChannel.register(SSyncRegistryPacket.class, new SSyncRegistryPacket.Handler());
        NetworkChannel.register(SSyncTagsPacket.class, new SSyncTagsPacket.Handler());
        NetworkChannel.register(SSyncRecordPacket.class, new SSyncRecordPacket.Handler());

        networkTagManager = new CuisineNetworkTagManager();

        CuisineAPI.registerBonusAdapter("effect", new EffectsBonus.Adapter());
        CuisineAPI.registerBonusAdapter("new_material", new NewMaterialBonus.Adapter());

        CuisineAPI.registerRecipeRuleAdapter("material", new CountRegistryRecipeRule.Adapter<>(networkTagManager.getMaterials(), CuisineRegistries.MATERIALS));
        CuisineAPI.registerRecipeRuleAdapter("spice", new CountRegistryRecipeRule.Adapter<>(networkTagManager.getSpices(), CuisineRegistries.SPICES));
        CuisineAPI.registerRecipeRuleAdapter("food", new CountRegistryRecipeRule.Adapter<>(null, CuisineRegistries.SPICES));
    }

    @Override
    protected void init(FMLCommonSetupEvent event) {
        CuisineCommonConfig.refresh();
        CuisineCapabilitiesInternal.register();

        if (CuisineCommonConfig.debug) {
            DebugItemGroup.init();
        }

        ArgumentTypes.register("cuisine:registry", ForgeRegistryArgument.class, (IArgumentSerializer) new ForgeRegistryArgument.Serializer());
    }

    @Override
    protected void clientInit(FMLClientSetupEvent event) {
        CuisineClientConfig.refresh();
    }

    @Override
    protected void serverInit(FMLServerStartingEvent event) {
        event.getServer().getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(() -> researchData, researchData.getName());

        LiteralArgumentBuilder<CommandSource> builder = CuisineCommand.init(event.getCommandDispatcher());
        event.getCommandDispatcher().register(builder);
    }

    @SubscribeEvent
    protected void serverStopped(FMLServerStoppedEvent event) {
        Cuisine.server = null;
    }

    @SubscribeEvent
    protected void serverInit(FMLServerAboutToStartEvent event) {
        Cuisine.server = event.getServer();
        if (materialManager == null) {
            materialManager = new CuisineDataManager("cuisine_material", CuisineRegistries.MATERIALS).setCallback(CoreModule::buildMaterialMap);
            spiceManager = new CuisineDataManager("cuisine_spice", CuisineRegistries.SPICES).setCallback(CoreModule::buildSpiceMap);
            foodManager = new CuisineDataManager("cuisine_food", CuisineRegistries.FOODS).setCallback(CoreModule::buildFoodMap);
            recipeManager = new CuisineRecipeManager("cuisine_recipe", CuisineRegistries.RECIPES).setVerifier(CuisineRecipe::validate);
        }
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, materialManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, spiceManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, foodManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.TAG, networkTagManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.RECIPE, recipeManager);
        manager.addReloadListener(DeferredReloadListener.INSTANCE);
    }

    //TODO Snownee: will we keep the insert order?
    static Map<Item, Material> item2Material = Maps.newIdentityHashMap();
    static Map<Item, Spice> item2Spice = Maps.newIdentityHashMap();
    static Map<Fluid, Spice> fluid2Spice = Maps.newIdentityHashMap();
    static Map<Item, CuisineFood> item2Food = Maps.newIdentityHashMap();
    static Map<Block, CuisineFood> block2Food = Maps.newIdentityHashMap();

    public static void buildMaterialMap() {
        item2Material.clear();
        for (Material material : CuisineRegistries.MATERIALS.getValues()) {
            for (Item item : material.getItems()) {
                item2Material.put(item, material);
            }
            for (Tag<Item> tag : material.getItemTags()) {
                for (Item item : tag.getAllElements()) {
                    item2Material.put(item, material);
                }
            }
        }
        DeferredReloadListener.INSTANCE.complete(CuisineRegistries.MATERIALS);
    }

    public static void buildSpiceMap() {
        item2Spice.clear();
        fluid2Spice.clear();
        for (Spice spice : CuisineRegistries.SPICES.getValues()) {
            for (Item item : spice.getItems()) {
                item2Spice.put(item, spice);
            }
            for (Tag<Item> tag : spice.getItemTags()) {
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
        DeferredReloadListener.INSTANCE.complete(CuisineRegistries.SPICES);
    }

    public static void buildFoodMap() {
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

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Cuisine.logger.info("Syncing...");
        sync((ServerPlayerEntity) event.getPlayer());
    }

    public static void sync(ServerPlayerEntity player) {
        new SSyncRegistryPacket(CuisineRegistries.MATERIALS).send(player);
        new SSyncRegistryPacket(CuisineRegistries.SPICES).send(player);
        new SSyncRegistryPacket(CuisineRegistries.FOODS).send(player);
        new SSyncTagsPacket(networkTagManager).send(player);
        new SSyncRegistryPacket(CuisineRegistries.RECIPES).send(player);
    }

    public static void setNetworkTagManager(CuisineNetworkTagManager networkTagManager) {
        CoreModule.networkTagManager = networkTagManager;
    }

    public static Collection<Item> getAllMaterialItems() {
        return Collections.unmodifiableCollection(item2Material.keySet());
    }

    public static Collection<Item> getAllSpiceItems() {
        return Collections.unmodifiableCollection(item2Spice.keySet());
    }

    public static Collection<Item> getAllFoodItems() {
        return Collections.unmodifiableCollection(item2Food.keySet());
    }

}
