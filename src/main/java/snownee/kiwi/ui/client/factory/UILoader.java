package snownee.kiwi.ui.client.factory;

import java.util.Map;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Maps;

import snownee.kiwi.Kiwi;
import snownee.kiwi.ui.client.UIContext;
import snownee.kiwi.ui.client.widget.NestedWidget;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.Widget;
import third_party.com.facebook.yoga.YogaValue;

public class UILoader {
    public static final UILoader INSTANCE = new UILoader();
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

    public Root<? extends Root> load(Document doc, UIContext ctx) {
        Root<? extends Root> root = parseRecursively(doc.getDocumentElement(), ctx);
        return root;
    }

    public <T extends Widget> T parseRecursively(Element element, UIContext ctx) {
        T widget = parse(element, ctx);
        widget.node.SetMinWidth(YogaValue.Pt(50));
        widget.node.SetMinHeight(YogaValue.Pt(50));
        if (element.hasChildNodes()) {
            boolean nested = widget instanceof NestedWidget;
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                short type = child.getNodeType();
                switch (type) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    String text = ((CharacterData) child).getData();
                    if (text.trim().isEmpty()) {
                        continue;
                    }
                    text = text.replaceAll("[\t\n\r]", "");
                    if (widget.text == null) {
                        widget.text = text;
                    } else {
                        widget.text += text;
                    }
                    break;
                case Node.ELEMENT_NODE:
                    if (nested) {
                        Element childElement = (Element) child;
                        ((NestedWidget) widget).addChild(parseRecursively(childElement, ctx));
                    } else {
                        Kiwi.logger.error("{} is not a nested widget, cannot insert child node into it.");
                    }
                    break;
                case Node.ATTRIBUTE_NODE:
                    break;
                default:
                    System.out.println(child);
                    break;
                }
            }
        }
        return widget;
    }

    public <T extends Widget> T parse(Element element, UIContext ctx) {
        String name = element.getTagName();
        WidgetFactory factory = factories.getOrDefault(name, DivisionFactory.INSTANCE);
        Widget widget = factory.parse(element, ctx);
        return (T) widget;
    }
}
