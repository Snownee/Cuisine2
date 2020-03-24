package snownee.kiwi.client.element;

import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IStaticRenderable extends IRenderable {
    void render(int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight);
}
