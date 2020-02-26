package snownee.cuisine.base.tile;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.IntPredicate;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap.BasicEntry;
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
import snownee.cuisine.data.DeferredReloadListener;

public class SpiceHandler implements ISpiceHandler {
    private final LinkedList<IItemHandler> handlers = Lists.newLinkedList();
    private int slotCount;
    private final Object2IntOpenHashMap<Spice> spiceCounts = new Object2IntOpenHashMap<>();
    private int dataPackID = DeferredReloadListener.INSTANCE.getDataPackID();

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

    private BasicEntry<IItemHandler> getLocalSlot(int slot) {
        if (slot < 0 || slot >= slotCount) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + slotCount + ")");
        }
        int i = 0;
        for (IItemHandler handler : handlers) {
            if (i + handler.getSlots() > slot) {
                return new BasicEntry<>(handler, slot - i);
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
        BasicEntry<IItemHandler> pair = getLocalSlot(slot);
        return pair.getKey().getStackInSlot(pair.getIntValue());
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slotCount == 0) {
            return stack;
        }
        tryRefresh();
        Optional<Spice> spice = CuisineAPI.findSpice(stack);
        if (spice.isPresent()) {
            if (simulate) {
                stack = stack.copy();
            }
            //TODO insertStackStacked
            for (IItemHandler handler : handlers) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack container = handler.getStackInSlot(i);
                    if (!(container.getItem() instanceof SpiceBottleItem)) {
                        continue;
                    }
                    SpiceBottleItem bottle = (SpiceBottleItem) container.getItem();
                    int c = stack.getCount();
                    stack = bottle.fill(container, stack, simulate);
                    if (!simulate && c != stack.getCount()) {
                        spiceCounts.addTo(spice.get(), SpiceBottleItem.VOLUME_PER_ITEM * (c - stack.getCount()));
                    }
                    // 正常情况下此处应当执行onContentsChanged
                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            return stack;
        }
        if (slot == slotCount) {
            return stack;
        }
        BasicEntry<IItemHandler> pair = getLocalSlot(slot);
        int c = stack.getCount();
        ItemStack ret = pair.getKey().insertItem(pair.getIntValue(), stack, simulate);
        if (!simulate && ret.getCount() != c) {
            SpiceBottleItem.getSpice(stack).ifPresent(s -> {
                spiceCounts.addTo(s, SpiceBottleItem.VOLUME_PER_ITEM * (c - ret.getCount()));
            });
        }
        return ret;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slotCount == 0 || slot == slotCount) {
            return ItemStack.EMPTY;
        }
        tryRefresh();
        BasicEntry<IItemHandler> pair = getLocalSlot(slot);
        ItemStack ret = pair.getKey().extractItem(pair.getIntValue(), amount, simulate);
        if (!simulate) {
            SpiceBottleItem.getSpice(ret).ifPresent(spice -> {
                spiceCounts.addTo(spice, -SpiceBottleItem.getDurability(ret));
            });
        }
        return ret;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSlotLimit(int slot) {
        if (slot == slotCount) {
            return Items.AIR.getMaxStackSize();
        }
        BasicEntry<IItemHandler> pair = getLocalSlot(slot);
        return pair.getKey().getSlotLimit(pair.getIntValue());
    }

    @Override
    public void addMultiblock(KitchenMultiblock multiblock) {
        tryRefresh();
        IItemHandler handler = multiblock.x;
        if (handler == null || handler == this || handlers.contains(handler)) {
            return;
        }
        handlers.add(handler);
        int slots = handler.getSlots();
        slotCount += slots;
        for (int i = 0; i < slots; i++) {
            ItemStack container = handler.getStackInSlot(i);
            SpiceBottleItem.getSpice(container).ifPresent(spice -> {
                spiceCounts.addTo(spice, SpiceBottleItem.getDurability(container));
            });
        }
    }

    @Override
    public void removeMultiblock(KitchenMultiblock multiblock) {
        tryRefresh();
        IItemHandler handler = multiblock.x;
        if (handlers.remove(handler)) {
            int slots = handler.getSlots();
            slotCount -= slots;
            for (int i = 0; i < slots; i++) {
                ItemStack container = handler.getStackInSlot(i);
                SpiceBottleItem.getSpice(container).ifPresent(spice -> {
                    spiceCounts.addTo(spice, -SpiceBottleItem.getDurability(container));
                });
            }
        }
    }

    @Override
    public Object2IntMap<Spice> getSpices() {
        tryRefresh();
        return Object2IntMaps.unmodifiable(spiceCounts);
    }

    @Override
    public boolean useSpices(Object2IntMap<Spice> spices) {
        tryRefresh();
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void addSpice(Spice spice, int incr) {
        tryRefresh();
        int old = spiceCounts.getInt(spice);
        if (old + incr > 0) {
            spiceCounts.put(spice, old + incr);
        } else {
            spiceCounts.removeInt(spice);
        }
    }

    private void tryRefresh() {
        if (dataPackID != DeferredReloadListener.INSTANCE.getDataPackID()) {
            spiceCounts.clear();
            for (IItemHandler handler : handlers) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack container = handler.getStackInSlot(i);
                    SpiceBottleItem.getSpice(container).ifPresent(spice -> {
                        spiceCounts.addTo(spice, SpiceBottleItem.getDurability(container));
                    });
                }
            }
            dataPackID = DeferredReloadListener.INSTANCE.getDataPackID();
        }
        spiceCounts.values().removeIf((IntPredicate) i -> i <= 0);
    }
}
