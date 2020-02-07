package snownee.cuisine.api.registry;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CuisineFood extends ForgeRegistryEntry<CuisineFood> {

    @Nullable
    private Item item;
    @Nullable
    private Block block;
    @SerializedName("max_stars")
    private int maxStars = 2;

    private CuisineFood() {}

    @Nullable
    public Item getItem() {
        if (item != null) {
            return item;
        } else {
            return block.asItem();
        }
    }

    public ItemStack getItemStack() {
        Item item = getItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    @Nullable
    public Block getBlock() {
        return block;
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
            // TODO Auto-generated method stub
            return new CuisineFood();
        }

        @Override
        public void write(PacketBuffer buf, CuisineFood entry) {
            // TODO Auto-generated method stub

        }

    }
}
