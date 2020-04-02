package snownee.kiwi.ui.client.widget;

import net.minecraft.inventory.container.Slot;
import snownee.kiwi.client.element.IDrawable;
import snownee.kiwi.client.element.NineSliceDrawable;
import snownee.kiwi.ui.client.UIContext;

public class SlotWidget extends Widget {
    //new ResourceLocation(Kiwi.MODID, "textures/gui/slot.png")
    //public static final IDrawable SLOT_BACKGROUND = new NineSliceDrawable(null, 3, 3, 1, 2, 1, 2);
    protected Slot slot;

    public SlotWidget(UIContext ctx) {
        super(ctx);
        background = SLOT_BACKGROUND;
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        super.draw(mouseX, mouseY, pTicks);
        if (isHovering(mouseX, mouseY)) {

        }
    }

}
