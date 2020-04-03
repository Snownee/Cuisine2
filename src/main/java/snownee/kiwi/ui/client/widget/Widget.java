package snownee.kiwi.ui.client.widget;

import javax.annotation.Nullable;

import com.google.common.base.Strings;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import snownee.kiwi.client.element.IDrawable;
import snownee.kiwi.ui.client.UIContext;
import third_party.com.facebook.yoga.YogaNode;

public class Widget implements IGuiEventListener {
    public final YogaNode node;
    public final UIContext ctx;
    public float left;
    public float top;
    public float right;
    public float bottom;
    public IDrawable background;
    public boolean visible = true;

    public Widget(UIContext ctx) {
        node = new YogaNode();
        this.ctx = ctx;
    }

    public void draw(int mouseX, int mouseY, float pTicks) {
        if (background != null) {
            background.draw(left, top, node.GetLayoutWidth(), node.GetLayoutHeight(), pTicks);
        }
    }

    public Widget getOwner() {
        return (Widget) node._data;
    }

    public void init() {
        Widget owner = getOwner();
        left = node.GetLayoutX() + (owner == null ? 0 : owner.left);
        top = node.GetLayoutY() + (owner == null ? 0 : owner.top);
        right = left + node.GetLayoutWidth();
        bottom = top + node.GetLayoutHeight();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return visible && mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
    }

    @Nullable
    public String getMesseage() {
        return null;
    }

    public void narrate() {
        String s = getMesseage();
        if (!Strings.isNullOrEmpty(s)) {
            NarratorChatListener.INSTANCE.say(s);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        System.out.println(this);
        return false;
    }
}
