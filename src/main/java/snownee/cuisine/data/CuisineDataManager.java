package snownee.cuisine.data;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.JsonUtils.ImmutableListTypeAdapter;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import snownee.cuisine.api.Bonus;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.registry.Utensil;
import snownee.cuisine.data.adapter.ForgeRegistryAdapter;
import snownee.cuisine.data.adapter.ImmutableSetAdapter;
import snownee.cuisine.data.adapter.StarsAdapter;
import snownee.cuisine.data.adapter.TagAdapter;

public class CuisineDataManager<T extends IForgeRegistryEntry<T>> extends JsonReloadListener {

    /* off */
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Item.class, new ForgeRegistryAdapter(Item.class))
            .registerTypeAdapter(Block.class, new ForgeRegistryAdapter(Block.class))
            //.registerTypeAdapter(Material.class, new ForgeRegistryAdapter(Material.class))
            //.registerTypeAdapter(Spice.class, new ForgeRegistryAdapter(Spice.class))
            //.registerTypeAdapter(Utensil.class, new ForgeRegistryAdapter(Utensil.class))
            //.registerTypeAdapter(CuisineFood.class, new ForgeRegistryAdapter(CuisineFood.class))
            .registerTypeAdapter(ImmutableListMultimap.class, new StarsAdapter())
            .registerTypeAdapter(ImmutableList.class, ImmutableListTypeAdapter.INSTANCE)
            .registerTypeAdapter(ImmutableSet.class, ImmutableSetAdapter.INSTANCE)
            .registerTypeAdapter(Tag.class, new TagAdapter())
            .create();
    /* on */

    public static final Map<String, JsonDeserializer<Bonus>> bonusAdapters = Maps.newHashMap();
    public static final Map<String, JsonDeserializer<RecipeRule>> ruleAdapters = Maps.newHashMap();

    private final ForgeRegistry<T> registry;
    private Runnable callback;

    public CuisineDataManager(String folder, ForgeRegistry<T> registry) {
        super(GSON, folder);
        this.registry = registry;
    }

    public CuisineDataManager<T> setCallback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startSection("Loading " + registry.getRegistryName());
        registry.unfreeze();
        registry.clear();
        splashList.forEach((id, o) -> {
            System.out.println(registry.getRegistryName() + " " + id);
            T go = GSON.fromJson(o, registry.getRegistrySuperType());
            registry.register(go.setRegistryName(id));
        });
        registry.freeze();
        profilerIn.endSection();
    }

}
