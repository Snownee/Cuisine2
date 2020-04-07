package snownee.kiwi.ui.client;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import snownee.kiwi.Kiwi;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.Widget;
import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssElementCombinatorPairsToXpath;
import third_party.org.sam.rosenthal.cssselectortoxpath.utilities.CssSelectorToXPathConverterException;

public class UIContext {
    private static final CssElementCombinatorPairsToXpath SELECTOR_CONVERTER = new CssElementCombinatorPairsToXpath();
    public final Minecraft mc = Minecraft.getInstance();
    @Nullable
    public Screen screen;
    @Nullable
    public Container container;
    public Root<? extends Root> root;
    public final Document doc;
    public final BiMap<Element, Widget> elementMap = HashBiMap.create();

    public UIContext(Document doc) {
        this.doc = doc;
    }

    @Nullable
    public <T extends Widget<T>> T getWidgetById(String id) {
        Element element = doc.getElementById(id);
        if (element == null) {
            return null;
        }
        return (T) elementMap.get(element);
    }

    @Nullable
    public <T extends Widget<T>> List<T> getWidgetsBySelector(String selector) {
        try {
            String expression = SELECTOR_CONVERTER.convertCssSelectorStringToXpathString(selector);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            return nodeListToWidgetList(nodeList);
        } catch (CssSelectorToXPathConverterException | XPathExpressionException e) {
            Kiwi.logger.catching(e);
        }
        return Collections.EMPTY_LIST;
    }

    public <T extends Widget<T>> List<T> nodeListToWidgetList(NodeList nodeList) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            builder.add((T) elementMap.get(nodeList.item(i)));
        }
        return builder.build();
    }
}
