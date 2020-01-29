package snownee.cuisine.api.registry;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CuisineFood extends ForgeRegistryEntry<CuisineFood> {

    @Nullable
    private Item item;
    @Nullable
    private Block block;
    @SerializedName("max_stars")
    private int maxStars = 2;

    @Override
    public String toString() {
        return "CuisineFood{" + getRegistryName() + "}";
    }
}
