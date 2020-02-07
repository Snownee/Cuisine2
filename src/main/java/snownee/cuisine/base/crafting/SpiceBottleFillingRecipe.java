package snownee.cuisine.base.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.kiwi.crafting.DynamicShapedRecipe;
import snownee.kiwi.util.Util;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class SpiceBottleFillingRecipe extends DynamicShapedRecipe {
    private final SpiceBottleItem bottle;

    public SpiceBottleFillingRecipe(ResourceLocation id, String group, SpiceBottleItem bottle) {
        super(id, group, 1, 2, NonNullList.from(Ingredient.EMPTY, SpiceIngredient.INSTANCE, Ingredient.fromItems(bottle)), ItemStack.EMPTY);
        this.bottle = bottle;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        int[] pos = getMatchPos(inv);
        if (pos == null) {
            return false;
        }
        ItemStack container = inv.getStackInSlot(pos[0] + (pos[1] + 1) * inv.getWidth());
        ItemStack in = ItemHandlerHelper.copyStackWithSize(inv.getStackInSlot(pos[0] + pos[1] * inv.getWidth()), 1);
        AtomicBoolean ret = new AtomicBoolean(false);
//        if (inv.getStackInSlot(pos[0] + pos[1] * inv.getWidth()).getCount()==1){
//            in.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(i -> {
//                if (i instanceof FluidHandlerItemStack &&
//                        bottle.fill(container, ((FluidHandlerItemStack) i).getFluid(), IFluidHandler.FluidAction.SIMULATE) > 0) {
//                    ret.set(true);
//                }
//                if (i instanceof FluidBucketWrapper &&
//                        bottle.fill(container, ((FluidBucketWrapper) i).getFluid(), IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME) {
//                    ret.set(true);
//                }
//            });
//        }
        return ret.get() || bottle.fill(container, in, IFluidHandler.FluidAction.SIMULATE) > 0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        int[] pos = getMatchPos(inv);
        if (pos == null) {
            return ItemStack.EMPTY;
        }
        ItemStack in = ItemHandlerHelper.copyStackWithSize(inv.getStackInSlot(pos[0] + pos[1] * inv.getWidth()), 1);
        ItemStack container = ItemHandlerHelper.copyStackWithSize(inv.getStackInSlot(pos[0] + (pos[1] + 1) * inv.getWidth()), 1);
//        AtomicBoolean ret = new AtomicBoolean(false);
//        inv.getStackInSlot(pos[0] + pos[1] * inv.getWidth()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(i -> {
//            if (i instanceof FluidHandlerItemStack) {
//                int num = bottle.fill(container, ((FluidHandlerItemStack) i).getFluid(), IFluidHandler.FluidAction.EXECUTE);
//                if (num > 0) {
//                    ((FluidHandlerItemStack) i).getFluid().shrink(num);
//                    ret.set(true);
//                }
//            }
//            if (i instanceof FluidBucketWrapper) {
//                int num = bottle.fill(container, ((FluidBucketWrapper) i).getFluid(), IFluidHandler.FluidAction.EXECUTE);
//                if (num == FluidAttributes.BUCKET_VOLUME) {
//                    ret.set(true);
//                }
//            }
//        });
//        if (ret.get()) {
//            return container;
//        }
        int num = this.bottle.fill(container, in, IFluidHandler.FluidAction.EXECUTE);
        return num == SpiceBottleItem.VOLUME_PER_ITEM ? container : ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
//        int[] pos = getMatchPos(inv);
//        ItemStack in = ItemHandlerHelper.copyStackWithSize(inv.getStackInSlot(pos[0] + pos[1] * inv.getWidth()), 1);
//        if (in.hasContainerItem()) {
//            nonnulllist.set(pos[0] + pos[1] * inv.getWidth(), in.getContainerItem());
//        }
//        else if (in.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
//            nonnulllist.set(pos[0] + pos[1] * inv.getWidth(), in);
//        }
        return nonnulllist;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BaseModule.SPICE_BOTTLE_FILL;
    }

    private static class SpiceIngredient extends Ingredient {

        public static final SpiceIngredient INSTANCE = new SpiceIngredient();

        private SpiceIngredient() {
            super(Stream.empty());
        }

        @Override
        public boolean isSimple() {
            return false;
        }

        @Override
        public boolean hasNoMatchingItems() {
            return false; // Currently spices may not be loaded.
        }

        @Override
        public boolean test(ItemStack stack) {
            return CuisineAPI.findSpice(stack).isPresent();
        }
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
            buffer.writeString(recipe.getGroup());
            buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, recipe.bottle);
        }

    }

}
