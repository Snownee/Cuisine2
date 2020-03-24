package snownee.cuisine.base.client;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FocusableGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.gui.widget.list.ExtendedList.AbstractListEntry;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.cuisine.api.registry.Spice;

@OnlyIn(Dist.CLIENT)
public class SpicePicker extends FocusableGui {

    private boolean init;
    private Object2IntMap<Spice> spices = Object2IntMaps.EMPTY_MAP;
    private SpiceList list;
    private final List<IGuiEventListener> children = Lists.newArrayList();

    public SpicePicker() {
        list = new SpiceList(Minecraft.getInstance(), 60, 50, 50, 250, 300);
        list.setLeftPos(80);
        children.add(list);
    }

    public void render(int x, int y, int mouseX, int mouseY, float pTicks) {
        list.render(mouseX, mouseY, pTicks);
    }

    public void update(Object2IntMap<Spice> spices) {
        init = true;
        this.spices = spices;
        for (Spice spice : spices.keySet()) {
            list.addEntry(new SpiceEntry(spice));
        }
    }

    @Override
    public List<? extends IGuiEventListener> children() {
        return children;
    }

    @Override
    public IGuiEventListener getFocused() {
        return list;
    }

    public static class SpiceList extends ExtendedList<SpiceEntry> {
        public SpiceList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
            super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        }

        @Override
        public void render(int mouseX, int mouseY, float pTicks) {
            this.renderBackground();
            int i = this.getScrollbarPosition();
            int j = i + 6;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            RenderSystem.disableTexture();
            RenderSystem.color4f(.77F, .77F, .77F, 1);
            float f = 32.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(this.x0, this.y1, 0.0D).tex(this.x0 / 32.0F, (this.y1 + (int) this.getScrollAmount()) / 32.0F).endVertex();
            bufferbuilder.pos(this.x1, this.y1, 0.0D).tex(this.x1 / 32.0F, (this.y1 + (int) this.getScrollAmount()) / 32.0F).endVertex();
            bufferbuilder.pos(this.x1, this.y0, 0.0D).tex(this.x1 / 32.0F, (this.y0 + (int) this.getScrollAmount()) / 32.0F).endVertex();
            bufferbuilder.pos(this.x0, this.y0, 0.0D).tex(this.x0 / 32.0F, (this.y0 + (int) this.getScrollAmount()) / 32.0F).endVertex();
            tessellator.draw();

            int k = this.getRowLeft();
            int l = this.y0 + 4 - (int) this.getScrollAmount();
            if (this.renderHeader) {
                this.renderHeader(k, l, tessellator);
            }

            this.renderList(k, l, mouseX, mouseY, pTicks);
            RenderSystem.disableDepthTest();
            this.renderHoleBackground(0, this.y0, 255, 255);
            this.renderHoleBackground(this.y1, this.height, 255, 255);

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            RenderSystem.disableAlphaTest();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableTexture();

            int j1 = this.getMaxScroll();
            if (j1 > 0) {
                int k1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
                k1 = MathHelper.clamp(k1, 32, this.y1 - this.y0 - 8);
                int l1 = (int) this.getScrollAmount() * (this.y1 - this.y0 - k1) / j1 + this.y0;
                if (l1 < this.y0) {
                    l1 = this.y0;
                }

                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, this.y1, 0.0D).tex(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, this.y0, 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(i, this.y0, 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, l1 + k1, 0.0D).tex(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, l1 + k1, 0.0D).tex(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, l1, 0.0D).tex(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(i, l1, 0.0D).tex(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, l1 + k1 - 1, 0.0D).tex(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, l1, 0.0D).tex(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(i, l1, 0.0D).tex(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.renderDecorations(mouseX, mouseY);
            RenderSystem.enableTexture();
            RenderSystem.shadeModel(7424);
            RenderSystem.enableAlphaTest();
            RenderSystem.disableBlend();
        }

        private int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
        }

        @Override
        public int addEntry(SpiceEntry p_addEntry_1_) {
            return super.addEntry(p_addEntry_1_);
        }

        @Override
        public int getRowWidth() {
            return 50;
        }

        @Override
        protected int getScrollbarPosition() {
            return getRowLeft() + getRowWidth() + 1;
        }

        @Override
        protected void renderHoleBackground(int p_renderHoleBackground_1_, int p_renderHoleBackground_2_, int p_renderHoleBackground_3_, int p_renderHoleBackground_4_) {}
    }

    public static class SpiceEntry extends AbstractListEntry<SpiceEntry> {

        private final Spice spice;
        private final String text;
        private boolean checked;

        public SpiceEntry(Spice spice) {
            this.spice = spice;
            text = spice.getDisplayName().getFormattedText();
        }

        @Override
        public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {

            Minecraft.getInstance().fontRenderer.drawString(text, left, top, 0);
        }

    }
}
