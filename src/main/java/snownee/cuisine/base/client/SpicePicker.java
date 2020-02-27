package snownee.cuisine.base.client;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.cuisine.api.registry.Spice;

@OnlyIn(Dist.CLIENT)
public class SpicePicker extends AbstractGui implements INestedGuiEventHandler {

    private boolean init;
    private Object2IntMap<Spice> spices = Object2IntMaps.EMPTY_MAP;

    public void render(int x, int y, int mouseX, int mouseY, float pTicks) {
        int yPos = 0;
        for (Entry<Spice> e : Object2IntMaps.fastIterable(spices)) {
            String text = e.getKey().getDisplayName().getFormattedText();
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(text, x, y + yPos, 0xFFFFFF);
            yPos += 20;
        }
    }

    public void update(Object2IntMap<Spice> spices) {
        init = true;
        this.spices = spices;
    }

    @Override
    public List<? extends IGuiEventListener> children() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isDragging() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setDragging(boolean p_setDragging_1_) {
        // TODO Auto-generated method stub

    }

    @Override
    public IGuiEventListener getFocused() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFocused(IGuiEventListener p_setFocused_1_) {
        // TODO Auto-generated method stub

    }

}
