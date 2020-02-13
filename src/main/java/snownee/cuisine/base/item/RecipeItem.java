package snownee.cuisine.base.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapIdTracker;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.data.RecordData;
import snownee.kiwi.item.ModItem;

public class RecipeItem extends ModItem {

    public RecipeItem() {
        super(new Item.Properties());
    }

    public ItemStack make(MinecraftServer server, FoodBuilder<?> builder, CuisineRecipe recipe) {
        DimensionSavedDataManager dataManager = server.getWorld(DimensionType.OVERWORLD).getSavedData();
        MapIdTracker tracker = dataManager.getOrCreate(MapIdTracker::new, "idcounts");
        int i = tracker.field_215163_a.getInt("cuisine.recipe") + 1;
        tracker.field_215163_a.put("cuisine.recipe", i);
        tracker.markDirty();
        RecordData recipeData = new RecordData("cuisine/recipe_" + i);
        recipeData.put(builder, recipe);
        dataManager.set(recipeData);
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt("Id", i);
        return stack;
    }

}
