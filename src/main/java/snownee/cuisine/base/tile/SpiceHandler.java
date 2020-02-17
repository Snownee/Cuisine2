package snownee.cuisine.base.tile;

import java.util.LinkedList;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.multiblock.KitchenMultiblock;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.tile.ISpiceHandler;
import snownee.cuisine.base.item.SpiceBottleItem;

public class SpiceHandler implements ISpiceHandler {
    private final LinkedList<IItemHandler> handlers = Lists.newLinkedList();
    private int slotCount;
    private final Object2IntOpenHashMap<Spice> spiceCounts = new Object2IntOpenHashMap<>();

    public SpiceHandler() {
        // spiceCounts.defaultReturnValue(0);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot != slotCount && stack.getItem() instanceof SpiceBottleItem) {
            return true;
        }
        return CuisineAPI.findSpice(stack).isPresent();
    }

    @Override
    public int getSlots() {
        return slotCount == 0 ? 0 : slotCount + 1;
    }

    private Pair<IItemHandler, Integer> getLocalSlot(int slot) {
        if (slot < 0 || slot >= slotCount) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + slotCount + ")");
        }
        int i = 0;
        for (IItemHandler handler : handlers) {
            if (i + handler.getSlots() > slot) {
                return Pair.of(handler, slot - i);
            }
            i += handler.getSlots();
        }
        return null; // should never reach
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == slotCount) {
            return ItemStack.EMPTY;
        }
        Pair<IItemHandler, Integer> pair = getLocalSlot(slot);
        return pair.getLeft().getStackInSlot(pair.getRight());
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        Optional<Spice> spice = CuisineAPI.findSpice(stack);
        if (spice.isPresent()) {
            if (simulate) {
                stack = stack.copy();
            }
            for (IItemHandler handler : handlers) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack container = handler.getStackInSlot(i);
                    if (!(container.getItem() instanceof SpiceBottleItem)) {
                        continue;
                    }
                    SpiceBottleItem bottle = (SpiceBottleItem) container.getItem();
                    stack = bottle.fill(container, stack, simulate);
                    // 正常情况下此处应当执行onContentsChanged
                    if (stack.isEmpty()) {
                        return stack;
                    }
                }
            }
            return stack;
        }
        if (slot == slotCount) {
            return stack;
        }
        Pair<IItemHandler, Integer> pair = getLocalSlot(slot);
        return pair.getLeft().insertItem(pair.getRight(), stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == slotCount) {
            return ItemStack.EMPTY;
        }
        Pair<IItemHandler, Integer> pair = getLocalSlot(slot);
        return pair.getLeft().extractItem(pair.getRight(), amount, simulate);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSlotLimit(int slot) {
        if (slot == slotCount) {
            return Items.AIR.getMaxStackSize();
        }
        Pair<IItemHandler, Integer> pair = getLocalSlot(slot);
        return pair.getLeft().getSlotLimit(pair.getRight());
    }

    @Override
    public void addMultiblock(KitchenMultiblock multiblock) {
        IItemHandler handler = multiblock.x;
        if (handler == null || handlers.contains(handler)) {
            return;
        }
        handlers.add(handler);
        slotCount += handler.getSlots();
    }

    @Override
    public void removeMultiblock(KitchenMultiblock multiblock) {
        IItemHandler handler = multiblock.x;
        if (handlers.remove(handler)) {
            slotCount -= handler.getSlots();
        }
    }

    @Override
    public Object2IntMap<Spice> getSpices() {
        return Object2IntMaps.unmodifiable(spiceCounts);
    }

    @Override
    public boolean useSpices(Object2IntMap<Spice> spices) {
        // TODO Auto-generated method stub
        return false;
    }
}
