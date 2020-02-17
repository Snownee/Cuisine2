package snownee.cuisine.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.cuisine.data.RecordData;
import snownee.kiwi.util.NBTHelper;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RecordRenderingContext {
    public final RecordData data;
    public int pageNum;
    public int page = 3;

    public RecordRenderingContext(RecordData data) {
        this.data = data;
    }

    public boolean turnPage(boolean next) {
        if (page < 2) {
            return false;
        }
        pageNum = (pageNum + page + (next ? 1 : -1)) % page;
        return true;
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        int pageNum = NBTHelper.of(stack).getInt("Page", this.pageNum);
        //TODO
        matrixStackIn.translate(0, 0, -1);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
        matrixStackIn.translate(1, -1, 0);
        mc.getItemRenderer().renderItem(null, new ItemStack(Items.MUSHROOM_STEW), TransformType.GUI, false, matrixStackIn, bufferIn, mc.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        matrixStackIn.translate(0, -1, 0);
        mc.getItemRenderer().renderItem(null, new ItemStack(Items.RED_MUSHROOM), TransformType.GUI, false, matrixStackIn, bufferIn, mc.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        matrixStackIn.translate(0, -1, 0);
        mc.getItemRenderer().renderItem(null, new ItemStack(Items.BROWN_MUSHROOM), TransformType.GUI, false, matrixStackIn, bufferIn, mc.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        RenderHelper.enableStandardItemLighting();
        matrixStackIn.pop();
        matrixStackIn.push();
        matrixStackIn.scale(0.8f, 0.8f, 1);
        Matrix4f matrix4f = matrixStackIn.getLast().getPositionMatrix();
        mc.fontRenderer.renderString("Mushroom Stew " + pageNum, 45, 24, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
        mc.fontRenderer.renderString("Red Mushroom x1", 45, 52, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
        mc.fontRenderer.renderString("Brown Mushroom x1", 45, 80, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
    }
}
