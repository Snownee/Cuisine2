package snownee.cuisine.api.registry;

import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.fluid.Fluid;
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
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.tag.SpiceTags;

public class Spice extends ForgeRegistryEntry<Spice> {

    //TODO remove
    private int color = 0xff0000;

    private ImmutableSet<Item> items = ImmutableSet.of();
    private ImmutableSet<Tag<Item>> tags = ImmutableSet.of();
    private ImmutableSet<Fluid> fluids = ImmutableSet.of();
    private ImmutableSet<Tag<Fluid>> fluidTags = ImmutableSet.of();
    private ReverseTagWrapper<Spice> reverseTags = new ReverseTagWrapper<>(this, SpiceTags::getGeneration, SpiceTags::getCollection);

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
        return new TranslationTextComponent("cuisine.spice." + String.valueOf(getRegistryName()).replace(':', '.'));
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
            IntStream.Builder builder = IntStream.builder();
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
            builder = IntStream.builder();
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
