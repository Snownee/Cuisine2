package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.ImmutableSet;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.data.tag.SpiceTags;

public class Spice extends ForgeRegistryEntry<Spice> {

    private String color = null;
    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableSet<Fluid> fluids = ImmutableSet.of();
    private ImmutableSet<Tag<Fluid>> fluidTags = ImmutableSet.of();
    private ReverseTagWrapper<Spice> reverseTags = new ReverseTagWrapper<>(this, SpiceTags::getGeneration, SpiceTags::getCollection);

    public final ImmutableSet<Item> getItems() {
        return items;
    }

    public final ImmutableSet<Tag<Item>> getItemTags() {
        return tags;
    }

    public final ImmutableSet<Fluid> getFluids() {
        return fluids;
    }

    public final ImmutableSet<Tag<Fluid>> getFluidTags() {
        return fluidTags;
    }

    public final Set<ResourceLocation> getTags() {
        return reverseTags.getTagNames();
    }

    public String getColor(){
        return color;
    }
    @Override
    public String toString() {
        return "Spice{" + getRegistryName() + "}";
    }
}
