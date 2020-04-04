package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.Widget;

public interface WidgetFactory<T extends Widget> {

    default ElementContentType getContentType() {
        return ElementContentType.NONE;
    }

    T parse(Element element, UIContext ctx);

}
