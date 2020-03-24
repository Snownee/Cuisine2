package snownee.cuisine.base.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.ui.client.KiwiScreen;

public class ManualItem extends ModItem {

    public ManualItem() {
        super(new Item.Properties().maxStackSize(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Minecraft.getInstance().displayGuiScreen(new KiwiScreen());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
