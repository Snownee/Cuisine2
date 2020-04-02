package snownee.kiwi.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public final class DrawUtil {
    private static int blitOffset;

    private DrawUtil() {}

    public static void fill(float x, float y, float width, float height, int color) {
        fill(TransformationMatrix.identity().getMatrix(), x, y, width, height, color);
    }

    public static void fill(Matrix4f matrix, float x, float y, float width, float height, int color) {
        if (x < width) {
            float i = x;
            x = width;
            width = i;
        }

        if (y < height) {
            float j = y;
            y = height;
            height = j;
        }

        float f3 = (color >> 24 & 255) / 255.0F;
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(matrix, x, height, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(matrix, width, height, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(matrix, width, y, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(matrix, x, y, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void fillGradient(float x, float y, float width, float height, int color0, int color1) {
        float f = (color0 >> 24 & 255) / 255.0F;
        float f1 = (color0 >> 16 & 255) / 255.0F;
        float f2 = (color0 >> 8 & 255) / 255.0F;
        float f3 = (color0 & 255) / 255.0F;
        float f4 = (color1 >> 24 & 255) / 255.0F;
        float f5 = (color1 >> 16 & 255) / 255.0F;
        float f6 = (color1 >> 8 & 255) / 255.0F;
        float f7 = (color1 & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(width, y, blitOffset).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, y, blitOffset).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, height, blitOffset).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(width, height, blitOffset).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static void draw(BufferBuilder bufferBuilder, float uMin, float vMin, float uMax, float vMax, float xOffset, float yOffset, float tiledWidth, float tiledHeight, float width, float height, DrawMode mode) {
        if (mode == DrawMode.REPEAT) {
            drawTiled(bufferBuilder, uMin, vMin, uMax, vMax, xOffset, yOffset, tiledWidth, tiledHeight, width, height);
        } else if (mode == DrawMode.STRETCH) {
            drawStretched(bufferBuilder, uMin, vMin, uMax, vMax, xOffset, yOffset, tiledWidth, tiledHeight, width, height);
        }
    }

    public static void drawTiled(BufferBuilder bufferBuilder, float uMin, float vMin, float uMax, float vMax, float xOffset, float yOffset, float tiledWidth, float tiledHeight, float width, float height) {
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

    public static void drawStretched(BufferBuilder bufferBuilder, float uMin, float vMin, float uMax, float vMax, float xOffset, float yOffset, float tiledWidth, float tiledHeight, float width, float height) {
        draw(bufferBuilder, uMin, vMin, uMax, vMax, xOffset, yOffset, width, height);
    }

    public static void draw(BufferBuilder bufferBuilder, float minU, float minV, float maxU, float maxV, float xOffset, float yOffset, float width, float height) {
        bufferBuilder.pos(xOffset, yOffset + height, 0).tex(minU, maxV).endVertex();
        bufferBuilder.pos(xOffset + width, yOffset + height, 0).tex(maxU, maxV).endVertex();
        bufferBuilder.pos(xOffset + width, yOffset, 0).tex(maxU, minV).endVertex();
        bufferBuilder.pos(xOffset, yOffset, 0).tex(minU, minV).endVertex();
    }
}
