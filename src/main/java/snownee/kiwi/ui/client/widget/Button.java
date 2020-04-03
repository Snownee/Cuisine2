package snownee.kiwi.ui.client.widget;

import javax.annotation.Nullable;

import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.element.IDrawable;
import snownee.kiwi.ui.client.UIContext;

public class Button extends Widget {
    @Nullable
    public IDrawable activeBackground = DefaultDrawables.getBtnActive();
    @Nullable
    public IDrawable disabledBackground = DefaultDrawables.getBtnDisabled();
    public boolean disabled;

    public Button(UIContext ctx) {
        super(ctx);
        background = DefaultDrawables.getBtn();
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        IDrawable drawable = null;
        if (disabled) {
            drawable = disabledBackground;
        } else if (isMouseOver(mouseX, mouseY)) {
            drawable = activeBackground;
        }
        if (drawable == null) {
            drawable = background;
        }
        if (drawable != null) {
            drawable.draw(left, top, node.GetLayoutWidth(), node.GetLayoutHeight(), pTicks);
        }
    }
}
