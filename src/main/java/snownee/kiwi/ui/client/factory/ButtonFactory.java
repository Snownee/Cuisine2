package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.Button;

public enum ButtonFactory implements WidgetFactory<Button> {

    INSTANCE;

    @Override
    public Button parse(Element element, UIContext ctx) {
        return new Button(ctx);
    }

}
