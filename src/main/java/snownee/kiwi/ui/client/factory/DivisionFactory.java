package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.NestedWidget;

public enum DivisionFactory implements WidgetFactory<NestedWidget> {

    INSTANCE;

    @Override
    public NestedWidget parse(Element element, UIContext ctx) {
        return new NestedWidget(ctx);
    }

}
