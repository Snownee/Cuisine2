package snownee.cuisine.processing.crafting;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.processing.ProcessingModule;
import snownee.cuisine.processing.inventory.MillInventory;
import snownee.cuisine.util.FluidStackHelper;
import snownee.cuisine.util.ingredient.FluidIngredient;
import snownee.kiwi.crafting.Recipe;

public class MillingRecipe extends Recipe<MillInventory> {
    private Ingredient item;
    private FluidIngredient fluid;
    private ItemStack itemOut;
    private FluidStack fluidOut;

    public MillingRecipe(ResourceLocation id, Ingredient item, FluidIngredient fluid, ItemStack outItem, FluidStack outFluid) {
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

    public List<ItemStack> getInputItemStack() {
        return Arrays.asList(item.getMatchingStacks());
    }

    public List<FluidStack> getInputFluidStack() {
        return Arrays.asList(fluid.getMatchingFluids());
    }

    public ItemStack getOutputItemStack() {
        return itemOut;
    }

    public FluidStack getOutputFluidStack() {
        return fluidOut;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ProcessingModule.MILL_RECIPE;
    }

    @Override
    public IRecipeType<?> getType() {
        return ProcessingModule.MILL_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MillingRecipe> {

        @Override
        public MillingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.EMPTY;
            if (JSONUtils.hasField(json,"ingredient")){
                ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
                if (ingredient.getMatchingStacks()[0].getItem().equals(Blocks.BARRIER.asItem())){
                    throw new JsonSyntaxException("An error item tag. Could not find any item.");
                }
            }
            FluidIngredient fluid_ingredient = FluidIngredient.EMPTY;
            if (JSONUtils.hasField(json,"fluid_ingredient")){
                fluid_ingredient = FluidIngredient.deserialize(JSONUtils.getJsonObject(json, "fluid_ingredient"));
                if (fluid_ingredient.getMatchingFluids().length==0){
                    throw new JsonSyntaxException("An error fluid tag. Could not find any Fluid.");
                }
            }
            ItemStack itemStack = null;
            if (JSONUtils.hasField(json, "result")) {
                itemStack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
            }
            FluidStack fluidStack = null;
            if (JSONUtils.hasField(json, "fluid_result")) {
                fluidStack = FluidStackHelper.getItemStack(JSONUtils.getJsonObject(json, "fluid_result"), true);
            }
            if (fluidStack == null && itemStack == null) {
                throw new JsonSyntaxException("need a result item or fluid.");
            }
//            return null;
            return new MillingRecipe(recipeId, ingredient, fluid_ingredient, itemStack, fluidStack);

        }

        @Nullable
        @Override
        public MillingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            FluidIngredient fluidIngredient = FluidIngredient.read(buffer);
            ItemStack item = buffer.readItemStack();
            FluidStack fluidStack = FluidStackHelper.read(buffer);
            return new MillingRecipe(recipeId, ingredient, fluidIngredient, item, fluidStack);
        }

        @Override
        public void write(PacketBuffer buffer, MillingRecipe recipe) {
            recipe.item.write(buffer);
            recipe.fluid.write(buffer);
            buffer.writeItemStack(recipe.itemOut);
            FluidStackHelper.write(buffer, recipe.fluidOut);
        }

    }
}
