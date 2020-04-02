package snownee.kiwi.ui.client.widget;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import snownee.kiwi.Kiwi;
import snownee.kiwi.client.DrawMode;
import snownee.kiwi.client.element.IDrawable;
import snownee.kiwi.client.element.NineSliceDrawable;
import snownee.kiwi.ui.client.UIContext;
import third_party.com.facebook.yoga.YogaValue;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SlotWidget extends Widget {
    //new ResourceLocation(Kiwi.MODID, "textures/gui/slot.png")
    public static final IDrawable SLOT_BACKGROUND = new NineSliceDrawable(null, 3, 3, 1, 2, 1, 2, DrawMode.STRETCH);
    protected Slot slot;

    public SlotWidget(UIContext ctx) {
        super(ctx);
        AtlasTexture i = this.ctx.mc.getModelManager().getAtlasTexture( new ResourceLocation("minecraft", "textures/atlas/blocks.png"));
        TextureAtlasSprite p= (i.getSprite(new ResourceLocation("minecraft","item/apple")));
        System.out.println(p);
        background = new NineSliceDrawable(p,
                16, 16, 0, 0, 0, 0, DrawMode.STRETCH);
        this.node.SetWidth(YogaValue.Pt(16));
        this.node.SetHeight( YogaValue.Pt(16));
    }

    public void setSlot(Slot slot){
        this.slot = slot;
    }

    @Override
    public void draw(int mouseX, int mouseY, float pTicks) {
        super.draw(mouseX, mouseY, pTicks);
        System.out.println(""+this.left+this.right);
        System.out.println((""+mouseX+ mouseY));
        if (isHovering(mouseX, mouseY)) {
            if (this.ctx.mc.currentScreen!=null)
                this.ctx.mc.currentScreen.renderTooltip(addInformation(ITooltipFlag.TooltipFlags.NORMAL),mouseX,mouseY);
        }
    }

    public  List<String> addInformation(ITooltipFlag flagIn) {
        List<String> list1 = Lists.newArrayList();
        if (slot!=null){
            ItemStack itemStack = slot.inventory.getStackInSlot(slot.getSlotIndex());
            List<ITextComponent> list = itemStack.getTooltip(null,flagIn);
            for(ITextComponent itextcomponent : list) {
                list1.add(itextcomponent.getFormattedText());
            }
        }
        list1.add("none");
        return list1;
    }

}
