package snownee.kiwi.ui.client.widget;

import javax.annotation.Nullable;

import com.google.common.base.Strings;

import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.element.IDrawable;
import snownee.kiwi.ui.client.UIContext;

public class Button<T extends Button<T>> extends Widget<T> {
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
        if (!Strings.isNullOrEmpty(text)) {
            ctx.screen.drawCenteredString(ctx.mc.fontRenderer, text, (int) (left + right) / 2, (int) (top + bottom - ctx.mc.fontRenderer.FONT_HEIGHT) / 2, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        if (bus.fire("click")) {
            playDownSound(ctx.mc.getSoundHandler());
            return true;
        } else {
            return false;
        }
    }
}
