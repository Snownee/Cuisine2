package snownee.kiwi.ui.client;

import javax.annotation.Nullable;

import org.w3c.dom.Element;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.Widget;

public class UIContext {
    public final Minecraft mc = Minecraft.getInstance();
    @Nullable
    public Screen screen;
    @Nullable
    public Container container;
    public Root<? extends Root> root;
    public final BiMap<Element, Widget> elementMap = HashBiMap.create();
}
