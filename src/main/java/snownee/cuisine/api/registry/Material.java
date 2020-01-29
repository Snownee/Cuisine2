package snownee.cuisine.api.registry;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.Bonus;

public class Material extends ForgeRegistryEntry<Material> {

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableListMultimap<Integer, Bonus> stars = ImmutableListMultimap.of();

    public ImmutableSet<Item> getItems() {
        return items;
    }

    public ImmutableSet<Tag<Item>> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Material{" + getRegistryName() + "}";
    }
}
