package snownee.cuisine.api.registry;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.fluid.Fluid;
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
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.tag.SpiceTags;

public class Spice extends ForgeRegistryEntry<Spice> {

    //TODO remove
    private int color = 0xff0000;

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableSet<Fluid> fluids = ImmutableSet.of();
    @SerializedName("fluid_tags")
    private ImmutableSet<Tag<Fluid>> fluidTags = ImmutableSet.of();
    private ReverseTagWrapper<Spice> reverseTags = new ReverseTagWrapper<>(this, SpiceTags::getGeneration, SpiceTags::getCollection);
    @SerializedName("translation_key")
    private String translationKey;

    private Spice() {}

    public final ImmutableSet<Item> getItems() {
        return items;
    }

    @LogicalServerSide
    public final ImmutableSet<Tag<Item>> getItemTags() {
        return tags;
    }

    public final ImmutableSet<Fluid> getFluids() {
        return fluids;
    }

    @LogicalServerSide
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
                if (!fluids.isEmpty()) {
                    translationKey = fluids.asList().get(0).getAttributes().getTranslationKey();
                } else if (!fluidTags.isEmpty()) {
                    for (Tag<Fluid> tag : fluidTags) {
                        if (!tag.getAllElements().isEmpty()) {
                            Fluid item = tag.getRandomElement(CuisineAPI.RAND);
                            translationKey = item.getAttributes().getTranslationKey();
                            break;
                        }
                    }
                }
                if (translationKey == null) {
                    translationKey = Util.makeTranslationKey("cuisine.spice", getRegistryName());
                }
            }
        }
        return new TranslationTextComponent(translationKey);
    }

    @Override
    public String toString() {
        return "Spice{" + getRegistryName() + "}";
    }

    public static class Serializer implements RegistrySerializer<Spice> {

        @Override
        public Spice read(PacketBuffer buf) {
            Spice spice = new Spice();
            int[] ids = buf.readVarIntArray();
            ImmutableSet.Builder<Item> itemBuilder = ImmutableSet.builder();
            ForgeRegistry<Item> itemRegistry = (ForgeRegistry<Item>) ForgeRegistries.ITEMS;
            for (int id : ids) {
                itemBuilder.add(itemRegistry.getValue(id));
            }
            spice.items = itemBuilder.build();
            ids = buf.readVarIntArray();
            ImmutableSet.Builder<Fluid> fluidBuilder = ImmutableSet.builder();
            ForgeRegistry<Fluid> fluidRegistry = (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS;
            for (int id : ids) {
                fluidBuilder.add(fluidRegistry.getValue(id));
            }
            spice.fluids = fluidBuilder.build();
            return spice;
        }

        @Override
        public void write(PacketBuffer buf, Spice entry) {
            ForgeRegistry<Item> itemRegistry = (ForgeRegistry<Item>) ForgeRegistries.ITEMS;
            IntSet set = new IntArraySet();
            for (Item item : entry.items) {
                set.add(itemRegistry.getID(item));
            }
            for (Tag<Item> tag : entry.tags) {
                for (Item item : tag.getAllElements()) {
                    set.add(itemRegistry.getID(item));
                }
            }
            buf.writeVarIntArray(set.toIntArray());
            ForgeRegistry<Fluid> fluidRegistry = (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS;
            set = new IntArraySet();
            for (Fluid fluid : entry.fluids) {
                set.add(fluidRegistry.getID(fluid));
            }
            for (Tag<Fluid> tag : entry.fluidTags) {
                for (Fluid fluid : tag.getAllElements()) {
                    set.add(fluidRegistry.getID(fluid));
                }
            }
            buf.writeVarIntArray(set.toIntArray());
        }

    }

}
