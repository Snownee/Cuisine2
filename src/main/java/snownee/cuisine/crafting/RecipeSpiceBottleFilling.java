package snownee.cuisine.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.BaseModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RecipeSpiceBottleFilling implements ICraftingRecipe {
    private final ResourceLocation Id;
    private final String group;

    public RecipeSpiceBottleFilling(ResourceLocation idIn, String groupIn) {
        this.Id = idIn;
        this.group = groupIn;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        for (int x = 0; x < inv.getWidth(); x++) {
            for (int y = 0; y < inv.getHeight() - 1; y++) {
                if (checkRecipe(inv, x + y * inv.getWidth())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkRecipe(CraftingInventory inv, int slot) {
        ItemStack spice = inv.getStackInSlot(slot);
        Optional<Spice> spice_op = CuisineAPI.findSpice(spice);
        if (!spice_op.isPresent()) {
            return false;
        }
        ItemStack bottle = inv.getStackInSlot(slot + inv.getWidth());
        return bottle.getItem() == BaseModule.SPICE_BOTTLE &&
                BaseModule.SPICE_BOTTLE.fill(bottle, spice, IFluidHandler.FluidAction.SIMULATE) != 0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack bottle = ItemStack.EMPTY;
        ItemStack spice = ItemStack.EMPTY;
        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack stack = inv.getStackInSlot(k);
            if (!stack.isEmpty()) {
                if (stack.getItem() == BaseModule.SPICE_BOTTLE) {
                    bottle = stack.copy();
                    bottle.setCount(1);
                } else {
                    spice = stack;
                }
            }
        }
        if (bottle.isEmpty() || spice.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int num = BaseModule.SPICE_BOTTLE.fill(bottle, spice, IFluidHandler.FluidAction.EXECUTE);
        if (num==0){
            return ItemStack.EMPTY;
        }
        spice.shrink(num-1);
        return bottle;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width > 0 && height > 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return Id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BaseModule.recipeSpiceBottleFilling;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeSpiceBottleFilling> {
        public static boolean recordRecipes = EffectiveSide.get() == LogicalSide.CLIENT && ModList.get().isLoaded("jei");
        public static List<RecipeSpiceBottleFilling> recipes = recordRecipes ? Lists.newArrayList() : null;

        @Override
        public RecipeSpiceBottleFilling read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            return record(new RecipeSpiceBottleFilling(recipeId, group));
        }

        @Nullable
        @Override
        public RecipeSpiceBottleFilling read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            return record(new RecipeSpiceBottleFilling(recipeId, group));
        }

        @Override
        public void write(PacketBuffer buffer, RecipeSpiceBottleFilling recipe) {
            buffer.writeString(recipe.group);
        }

        public static RecipeSpiceBottleFilling record(RecipeSpiceBottleFilling recipe) {
            if (recordRecipes) {
                recipes.add(recipe);
            }
            return recipe;
        }
    }

}
