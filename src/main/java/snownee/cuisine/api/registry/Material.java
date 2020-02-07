package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.ReverseTagWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.Bonus;
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
            // TODO Auto-generated method stub
            return new Material();
        }

        @Override
        public void write(PacketBuffer buf, Material entry) {
            // TODO Auto-generated method stub

        }

    }
}
