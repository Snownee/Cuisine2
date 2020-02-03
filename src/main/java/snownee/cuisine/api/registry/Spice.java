package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.tag.SpiceTags;

public class Spice extends ForgeRegistryEntry<Spice> {

    private int color = 0xff0000;
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

    public int getColor() {
        return color;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("cuisine.spice." + String.valueOf(getRegistryName()).replace(':', '.'));
    }

    @Override
    public String toString() {
        return "Spice{" + getRegistryName() + "}";
    }
}
