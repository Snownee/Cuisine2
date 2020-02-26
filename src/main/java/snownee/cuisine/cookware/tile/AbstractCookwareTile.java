package snownee.cuisine.cookware.tile;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.event.FoodCookedEvent;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.cookware.container.CookwareContainer;

abstract public class AbstractCookwareTile extends KitchenTile implements ITickableTileEntity {

    protected CuisineRecipe cachedLastRecipe;
    private final Cookware cookware;

    public AbstractCookwareTile(TileEntityType<?> tileEntityTypeIn, Cookware cookware, String... textureKeys) {
        super(tileEntityTypeIn, textureKeys);
        this.cookware = cookware;
    }

    @LogicalServerSide
    public CuisineRecipe cook(FoodBuilder<?> builder, Entity entity) {
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

    public boolean canCook() {
        return true;
    }

    public boolean isOutputFull() {
        return !getOutputHandler().getStackInSlot(0).isEmpty();
    }

    @LogicalServerSide
    public boolean cookAsItem(@Nullable Entity entity, boolean insertEntityInventory) {
        if (isRemoved() || !canCook()) {
            return false;
        }
        if (isOutputFull() && (!insertEntityInventory || entity == null || !entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())) {
            return false;
        }
        FoodBuilder<?> builder = foodBuilder(this, entity);
        CuisineRecipe recipe = cook(builder, entity);
        if (recipe == null) {
            return false;
        }
        ItemStack result = builder.build(recipe);
        FoodCookedEvent event = new FoodCookedEvent(builder, result);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        result = event.output;
        builder = event.foodBuilder;
        // we assume the amount of result is always 1
        if (result.isEmpty()) {
            return false;
        }
        IItemHandler output = getOutputHandler();
        if (!ItemHandlerHelper.insertItemStacked(output, result, true).isEmpty()) {
            if (!insertEntityInventory || entity == null) {
                return false;
            }
            IItemHandler itemHandler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
            if (itemHandler == null || !ItemHandlerHelper.insertItemStacked(itemHandler, result, true).isEmpty()) {
                return false;
            }
            output = itemHandler;
        }
        IItemHandler input = getInputHandler();
        for (int i = 0; i < input.getSlots(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            ItemStack container = stack.getContainerItem();
            if (!stack.isEmpty() && !ItemHandlerHelper.canItemStacksStack(stack, container)) {
                input.extractItem(i, 1, false);
                container = input.insertItem(i, container, false);
                if (!container.isEmpty()) {
                    if (entity != null) {
                        if (insertEntityInventory || entity instanceof PlayerEntity) {
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
        IItemHandler paperHandler = getPaperHandler();
        IItemHandlerModifiable recipeHandler = getRecipeHandler();
        /* off */
        if (paperHandler != null
                && recipeHandler != null
                && recipeHandler.getStackInSlot(0).isEmpty()) {
            for (int i = 0; i < paperHandler.getSlots(); i++) {
                /* on */
                if (!paperHandler.getStackInSlot(i).isEmpty()) {
                    ItemStack recipeItem = BaseModule.RECIPE.make(builder, recipe);
                    recipeHandler.setStackInSlot(i, recipeItem);
                    paperHandler.extractItem(i, 1, false);
                }
            }
        }
        return ItemHandlerHelper.insertItemStacked(output, result, false).isEmpty();
    }

    abstract public IItemHandlerModifiable getOutputHandler();

    abstract public IItemHandlerModifiable getInputHandler();

    @Nullable
    abstract public IItemHandlerModifiable getRecipeHandler();

    @Nullable
    abstract public IItemHandler getPaperHandler();

    public <C> FoodBuilder<C> foodBuilder(C context, @Nullable Entity cook) {
        FoodBuilder<C> builder = CuisineAPI.foodBuilder(getCookware(), context, cook);
        getMaterials().forEach(builder::add);
        getFoods().forEach(builder::add);
        getSpices().forEach(builder::add);
        return builder;
    }

    protected Stream<Material> getMaterials() {
        IItemHandler handler = getInputHandler();
        /* off */
        return IntStream
                .range(0, handler.getSlots())
                .mapToObj(handler::getStackInSlot)
                .filter($ -> !$.isEmpty())
                .map(CuisineAPI::findMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get);
        /* on */
    }

    protected Stream<Spice> getSpices() {
        IItemHandler handler = getInputHandler();
        /* off */
        return Stream.empty();
        /* on */
    }

    protected Stream<CuisineFoodInstance> getFoods() {
        IItemHandler handler = getInputHandler();
        /* off */
        return IntStream
                .range(0, handler.getSlots())
                .mapToObj(handler::getStackInSlot)
                .filter($ -> !$.isEmpty())
                .map(CuisineAPI::findFoodInstance)
                .filter(Optional::isPresent)
                .map(Optional::get);
        /* on */
    }

    public Cookware getCookware() {
        return cookware;
    }

    protected CookwareContainer cookingPlayer;

    @Override
    public void tick() {
        if (!world.isRemote && cookingPlayer != null) {
            cookingPlayer.tick();
        }
    }

    public void stopCooking() {
        cookingPlayer = null;
    }

    public void startCooking(CookwareContainer cookingPlayer) {
        Preconditions.checkArgument(this.cookingPlayer == null || this.cookingPlayer == cookingPlayer);
        this.cookingPlayer = cookingPlayer;
    }

    public PlayerEntity getCookingPlayer() {
        return cookingPlayer == null ? null : cookingPlayer.player;
    }

}
