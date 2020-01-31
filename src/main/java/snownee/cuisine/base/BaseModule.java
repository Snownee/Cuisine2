package snownee.cuisine.base;

import net.minecraft.item.crafting.IRecipeSerializer;
import snownee.cuisine.base.crafting.SpiceBottleFillingRecipe;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.KiwiModule.Subscriber.Bus;

@KiwiModule(name = "base", dependencies = "@core")
@KiwiModule.Group("decorations")
@KiwiModule.Subscriber(Bus.MOD)
public class BaseModule extends AbstractModule {
    public static final SpiceBottleItem SPICE_BOTTLE = new SpiceBottleItem(50, itemProp());

    public static final IRecipeSerializer<?> SPICE_BOTTLE_FILL = new SpiceBottleFillingRecipe.Serializer();
}
