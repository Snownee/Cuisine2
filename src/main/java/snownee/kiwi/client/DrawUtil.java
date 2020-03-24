package snownee.kiwi.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public final class DrawUtil {
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
}
