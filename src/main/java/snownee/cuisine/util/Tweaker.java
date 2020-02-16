package snownee.cuisine.util;

import java.util.Set;

import com.google.common.collect.Sets;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import snownee.cuisine.data.network.SSyncStackSizePacket;

@EventBusSubscriber
public final class Tweaker {
    private Tweaker() {}

    private static Set<ResourceLocation> disabledRecipes = Sets.newHashSet();
    private static Set<Item> noContainerItems = Sets.newIdentityHashSet();
    private static Object2IntLinkedOpenHashMap<Item> undoStackSize = new Object2IntLinkedOpenHashMap();
    private static Object2IntLinkedOpenHashMap<Item> newStackSize = new Object2IntLinkedOpenHashMap();

    public synchronized static void disableRecipe(ResourceLocation id) {
        if (id != null) {
            disabledRecipes.add(id);
        }
    }

    public static boolean isRecipeDisabled(ResourceLocation id) {
        return disabledRecipes.contains(id);
    }

    public synchronized static void disableContainer(IItemProvider item) {
        Item realItem = item.asItem();
        if (realItem != null) {
            noContainerItems.add(realItem);
        }
    }

    public static void clearRecipes() {
        disabledRecipes.clear();
    }

    public static void clear() {
        noContainerItems.clear();
        undoStackSize.keySet().forEach(item -> item.maxStackSize = undoStackSize.getInt(item));
        undoStackSize.clear();
        newStackSize.clear();
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof PlayerEntity && !((PlayerEntity) event.getEntityLiving()).abilities.isCreativeMode) {
            ItemStack stack = event.getItem();
            if (stack.getItem().isFood() && noContainerItems.contains(stack.getItem())) {
                stack.shrink(1);
                event.setResultStack(stack);
            }
        }
    }

    public synchronized static void setStackSize(Item item, int size) {
        undoStackSize.put(item, item.maxStackSize);
        newStackSize.put(item, size);
        item.maxStackSize = size;
    }

    public static void sync(ServerPlayerEntity player) {
        new SSyncStackSizePacket(newStackSize).send(player);
    }
}
