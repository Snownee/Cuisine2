package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.Bonus;
import snownee.cuisine.data.tag.MaterialTags;

public class Material extends ForgeRegistryEntry<Material> {

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableListMultimap<Integer, Bonus> stars = ImmutableListMultimap.of();
    private final ReverseTagWrapper<Material> reverseTags = new ReverseTagWrapper<>(this, MaterialTags::getGeneration, MaterialTags::getCollection);

    public final ImmutableSet<Item> getItems() {
        return items;
    }

    public final ImmutableSet<Tag<Item>> getItemTags() {
        return tags;
    }

    public final Set<ResourceLocation> getTags() {
        return reverseTags.getTagNames();
    }

    @Override
    public String toString() {
        return "Material{" + getRegistryName() + "}";
    }
}
