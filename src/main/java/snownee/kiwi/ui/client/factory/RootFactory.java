package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.Root;

public enum RootFactory implements WidgetFactory<Root> {

    INSTANCE;

    @Override
    public Root parse(Element element, UIContext ctx) {
        return new Root(ctx);
    }

    @Override
    public ElementContentType getContentType() {
        return ElementContentType.ELEMENT;
    }

}
