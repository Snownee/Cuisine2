package snownee.kiwi.ui.client.widget;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import snownee.kiwi.ui.client.UIContext;

public class NestedWidget<T extends NestedWidget<T>> extends Widget<T> implements INestedGuiEventHandler {

    public NestedWidget(UIContext ctx) {
        super(ctx);
    }

    protected final List<Widget> children = Lists.newArrayList();

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        super.draw(mouseX, mouseY, pTicks);
        for (Widget child : children) {
            if (child.visible) {
                child.draw(mouseX, mouseY, pTicks);
            }
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
    public Optional<IGuiEventListener> getEventListenerForPos(double mouseX, double mouseY) {
        for (Widget child : children()) {
            if (child.visible && child.isMouseOver(mouseX, mouseY)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    public Optional<Widget> getDeepWidgetForPos(double mouseX, double mouseY) {
        for (Widget child : children()) {
            if (child.visible && child.isMouseOver(mouseX, mouseY)) {
                if (child instanceof NestedWidget) {
                    Optional<Widget> optional = ((NestedWidget) child).getDeepWidgetForPos(mouseX, mouseY);
                    if (optional.isPresent()) {
                        return optional;
                    }
                }
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        /* off */
        return getEventListenerForPos(mouseX, mouseY)
                .filter(widget -> widget.mouseClicked(mouseX, mouseY, type))
                .map(widget -> {
                    this.setFocused(widget);
                    if (type == 0) {
                        this.setDragging(true);
                    }
                    return widget;
                })
                .isPresent();
        /* on */
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int type) {
        this.setDragging(false);
        return this.getEventListenerForPos(mouseX, mouseY).filter((p_212931_5_) -> {
            return p_212931_5_.mouseReleased(mouseX, mouseY, type);
        }).isPresent();
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {}

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
    public void init() {
        super.init();
        for (Widget child : children) {
            child.init();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Widget child : children) {
            child.destroy();
        }
    }

}
