package snownee.cuisine.cookware.tile;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.tile.KitchenTile;

abstract public class AbstractCookwareTile extends KitchenTile {

    protected CuisineRecipe cachedLastRecipe;
    private final Cookware cookware;

    public AbstractCookwareTile(TileEntityType<?> tileEntityTypeIn, Cookware cookware, String... textureKeys) {
        super(tileEntityTypeIn, textureKeys);
        this.cookware = cookware;
    }

    public CuisineRecipe cook(Entity entity) {
        FoodBuilder<AbstractCookwareTile> builder = foodBuilder(this, entity);
        if (cachedLastRecipe != null && cachedLastRecipe.matches(builder)) {
            return cachedLastRecipe;
        }
        Optional<CuisineRecipe> recipe = builder.findRecipe();
        if (recipe.isPresent()) {
            return cachedLastRecipe = recipe.get();
        } else {
            return null;
        }
    }

    public boolean cookAsItem(Entity entity) {
        CuisineRecipe recipe = cook(entity);
        if (recipe == null) {
            return false;
        }
        ItemStack result = recipe.getResult().getItemStack();
        // we assume the amount of result is always 1
        if (result.isEmpty()) {
            return false;
        }
        IItemHandlerModifiable output = getOutputHandler();
        IItemHandlerModifiable input = getInputHandler();
        for (int i = 0; i < input.getSlots(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            ItemStack container = stack.getContainerItem();
            if (!ItemHandlerHelper.canItemStacksStack(stack, container)) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    input.setStackInSlot(i, container);
                } else {
                    input.setStackInSlot(i, stack);
                    if (!container.isEmpty()) {
                        if (entity != null) {
                            if (entity instanceof PlayerEntity) {
                                ItemHandlerHelper.giveItemToPlayer((PlayerEntity) entity, container);
                                container = ItemStack.EMPTY;
                            } else {
                                IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                                if (handler != null) {
                                    container = ItemHandlerHelper.insertItemStacked(handler, container, false);
                                }
                            }
                        }
                        if (container != null) {
                            Block.spawnAsEntity(world, pos, container);
                        }
                    }
                }
            }
        }
        return ItemHandlerHelper.insertItemStacked(output, result, false).isEmpty();
    }

    abstract public IItemHandlerModifiable getOutputHandler();

    abstract public IItemHandlerModifiable getInputHandler();

    public <C> FoodBuilder<C> foodBuilder(C context, @Nullable Entity cook) {
        FoodBuilder<C> builder = CuisineAPI.foodBuilder(getCookware(), context, cook);
        /* off */
        getMaterialItems().stream()
                .map(CuisineAPI::findMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(builder::add);
        /* on */
        return builder;
    }

    abstract public List<ItemStack> getMaterialItems();

    public Cookware getCookware() {
        return cookware;
    }

}
