package snownee.cuisine.base.crafting;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.StonecuttingRecipe;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import snownee.cuisine.base.BaseModule;
import snownee.kiwi.crafting.FullBlockIngredient;
import snownee.kiwi.util.NBTHelper;

public class TextureStonecuttingRecipe extends StonecuttingRecipe {

    public TextureStonecuttingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        super(id, group, ingredient, result);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        ItemStack input = inv.getStackInSlot(0);
        return FullBlockIngredient.isTextureBlock(input) && this.ingredient.test(input);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack input = inv.getStackInSlot(0);
        Item item = input.getItem();
        if (item instanceof BlockItem) {
            ItemStack result = super.getCraftingResult(inv);
            BlockState state = ((BlockItem) item).getBlock().getDefaultState();
            NBTHelper.of(result).setString("BlockEntityTag.Textures.0", NBTUtil.writeBlockState(state).toString());
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BaseModule.STONECUTTING_TEXTURE;
    }

}
