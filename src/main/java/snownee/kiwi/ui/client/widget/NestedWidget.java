package snownee.kiwi.ui.client.widget;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public class NestedWidget extends Widget implements INestedGuiEventHandler {

    protected final List<Widget> children = Lists.newArrayList();

    @Override
    public void draw(float pTicks) {
        super.draw(pTicks);
        for (Widget child : children) {
            child.draw(pTicks);
        }
    }

    public void addChild(int index, Widget child) {
        children.add(index, child);
        node.Insert(index, child.node);
        child.node._data = this;
    }

    @Override
    public List<Widget> children() {
        return children;
    }

    @Override
    public boolean isDragging() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {
        // TODO Auto-generated method stub

    }

    @Override
    public Widget getFocused() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFocused(IGuiEventListener p_setFocused_1_) {
        // TODO Auto-generated method stub

    }

    @Override
    public void done() {
        super.done();
        for (Widget child : children) {
            child.done();
        }
    }

}
