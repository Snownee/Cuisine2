package snownee.cuisine.base.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapIdTracker;
import net.minecraftforge.common.util.Constants;
import snownee.cuisine.Cuisine;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.data.RecordData;
import snownee.cuisine.data.network.SSyncRecordPacket;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.util.NBTHelper;

public class RecipeItem extends ModItem {

    public RecipeItem() {
        super(new Item.Properties());
    }

    public ItemStack make(FoodBuilder<?> builder, CuisineRecipe recipe) {
        DimensionSavedDataManager dataManager = Cuisine.getServer().getWorld(DimensionType.OVERWORLD).getSavedData();
        MapIdTracker tracker = dataManager.getOrCreate(MapIdTracker::new, "idcounts");
        int id = tracker.field_215163_a.getInt("cuisine.recipe") + 1;
        tracker.field_215163_a.put("cuisine.recipe", id);
        tracker.markDirty();
        RecordData recipeData = new RecordData(id);
        recipeData.put(builder, recipe);
        dataManager.set(recipeData);
        ItemStack stack = new ItemStack(this);
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("Id", id);
        tag.putString("Key", recipe.getResult().getTranslationKey());
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof ServerPlayerEntity && stack.hasTag() && stack.getTag().contains("Id", Constants.NBT.TAG_INT)) {
            int id = stack.getTag().getInt("Id");
            RecordData data = getData(id);
            if (data != null && !data.syncedPlayers.contains(entityIn)) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityIn;
                new SSyncRecordPacket(data, id).send(player);
                data.syncedPlayers.add(player);
            }
        }
    }

    public static RecordData getData(ItemStack stack) {
        return getData(NBTHelper.of(stack).getInt("Id"));
    }

    public static RecordData getData(int id) {
        return Cuisine.getServer().getWorld(DimensionType.OVERWORLD).getSavedData().get(() -> new RecordData(id), "cuisine/recipe_" + id);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        String key = NBTHelper.of(stack).getString("Key", "cuisine.unknown");
        return new TranslationTextComponent(getTranslationKey(), new TranslationTextComponent(key));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return NBTHelper.of(stack).getString("Key", super.getTranslationKey());
    }

}
