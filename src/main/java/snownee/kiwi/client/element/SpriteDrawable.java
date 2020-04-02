package snownee.kiwi.client.element;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SpriteDrawable implements IDrawable {
    private final TextureAtlasSprite sprite;
    private int trimLeft;
    private int trimRight;
    private int trimTop;
    private int trimBottom;

    public SpriteDrawable(TextureAtlasSprite sprite, int width, int height) {
        this.sprite = sprite;
        //        this.width = width;
        //        this.height = height;
    }

    // TODO
    //    public SpriteDrawable trim(int left, int right, int top, int bottom) {
    //        this.trimLeft = left;
    //        this.trimRight = right;
    //        this.trimTop = top;
    //        this.trimBottom = bottom;
    //        return this;
    //    }
    //
    //    public int getWidth() {
    //        return width;
    //    }
    //
    //    public int getHeight() {
    //        return height;
    //    }
    //
    //    public void draw(int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
    //        int textureWidth = this.width;
    //        int textureHeight = this.height;
    //
    //        sprite.getAtlasTexture().bindTexture();
    //
    //        maskTop += trimTop;
    //        maskBottom += trimBottom;
    //        maskLeft += trimLeft;
    //        maskRight += trimRight;
    //
    //        int x = xOffset + maskLeft;
    //        int y = yOffset + maskTop;
    //        int width = textureWidth - maskRight - maskLeft;
    //        int height = textureHeight - maskBottom - maskTop;
    //        float uSize = sprite.getMaxU() - sprite.getMinU();
    //        float vSize = sprite.getMaxV() - sprite.getMinV();
    //
    //        float minU = sprite.getMinU() + uSize * (maskLeft / (float) textureWidth);
    //        float minV = sprite.getMinV() + vSize * (maskTop / (float) textureHeight);
    //        float maxU = sprite.getMaxU() - uSize * (maskRight / (float) textureWidth);
    //        float maxV = sprite.getMaxV() - vSize * (maskBottom / (float) textureHeight);
    //
    //        Tessellator tessellator = Tessellator.getInstance();
    //        BufferBuilder bufferBuilder = tessellator.getBuffer();
    //        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
    //        bufferBuilder.pos(x, y + height, 0).tex(minU, maxV).endVertex();
    //        bufferBuilder.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
    //        bufferBuilder.pos(x + width, y, 0).tex(maxU, minV).endVertex();
    //        bufferBuilder.pos(x, y, 0).tex(minU, minV).endVertex();
    //        tessellator.draw();
    //    }

    @Override
    public void draw(float left, float top, float width, float height, float pTicks) {
        sprite.getAtlasTexture().bindTexture();

        float x = left + trimLeft;
        float y = top + trimTop;
        width += -trimRight - trimLeft;
        height += -trimBottom - trimTop;
        float uSize = sprite.getMaxU() - sprite.getMinU();
        float vSize = sprite.getMaxV() - sprite.getMinV();

        float minU = sprite.getMinU() + uSize * (trimLeft / width);
        float minV = sprite.getMinV() + vSize * (trimTop / height);
        float maxU = sprite.getMaxU() - uSize * (trimRight / width);
        float maxV = sprite.getMaxV() - vSize * (trimBottom / height);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x, y + height, 0).tex(minU, maxV).endVertex();
        bufferBuilder.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
        bufferBuilder.pos(x + width, y, 0).tex(maxU, minV).endVertex();
        bufferBuilder.pos(x, y, 0).tex(minU, minV).endVertex();
        tessellator.draw();
    }
}
