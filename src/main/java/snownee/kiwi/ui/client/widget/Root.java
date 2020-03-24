package snownee.kiwi.ui.client.widget;

public class Root extends NestedWidget {

    @Override
    public void draw(float pTicks) {
        super.draw(pTicks);
    }

    @Override
    public void done() {
        node.CalculateLayout();
        super.done();
    }
}
