package snownee.kiwi.ui.client.widget;

import snownee.kiwi.ui.client.UIContext;

public class Root extends NestedWidget {

    public Root(UIContext ctx) {
        super(ctx);
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

}
