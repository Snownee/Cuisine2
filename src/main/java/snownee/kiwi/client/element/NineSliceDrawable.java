package snownee.kiwi.client.element;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import snownee.kiwi.client.DrawMode;
import snownee.kiwi.client.DrawUtil;

public class NineSliceDrawable implements IDrawable {
    private final TextureAtlasSprite sprite;
    private final float width;
    private final float height;
    private final float sliceLeft;
    private final float sliceRight;
    private final float sliceTop;
    private final float sliceBottom;
    private final DrawMode mode;

    public NineSliceDrawable(TextureAtlasSprite sprite, float width, float height, float sliceLeft, float sliceRight, float sliceTop, float sliceBottom, DrawMode mode) {
        this.sprite = sprite;
        this.width = width;
        this.height = height;
        this.sliceLeft = sliceLeft;
        this.sliceRight = sliceRight;
        this.sliceTop = sliceTop;
        this.sliceBottom = sliceBottom;
        this.mode = mode;
    }

    @Override
    public void draw(float left, float top, float width, float height, float pTicks) {
        float leftWidth = sliceLeft;
        float rightWidth = sliceRight;
        float topHeight = sliceTop;
        float bottomHeight = sliceBottom;
        float textureWidth = this.width;
        float textureHeight = this.height;

        sprite.getAtlasTexture().bindTexture();

        float uMin = sprite.getMinU();
        float uMax = sprite.getMaxU();
        float vMin = sprite.getMinV();
        float vMax = sprite.getMaxV();
        float uSize = uMax - uMin;
        float vSize = vMax - vMin;

        float uLeft = uMin + uSize * (leftWidth / textureWidth);
        float uRight = uMax - uSize * (rightWidth / textureWidth);
        float vTop = vMin + vSize * (topHeight / textureHeight);
        float vBottom = vMax - vSize * (bottomHeight / textureHeight);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        // left top
        DrawUtil.draw(bufferBuilder, uMin, vMin, uLeft, vTop, left, top, leftWidth, topHeight);
        // left bottom
        DrawUtil.draw(bufferBuilder, uMin, vBottom, uLeft, vMax, left, top + height - bottomHeight, leftWidth, bottomHeight);
        // right top
        DrawUtil.draw(bufferBuilder, uRight, vMin, uMax, vTop, left + width - rightWidth, top, rightWidth, topHeight);
        // right bottom
        DrawUtil.draw(bufferBuilder, uRight, vBottom, uMax, vMax, left + width - rightWidth, top + height - bottomHeight, rightWidth, bottomHeight);

        float middleWidth = textureWidth - leftWidth - rightWidth;
        float middleHeight = textureWidth - topHeight - bottomHeight;
        float tiledMiddleWidth = width - leftWidth - rightWidth;
        float tiledMiddleHeight = height - topHeight - bottomHeight;
        if (tiledMiddleWidth > 0) {
            // top edge
            DrawUtil.draw(bufferBuilder, uLeft, vMin, uRight, vTop, left + leftWidth, top, tiledMiddleWidth, topHeight, middleWidth, topHeight, mode);
            // bottom edge
            DrawUtil.draw(bufferBuilder, uLeft, vBottom, uRight, vMax, left + leftWidth, top + height - bottomHeight, tiledMiddleWidth, bottomHeight, middleWidth, bottomHeight, mode);
        }
        if (tiledMiddleHeight > 0) {
            // left side
            DrawUtil.draw(bufferBuilder, uMin, vTop, uLeft, vBottom, left, top + topHeight, leftWidth, tiledMiddleHeight, leftWidth, middleHeight, mode);
            // right side
            DrawUtil.draw(bufferBuilder, uRight, vTop, uMax, vBottom, left + width - rightWidth, top + topHeight, rightWidth, tiledMiddleHeight, rightWidth, middleHeight, mode);
        }
        if (tiledMiddleHeight > 0 && tiledMiddleWidth > 0) {
            // middle area
            DrawUtil.draw(bufferBuilder, uLeft, vTop, uRight, vBottom, left + leftWidth, top + topHeight, tiledMiddleWidth, tiledMiddleHeight, middleWidth, middleHeight, mode);
        }

        tessellator.draw();
    }

}
