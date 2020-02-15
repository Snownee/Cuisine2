package snownee.cuisine.cookware.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.base.item.RecipeItem;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.container.OvenContainer;
import snownee.cuisine.util.ExtractOnlyItemHandler;
import snownee.cuisine.util.InvHandlerWrapper;

public class OvenTile extends AbstractCookwareTile implements INamedContainerProvider {

    private final ItemStackHandler inputHandler = new ItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CuisineAPI.findMaterial(stack).isPresent() || CuisineAPI.findFood(stack).isPresent();
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.isEmpty())
                return ItemStack.EMPTY;

            if (!isItemValid(slot, stack))
                return stack;

            validateSlotIndex(slot);

            ItemStack existing = this.stacks.get(slot);

            int limit = getStackLimit(slot, stack);

            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack;

                limit -= existing.getCount();
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate) {
                if (existing.isEmpty()) {
                    this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                    cachedLastRecipe = null; // cuisine patch
                } else {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged(slot);
            }

            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
        }

        @Override
        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0)
                return ItemStack.EMPTY;

            validateSlotIndex(slot);

            ItemStack existing = this.stacks.get(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract) {
                if (!simulate) {
                    this.stacks.set(slot, ItemStack.EMPTY);
                    cachedLastRecipe = null; // cuisine patch
                    onContentsChanged(slot);
                }
                return existing;
            } else {
                if (!simulate) {
                    this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    onContentsChanged(slot);
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

    };
    private final ExtractOnlyItemHandler<ItemStackHandler> outputHandler = new ExtractOnlyItemHandler<>(new ItemStackHandler());
    private final LazyOptional<ItemStackHandler> inputProvider = LazyOptional.of(() -> inputHandler);
    private final LazyOptional<IItemHandler> outputProvider = LazyOptional.of(() -> outputHandler);
    private final ItemStackHandler paperHandler = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() == Items.PAPER;
        }
    };
    private final ItemStackHandler recipeHandler = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof RecipeItem;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private final LazyOptional<IItemHandlerModifiable> unsidedProvider = LazyOptional.of(() -> new CombinedInvWrapper(inputHandler, outputHandler.get()) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot != inputHandler.getSlots() && super.isItemValid(slot, stack);
        }
    });

    public OvenTile() {
        super(CookwareModule.OVEN_TILE, CookwareModule.OVEN_TYPE);
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new OvenContainer(id, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuisine.oven");
    }

    public IInventory getInventory() {
        return new InvHandlerWrapper(new CombinedInvWrapper(inputHandler, outputHandler.get(), paperHandler, recipeHandler));
    }

    @Override
    public IItemHandlerModifiable getInputHandler() {
        return inputHandler;
    }

    @Override
    public IItemHandlerModifiable getOutputHandler() {
        return outputHandler.get();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getItemCap(side).cast();
        }
        return super.getCapability(cap, side);
    }

    public LazyOptional<? extends IItemHandler> getItemCap(@Nullable Direction side) {
        switch (side) {
        case UP:
            return inputProvider;
        case DOWN:
            return outputProvider;
        default:
            return unsidedProvider;
        }
    }

    @Override
    public void remove() {
        super.remove();
        unsidedProvider.invalidate();
        inputProvider.invalidate();
        outputProvider.invalidate();
    }

    @Override
    public void read(CompoundNBT compound) {
        inputHandler.deserializeNBT(compound.getCompound("Input"));
        outputHandler.get().deserializeNBT(compound.getCompound("Output"));
        recipeHandler.deserializeNBT(compound.getCompound("Recipe"));
        paperHandler.deserializeNBT(compound.getCompound("Paper"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Input", inputHandler.serializeNBT());
        compound.put("Output", outputHandler.get().serializeNBT());
        compound.put("Recipe", recipeHandler.serializeNBT());
        compound.put("Paper", paperHandler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public IItemHandler getPaperHandler() {
        return paperHandler;
    }

    @Override
    public IItemHandlerModifiable getRecipeHandler() {
        return recipeHandler;
    }

}
