package snownee.cuisine.api.registry;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CuisineFood extends ForgeRegistryEntry<CuisineFood> {

    private Item item = Items.AIR;
    private Block block = Blocks.AIR;
    @SerializedName("max_stars")
    private int maxStars = 2;

    private CuisineFood() {}

    @Nullable
    public Item getItem() {
        if (item != Items.AIR) {
            return item;
        } else {
            Block block = getBlock();
            return block != null ? block.asItem() : null;
        }
    }

    public ItemStack getItemStack() {
        Item item = getItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    @Nullable
    public Block getBlock() {
        return block != Blocks.AIR ? block : null;
    }

    public ITextComponent getDisplayName() {
        if (getItem() != null) {
            return new TranslationTextComponent(getItem().getTranslationKey());
        } else if (getBlock() != null) {
            return new TranslationTextComponent(getBlock().getTranslationKey());
        }
        return new TranslationTextComponent("cuisine.food." + String.valueOf(getRegistryName()).replace(':', '.'));
    }

    @Override
    public String toString() {
        return "CuisineFood{" + getRegistryName() + "}";
    }

    public static class Serializer implements RegistrySerializer<CuisineFood> {

        @Override
        public CuisineFood read(PacketBuffer buf) {
            CuisineFood food = new CuisineFood();
            food.item = buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
            food.block = buf.readRegistryIdUnsafe(ForgeRegistries.BLOCKS);
            food.maxStars = buf.readByte();
            return food;
        }

        @Override
        public void write(PacketBuffer buf, CuisineFood entry) {
            buf.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, entry.item);
            buf.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, entry.block);
            buf.writeByte(entry.maxStars);
        }

    }
}
