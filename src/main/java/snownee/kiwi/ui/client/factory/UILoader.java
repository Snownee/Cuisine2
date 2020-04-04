package snownee.kiwi.ui.client.factory;

import java.util.Map;

import com.google.common.collect.Maps;

public class UILoader {
    public final Map<String, WidgetFactory> factories = Maps.newHashMap();

    public UILoader() {
        init();
    }

    protected void init() {
        factories.put("kxml", RootFactory.INSTANCE);
        factories.put("div", DivisionFactory.INSTANCE);
        factories.put("slot", SlotFactory.INSTANCE);
        factories.put("button", ButtonFactory.INSTANCE);
    }
}
