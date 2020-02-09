package snownee.cuisine.debug;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.cuisine.CoreModule;

public final class DebugItemGroup extends ItemGroup {

    private final ItemStack icon;
    private final Collection<Item> stacks;

    public DebugItemGroup(String label, ItemStack icon, Collection<Item> stacks) {
        super("cuisine.debug." + label);
        this.icon = icon;
        this.stacks = stacks;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
        return icon;
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        stacks.stream().map(ItemStack::new).forEach(items::add);
    }

    @SuppressWarnings("unused")
    public static void init() {
        new DebugItemGroup("material", Items.CARROT.getDefaultInstance(), CoreModule.getAllMaterialItems());
        new DebugItemGroup("spice", Items.SUGAR.getDefaultInstance(), CoreModule.getAllSpiceItems());
        new DebugItemGroup("food", Items.CAKE.getDefaultInstance(), CoreModule.getAllFoodItems());
    }
}
