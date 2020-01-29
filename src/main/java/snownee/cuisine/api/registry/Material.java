package snownee.cuisine.api.registry;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.Bonus;

public class Material extends ForgeRegistryEntry<Material> {

    private ImmutableSet<Item> items;
    private ImmutableSet<Tag<Item>> tags;
    private ImmutableListMultimap<Integer, Bonus> stars;

    @Override
    public String toString() {
        return "Material{" + getRegistryName() + "}";
    }
}
