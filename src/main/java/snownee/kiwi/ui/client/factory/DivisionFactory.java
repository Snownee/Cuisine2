package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.Widget;

public enum DivisionFactory implements WidgetFactory<Widget> {

    INSTANCE;

    @Override
    public Widget parse(Element element, UIContext ctx) {
        return new Widget(ctx);
    }

    @Override
    public ElementContentType getContentType() {
        return ElementContentType.ELEMENT;
    }

}
