package snownee.kiwi.client.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import snownee.kiwi.client.KiwiSpriteUploader;

public class NineSliceDrawable implements IDrawable {
    private static final KiwiSpriteUploader spriteUploader = KiwiSpriteUploader.GUI_ATLAS;
    private final ResourceLocation location;
    private final int width;
    private final int height;
    private final int sliceLeft;
    private final int sliceRight;
    private final int sliceTop;
    private final int sliceBottom;

    public NineSliceDrawable(ResourceLocation location, int width, int height, int left, int right, int top, int bottom) {
        this.location = location;

        this.width = width;
        this.height = height;
        this.sliceLeft = left;
        this.sliceRight = right;
        this.sliceTop = top;
        this.sliceBottom = bottom;
    }

    @Override
    public void draw(float left, float top, float width, float height, float pTicks) {
        TextureAtlasSprite sprite = spriteUploader.getSprite(location);
        int leftWidth = sliceLeft;
        int rightWidth = sliceRight;
        int topHeight = sliceTop;
        int bottomHeight = sliceBottom;
        int textureWidth = this.width;
        int textureHeight = this.height;

        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        textureManager.bindTexture(KiwiSpriteUploader.LOCATION_GUI_TEXTURE);

        float uMin = sprite.getMinU();
        float uMax = sprite.getMaxU();
        float vMin = sprite.getMinV();
        float vMax = sprite.getMaxV();
        float uSize = uMax - uMin;
        float vSize = vMax - vMin;

        float uLeft = uMin + uSize * (leftWidth / (float) textureWidth);
        float uRight = uMax - uSize * (rightWidth / (float) textureWidth);
        float vTop = vMin + vSize * (topHeight / (float) textureHeight);
        float vBottom = vMax - vSize * (bottomHeight / (float) textureHeight);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        // left top
        draw(bufferBuilder, uMin, vMin, uLeft, vTop, left, top, leftWidth, topHeight);
        // left bottom
        draw(bufferBuilder, uMin, vBottom, uLeft, vMax, left, top + height - bottomHeight, leftWidth, bottomHeight);
        // right top
        draw(bufferBuilder, uRight, vMin, uMax, vTop, left + width - rightWidth, top, rightWidth, topHeight);
        // right bottom
        draw(bufferBuilder, uRight, vBottom, uMax, vMax, left + width - rightWidth, top + height - bottomHeight, rightWidth, bottomHeight);

        int middleWidth = textureWidth - leftWidth - rightWidth;
        int middleHeight = textureWidth - topHeight - bottomHeight;
        float tiledMiddleWidth = width - leftWidth - rightWidth;
        float tiledMiddleHeight = height - topHeight - bottomHeight;
        if (tiledMiddleWidth > 0) {
            // top edge
            drawTiled(bufferBuilder, uLeft, vMin, uRight, vTop, left + leftWidth, top, tiledMiddleWidth, topHeight, middleWidth, topHeight);
            // bottom edge
            drawTiled(bufferBuilder, uLeft, vBottom, uRight, vMax, left + leftWidth, top + height - bottomHeight, tiledMiddleWidth, bottomHeight, middleWidth, bottomHeight);
        }
        if (tiledMiddleHeight > 0) {
            // left side
            drawTiled(bufferBuilder, uMin, vTop, uLeft, vBottom, left, top + topHeight, leftWidth, tiledMiddleHeight, leftWidth, middleHeight);
            // right side
            drawTiled(bufferBuilder, uRight, vTop, uMax, vBottom, left + width - rightWidth, top + topHeight, rightWidth, tiledMiddleHeight, rightWidth, middleHeight);
        }
        if (tiledMiddleHeight > 0 && tiledMiddleWidth > 0) {
            // middle area
            drawTiled(bufferBuilder, uLeft, vTop, uRight, vBottom, left + leftWidth, top + topHeight, tiledMiddleWidth, tiledMiddleHeight, middleWidth, middleHeight);
        }

        tessellator.draw();
    }

    private static void drawTiled(BufferBuilder bufferBuilder, float uMin, float vMin, float uMax, float vMax, float xOffset, float yOffset, float tiledWidth, float tiledHeight, float width, float height) {
        float xTileCount = tiledWidth / width;
        float xRemainder = tiledWidth - (xTileCount * width);
        float yTileCount = tiledHeight / height;
        float yRemainder = tiledHeight - (yTileCount * height);

        float yStart = yOffset + tiledHeight;

        float uSize = uMax - uMin;
        float vSize = vMax - vMin;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                float tileWidth = (xTile == xTileCount) ? xRemainder : width;
                float tileHeight = (yTile == yTileCount) ? yRemainder : height;
                float x = xOffset + (xTile * width);
                float y = yStart - ((yTile + 1) * height);
                if (tileWidth > 0 && tileHeight > 0) {
                    float maskRight = width - tileWidth;
                    float maskTop = height - tileHeight;
                    float uOffset = (maskRight / width) * uSize;
                    float vOffset = (maskTop / height) * vSize;

                    draw(bufferBuilder, uMin, vMin + vOffset, uMax - uOffset, vMax, x, y + maskTop, tileWidth, tileHeight);
                }
            }
        }
    }

    private static void draw(BufferBuilder bufferBuilder, float minU, float minV, float maxU, float maxV, float xOffset, float yOffset, float width, float height) {
        bufferBuilder.pos(xOffset, yOffset + height, 0).tex(minU, maxV).endVertex();
        bufferBuilder.pos(xOffset + width, yOffset + height, 0).tex(maxU, maxV).endVertex();
        bufferBuilder.pos(xOffset + width, yOffset, 0).tex(maxU, minV).endVertex();
        bufferBuilder.pos(xOffset, yOffset, 0).tex(minU, minV).endVertex();
    }
}
