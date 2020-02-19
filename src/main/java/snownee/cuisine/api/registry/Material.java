package snownee.cuisine.api.registry;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import snownee.cuisine.api.Bonus;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.tag.MaterialTags;

public class Material extends CuisineRegistryEntry<Material> {

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableListMultimap<Integer, Bonus> stars = ImmutableListMultimap.of();
    @SerializedName("translation_key")
    private String translationKey;
    private final ReverseTagWrapper<Material> reverseTags = new ReverseTagWrapper<>(this, MaterialTags::getGeneration, MaterialTags::getCollection);

    private Material() {}

    public ImmutableSet<Item> getItems() {
        return items;
    }

    @LogicalServerSide
    public ImmutableSet<Tag<Item>> getItemTags() {
        return tags;
    }

    public Set<ResourceLocation> getTags() {
        return reverseTags.getTagNames();
    }

    public List<Bonus> getBonus(int star) {
        return stars.get(star);
    }

    @Override
    public ITextComponent getDisplayName() {
        if (translationKey == null) {
            if (!items.isEmpty()) {
                translationKey = items.asList().get(0).getTranslationKey();
            } else if (!tags.isEmpty()) {
                for (Tag<Item> tag : tags) {
                    if (!tag.getAllElements().isEmpty()) {
                        Item item = tag.getRandomElement(CuisineAPI.RAND);
                        translationKey = item.getTranslationKey();
                        break;
                    }
                }
            }
            if (translationKey == null) {
                translationKey = Util.makeTranslationKey("cuisine.material", getRegistryName());
            }
        }
        return new TranslationTextComponent(translationKey);
    }

    @Override
    public String toString() {
        return "Material{" + getRegistryName() + "}";
    }

    @Override
    @LogicalServerSide
    public boolean validate() {
        return valid = !items.isEmpty() || !tags.stream().allMatch(tag -> tag.getAllElements().isEmpty());
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
            return material;
        }

        @Override
        public void write(PacketBuffer buf, Material entry) {
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
        }

    }
}
