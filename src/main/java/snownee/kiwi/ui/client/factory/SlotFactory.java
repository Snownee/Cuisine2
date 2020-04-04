package snownee.kiwi.ui.client.factory;

import org.w3c.dom.Element;

import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.SlotWidget;

public enum SlotFactory implements WidgetFactory<SlotWidget> {

    INSTANCE;

    @Override
    public SlotWidget parse(Element element, UIContext ctx) {
        if (ctx.container == null) {
            // TODO
        }
        return new SlotWidget(ctx);
    }

}
