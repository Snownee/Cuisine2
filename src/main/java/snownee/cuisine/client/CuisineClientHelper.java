package snownee.cuisine.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.base.item.RecipeItem;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.kiwi.util.NBTHelper;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public final class CuisineClientHelper {

    private static final RenderType MAP_BACKGROUND = RenderType.getText(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.getText(new ResourceLocation("textures/map/map_background_checkerboard.png"));
    static final Minecraft MC = Minecraft.getInstance();
    private static final Cache<Integer, RecordRenderingContext> RECORDS = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
    private static long lastShowRecordTime;
    public static Object2IntMap<Spice> spices;

    private CuisineClientHelper() {}

    static {
        MinecraftForge.EVENT_BUS.register(CuisineClientHelper.class);
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
    public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
        if (MC.currentScreen != null || MC.player == null || !MC.player.isShiftKeyDown() || !MC.isGameFocused()) {
            return;
        }
        if (MC.gameSettings.thirdPersonView != 0 || MC.gameSettings.hideGUI) {
            return;
        }
        Hand hand = Hand.MAIN_HAND;
        ItemStack stack = MC.player.getHeldItemMainhand();
        if (stack.getItem() != BaseModule.RECIPE) {
            stack = MC.player.getHeldItemOffhand();
            if (stack.getItem() != BaseModule.RECIPE) {
                return;
            }
            hand = Hand.OFF_HAND;
        }
        event.setCanceled(true);
        RecordRenderingContext ctx = getContext(stack);
        if (ctx == null) {
            return;
        }
        int pageNum = ctx.pageNum;
        if (ctx.turnPage(event.getScrollDelta() < 0)) {
            // play re-equip anim
            stack = stack.copy();
            stack.getOrCreateTag().putInt("Page", pageNum);
            if (hand == Hand.MAIN_HAND) {
                MC.getFirstPersonRenderer().itemStackMainHand = stack;
            } else {
                MC.getFirstPersonRenderer().itemStackOffHand = stack;
            }
        }
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

        long time = Util.milliTime();
        if (time - lastShowRecordTime > 5000) {
            MC.player.sendStatusMessage(new StringTextComponent("Shit + Scroll to turn page."), true);
        }
        lastShowRecordTime = time;
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

    @Nullable
    private static RecordRenderingContext getContext(ItemStack stack) {
        try {
            int id = NBTHelper.of(stack).getInt("Id");
            return RECORDS.get(id, () -> new RecordRenderingContext(RecipeItem.getData(id)));
        } catch (ExecutionException e) {
            return null;
        }
    }

    private static void renderMapFirstPerson(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, ItemStack stack) {
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStackIn.scale(0.38F, 0.38F, 0.38F);
        matrixStackIn.translate(-0.75D, -0.75D, 0.0D);
        //matrixStackIn.translate(-0.5D, -0.5D, 0.0D);
        matrixStackIn.scale(0.01F, 0.01F, 0.01F);
        //matrixStackIn.scale(0.0078125F, 0.0078125F, 0.0078125F);
        RecordRenderingContext ctx = getContext(stack);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ctx != null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        ivertexbuilder.pos(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).tex(0.0F, 1.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).tex(1.0F, 1.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).tex(1.0F, 0.0F).lightmap(combinedLightIn).endVertex();
        ivertexbuilder.pos(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).tex(0.0F, 0.0F).lightmap(combinedLightIn).endVertex();
        if (ctx == null) {
            return;
        }
        matrixStackIn.push();
        RenderSystem.setupGuiFlatDiffuseLighting();
        matrixStackIn.scale(20, 20, 1);
        ctx.render(matrixStackIn, bufferIn, combinedLightIn, stack);
        matrixStackIn.pop();

    }
}
