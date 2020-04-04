package snownee.kiwi.ui.client.widget;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.ui.client.UIContext;
import third_party.com.facebook.yoga.YogaValue;

public class SlotWidget<T extends SlotWidget<T>> extends Widget<T> {
    protected Slot slot;

    public SlotWidget(UIContext ctx) {
        super(ctx);
        background = DefaultDrawables.getSlot();
        this.node.SetWidth(YogaValue.Pt(18));
        this.node.SetHeight(YogaValue.Pt(18));
        this.node.SetMaxWidth(YogaValue.Pt(18));
        this.node.SetMaxHeight(YogaValue.Pt(18));
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        super.draw(mouseX, mouseY, pTicks);
        if (isMouseOver(mouseX, mouseY)) {
            //TODO draw highlight
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
