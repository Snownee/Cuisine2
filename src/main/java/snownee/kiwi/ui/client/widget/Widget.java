package snownee.kiwi.ui.client.widget;

import net.minecraft.client.gui.IGuiEventListener;
import snownee.kiwi.client.DrawUtil;
import third_party.com.facebook.yoga.YogaNode;

public class Widget implements IGuiEventListener {
    public final YogaNode node;
    public int color;
    public float left;
    public float top;
    public float right;
    public float bottom;

    public Widget() {
        node = new YogaNode();
    }

    public void draw(float pTicks) {
        DrawUtil.fill(left, top, right, bottom, color);
    }

    public Widget getOwner() {
        return (Widget) node._data;
    }

    public void done() {
        Widget owner = getOwner();
        left = node.GetLayoutX() + (owner == null ? 0 : owner.left);
        top = node.GetLayoutY() + (owner == null ? 0 : owner.top);
        right = left + node.GetLayoutWidth();
        bottom = top + node.GetLayoutHeight();
    }
}
