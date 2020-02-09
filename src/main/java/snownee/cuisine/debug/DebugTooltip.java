package snownee.cuisine.debug;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.config.CuisineCommonConfig;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public final class DebugTooltip {
    private DebugTooltip() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!CuisineCommonConfig.debug || !event.getFlags().isAdvanced()) {
            return;
        }
        List<ITextComponent> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();
        append(tooltip, CuisineAPI.findMaterial(stack), Material::getTags, TextFormatting.YELLOW);
        append(tooltip, CuisineAPI.findSpice(stack), Spice::getTags, TextFormatting.LIGHT_PURPLE);
    }

    private static <T extends IForgeRegistryEntry<T>> void append(List<ITextComponent> tooltip, Optional<T> result, Function<T, Set<ResourceLocation>> tagsProvider, TextFormatting color) {
        if (!result.isPresent()) {
            return;
        }
        tooltip.add(new StringTextComponent(result.get().getRegistryName().toString()).applyTextStyle(color));
        for (ResourceLocation id : tagsProvider.apply(result.get())) {
            tooltip.add(new StringTextComponent("#" + id.toString()).applyTextStyle(color));
        }
    }
}
