package snownee.cuisine.base.crafting;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.kiwi.util.Util;

public class SpiceBottleFillingRecipe implements ICraftingRecipe {
    private final ResourceLocation id;
    private final String group;
    private final SpiceBottleItem bottle;

    public SpiceBottleFillingRecipe(ResourceLocation id, String group, SpiceBottleItem bottle) {
        this.id = id;
        this.group = group;
        this.bottle = bottle;
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
        return bottle.getItem() == this.bottle && this.bottle.fill(bottle, spice, IFluidHandler.FluidAction.SIMULATE) != 0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack bottle = ItemStack.EMPTY;
        ItemStack spice = ItemStack.EMPTY;
        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack stack = inv.getStackInSlot(k);
            if (!stack.isEmpty()) {
                if (stack.getItem() == this.bottle) {
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
        int num = this.bottle.fill(bottle, spice, IFluidHandler.FluidAction.EXECUTE);
        if (num == 0) {
            return ItemStack.EMPTY;
        }
        spice.shrink(num - 1);
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
        return id;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BaseModule.SPICE_BOTTLE_FILL;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpiceBottleFillingRecipe> {

        @Override
        public SpiceBottleFillingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            ResourceLocation id = Util.RL(JSONUtils.getString(json, "item"));
            if (id == null) {
                throw new JsonSyntaxException("Malformed item id, expected to find a string");
            }
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item instanceof SpiceBottleItem) {
                return new SpiceBottleFillingRecipe(recipeId, group, (SpiceBottleItem) item);
            } else {
                throw new JsonSyntaxException("Item is not spice bottle");
            }
        }

        @Nullable
        @Override
        public SpiceBottleFillingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Item item = buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
            return new SpiceBottleFillingRecipe(recipeId, group, (SpiceBottleItem) item);
        }

        @Override
        public void write(PacketBuffer buffer, SpiceBottleFillingRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, recipe.bottle);
        }

    }

}
