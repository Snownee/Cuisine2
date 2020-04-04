package snownee.kiwi.ui.client.widget;

import snownee.kiwi.ui.client.UIContext;

public class Root<T extends Root<T>> extends NestedWidget<T> {

    public Root(UIContext ctx) {
        super(ctx);
        ctx.root = this;
    }

    @Override
    public void init() {
        node.CalculateLayout();
        super.init();
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        if (ctx != null) {
            ctx.screen.renderBackground();
        }
        super.draw(mouseX, mouseY, pTicks);
    }

    @Override
    public void destroy() {
        super.destroy();
        ctx.root = null;
    }

}
