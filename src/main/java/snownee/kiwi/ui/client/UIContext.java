package snownee.kiwi.ui.client;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import snownee.kiwi.ui.client.widget.Root;

public class UIContext {
    public Minecraft mc = Minecraft.getInstance();
    @Nullable
    public Screen screen;
    @Nullable
    public Container container;
    public Root<? extends Root> root;
}
