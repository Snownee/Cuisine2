package snownee.cuisine.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import snownee.cuisine.api.FoodBuilder;

@Cancelable
public class FoodCookedEvent extends Event {
    public ItemStack output;
    public FoodBuilder<?> foodBuilder;

    public FoodCookedEvent(FoodBuilder<?> foodBuilder, ItemStack output) {
        this.foodBuilder = foodBuilder;
        this.output = output;
    }
}
