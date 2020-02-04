package snownee.cuisine.processing.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.processing.ProcessingModule;
import snownee.cuisine.processing.inventory.MillInventory;
import snownee.cuisine.util.FluidHelper;
import snownee.cuisine.util.ingredient.FluidIngredient;
import snownee.kiwi.crafting.Recipe;
import snownee.kiwi.util.Util;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MillingRecipe extends Recipe<MillInventory> {
    private Ingredient item;
    private FluidIngredient fluid;
    private ItemStack itemOut;
    private FluidStack fluidOut;

    public MillingRecipe(ResourceLocation id, Ingredient item,FluidIngredient fluid,
                         ItemStack outItem, FluidStack outFluid) {
        super(id);
        this.item = item;
        this.fluid = fluid;
        this.fluidOut = outFluid;
        this.itemOut = outItem;
    }

    @Override
    public boolean matches(MillInventory inv, World worldIn) {
        return false;
    }

    public List<ItemStack> getInputItemStack(){
       return Arrays.stream(item.getMatchingStacks()).collect(Collectors.toList());
    }

    public List<FluidStack> getInputFluidStack(){
        return Arrays.stream(fluid.getMatchingFluids()).collect(Collectors.toList());
    }

    public ItemStack getOutputItemStack(){
        return itemOut;
    }
    public FluidStack getOutputFluidStack(){
        return fluidOut;
    }
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ProcessingModule.Mill_RECIPE;
    }

    @Override
    public IRecipeType<?> getType() {
        return ProcessingModule.Mill_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MillingRecipe> {

        @Override
        public MillingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            JsonObject ingredient = JSONUtils.getJsonObject(json, "ingredient");
            JsonObject fluid_ingredient = JSONUtils.getJsonObject(json, "fluid_ingredient");
            ItemStack itemStack = null;
            if (JSONUtils.hasField( json,"result")){
                 itemStack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"),true);
            }
            FluidStack fluidStack = null;
            if (JSONUtils.hasField(json,"fluid_result")){
                fluidStack = FluidHelper.getItemStack(JSONUtils.getJsonObject(json, "fluid_result"),true);
            }
            if (fluidStack ==null && itemStack == null){
                throw new JsonSyntaxException("need a result item or fluid.");
            }
            return new MillingRecipe(recipeId,Ingredient.deserialize(ingredient),
                    FluidIngredient.deserialize(fluid_ingredient),
                    itemStack ,fluidStack);

        }

        @Nullable
        @Override
        public MillingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            FluidIngredient fluidIngredient = FluidIngredient.read(buffer);
            ItemStack item = buffer.readItemStack();
            FluidStack fluidStack  = FluidHelper.read(buffer);
            return new MillingRecipe(recipeId, ingredient,fluidIngredient, item, fluidStack);
        }

        @Override
        public void write(PacketBuffer buffer, MillingRecipe recipe) {
            buffer.writeString(recipe.getGroup());
            recipe.item.write(buffer);
            recipe.fluid.write(buffer);
            buffer.writeItemStack(recipe.itemOut);
            FluidHelper.write(buffer,recipe.fluidOut);
        }

    }
}
