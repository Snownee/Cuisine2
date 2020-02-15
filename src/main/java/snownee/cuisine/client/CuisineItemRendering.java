package snownee.cuisine.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.item.RecipeItem;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.cuisine.data.RecordData;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public final class CuisineItemRendering {

    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));
    private static final Minecraft MC = Minecraft.getInstance();

    private CuisineItemRendering() {}

    static {
        MinecraftForge.EVENT_BUS.register(CuisineItemRendering.class);
    }

    @SubscribeEvent
    public static void onItemColorsInit(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                return SpiceBottleItem.getSpice(stack).map(ColorLookup::get).orElse(-1);
            }
            return -1;
        }, BaseModule.SPICE_BOTTLE);
    }

    @SubscribeEvent
    public static void onFirstPersonHandRendering(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() != BaseModule.RECIPE) {
            return;
        }
        event.setCanceled(true);
        HandSide handside = event.getHand() == Hand.MAIN_HAND ? MC.player.getPrimaryHand() : MC.player.getPrimaryHand().opposite();
        event.getMatrixStack().push();
        renderMapFirstPersonSide(event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getEquipProgress(), handside, event.getSwingProgress(), stack);
        event.getMatrixStack().pop();
        //TODO
    }

    private static void renderMapFirstPersonSide(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, float equippedProgress, HandSide handIn, float swingProgress, ItemStack stack) {
        float f = handIn == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStackIn.translate(f * 0.125F, -0.125D, 0.0D);
        if (!MC.player.isInvisible()) {
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * 10.0F));
            MC.getFirstPersonRenderer().renderArmFirstPerson(matrixStackIn, bufferIn, combinedLightIn, equippedProgress, swingProgress, handIn);
            matrixStackIn.pop();
        }

        matrixStackIn.push();
        matrixStackIn.translate(f * 0.51F, -0.08F + equippedProgress * -1.2F, -0.75D);
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swingProgress * (float) Math.PI);
        matrixStackIn.translate(f * f3, f4 - 0.3F * f2, f5);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f2 * -45.0F));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * f2 * -30.0F));
        renderMapFirstPerson(matrixStackIn, bufferIn, combinedLightIn, stack);
        matrixStackIn.pop();
    }

    @SuppressWarnings("deprecation")
    private static void renderMapFirstPerson(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, ItemStack stack) {
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStackIn.scale(0.38F, 0.38F, 0.38F);
        matrixStackIn.translate(-0.75D, -0.75D, 0.0D);
        //matrixStackIn.translate(-0.5D, -0.5D, 0.0D);
        matrixStackIn.scale(0.01F, 0.01F, 0.01F);
        //matrixStackIn.scale(0.0078125F, 0.0078125F, 0.0078125F);
        RecordData data = RecipeItem.getData(stack);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(data != null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = matrixStackIn.getLast().getPositionMatrix();
        ivertexbuilder.pos(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).tex(0.0F, 1.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).tex(1.0F, 1.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).tex(1.0F, 0.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).tex(0.0F, 0.0F).lightmap(combinedLightIn).endVertex();
        if (data == null) {
            return;
        }
        matrixStackIn.push();
        RenderSystem.setupGuiFlatDiffuseLighting();
        matrixStackIn.scale(20, 20, 1);
        matrixStackIn.translate(0, 0, -1);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
        matrixStackIn.translate(1, -1, 0);
        MC.getItemRenderer().renderItem(null, new ItemStack(Items.MUSHROOM_STEW), TransformType.GUI, false, matrixStackIn, bufferIn, MC.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        matrixStackIn.translate(0, -1, 0);
        MC.getItemRenderer().renderItem(null, new ItemStack(Items.RED_MUSHROOM), TransformType.GUI, false, matrixStackIn, bufferIn, MC.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        matrixStackIn.translate(0, -1, 0);
        MC.getItemRenderer().renderItem(null, new ItemStack(Items.BROWN_MUSHROOM), TransformType.GUI, false, matrixStackIn, bufferIn, MC.world, combinedLightIn, OverlayTexture.DEFAULT_LIGHT);
        RenderHelper.enableStandardItemLighting();
        matrixStackIn.pop();
        matrixStackIn.push();
        matrixStackIn.scale(0.8f, 0.8f, 1);
        matrix4f = matrixStackIn.getLast().getPositionMatrix();
        MC.fontRenderer.renderString("Mushroom Stew", 45, 24, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
        MC.fontRenderer.renderString("Red Mushroom x1", 45, 52, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
        MC.fontRenderer.renderString("Brown Mushroom x1", 45, 80, 0x000000, false, matrix4f, bufferIn, /*transparent*/ false, /*?*/0, combinedLightIn);
        matrixStackIn.pop();

    }
}
