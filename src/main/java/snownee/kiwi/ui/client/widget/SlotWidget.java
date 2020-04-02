package snownee.kiwi.ui.client.widget;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.ui.client.UIContext;
import third_party.com.facebook.yoga.YogaValue;

public class SlotWidget extends Widget {
    protected Slot slot;

    public SlotWidget(UIContext ctx) {
        super(ctx);
        AtlasTexture i = this.ctx.mc.getModelManager().getAtlasTexture(new ResourceLocation("minecraft", "textures/atlas/blocks.png"));
        TextureAtlasSprite p = (i.getSprite(new ResourceLocation("minecraft", "item/apple")));
        System.out.println(p);
        background = DefaultDrawables.getSlot();
        this.node.SetWidth(YogaValue.Pt(16));
        this.node.SetHeight(YogaValue.Pt(16));
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        super.draw(mouseX, mouseY, pTicks);
        System.out.println("" + this.left + this.right);
        System.out.println(("" + mouseX + mouseY));
        if (isHovering(mouseX, mouseY)) {
            if (this.ctx.mc.currentScreen != null)
                this.ctx.mc.currentScreen.renderTooltip(addInformation(ITooltipFlag.TooltipFlags.NORMAL), mouseX, mouseY);
        }
    }

    public List<String> addInformation(ITooltipFlag flagIn) {
        List<String> list1 = Lists.newArrayList();
        if (slot != null) {
            ItemStack itemStack = slot.inventory.getStackInSlot(slot.getSlotIndex());
            List<ITextComponent> list = itemStack.getTooltip(null, flagIn);
            for (ITextComponent itextcomponent : list) {
                list1.add(itextcomponent.getFormattedText());
            }
        }
        list1.add("none");
        return list1;
    }

}
