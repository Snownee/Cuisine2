package snownee.cuisine.api.registry;

import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.Bonus;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.tag.MaterialTags;

public class Material extends ForgeRegistryEntry<Material> {

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableListMultimap<Integer, Bonus> stars = ImmutableListMultimap.of();
    private final ReverseTagWrapper<Material> reverseTags = new ReverseTagWrapper<>(this, MaterialTags::getGeneration, MaterialTags::getCollection);

    private Material() {}

    public final ImmutableSet<Item> getItems() {
        return items;
    }

    @LogicalServerSide
    public final ImmutableSet<Tag<Item>> getItemTags() {
        return tags;
    }

    public final Set<ResourceLocation> getTags() {
        return reverseTags.getTagNames();
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("cuisine.material." + String.valueOf(getRegistryName()).replace(':', '.'));
    }

    @Override
    public String toString() {
        return "Material{" + getRegistryName() + "}";
    }

    public static class Serializer implements RegistrySerializer<Material> {

        @Override
        public Material read(PacketBuffer buf) {
            Material material = new Material();
            int[] ids = buf.readVarIntArray();
            ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
            ForgeRegistry<Item> registry = (ForgeRegistry<Item>) ForgeRegistries.ITEMS;
            for (int id : ids) {
                builder.add(registry.getValue(id));
            }
            material.items = builder.build();
            return material.setRegistryName(buf.readResourceLocation());
        }

        @Override
        public void write(PacketBuffer buf, Material entry) {
            IntStream.Builder builder = IntStream.builder();
            ForgeRegistry<Item> registry = (ForgeRegistry<Item>) ForgeRegistries.ITEMS;
            IntSet set = new IntArraySet();
            for (Item item : entry.items) {
                set.add(registry.getID(item));
            }
            for (Tag<Item> tag : entry.tags) {
                for (Item item : tag.getAllElements()) {
                    set.add(registry.getID(item));
                }
            }
            buf.writeVarIntArray(set.toIntArray());
            buf.writeResourceLocation(entry.getRegistryName());
        }

    }
}
