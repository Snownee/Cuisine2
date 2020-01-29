package snownee.cuisine.api.registry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Spice extends ForgeRegistryEntry<Spice> {

    private ImmutableSet<Item> items;
    private ImmutableSet<Tag<Item>> tags;
    private ImmutableSet<Fluid> fluids;
    private ImmutableSet<Tag<Fluid>> fluidTags;

    public ImmutableSet<Item> getItems() {
        return items;
    }

    public ImmutableSet<Tag<Item>> getTags() {
        return tags;
    }

    public ImmutableSet<Fluid> getFluids() {
        return fluids;
    }

    public ImmutableSet<Tag<Fluid>> getFluidTags() {
        return fluidTags;
    }

    @Override
    public String toString() {
        return "Spice{" + getRegistryName() + "}";
    }
}
