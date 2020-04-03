package snownee.kiwi.ui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import snownee.kiwi.client.DefaultDrawables;
import snownee.kiwi.client.element.FillDrawable;
import snownee.kiwi.ui.client.widget.NestedWidget;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.SlotWidget;
import snownee.kiwi.ui.client.widget.Widget;
import third_party.com.facebook.yoga.YogaDirection;
import third_party.com.facebook.yoga.YogaJustify;
import third_party.com.facebook.yoga.YogaValue;

public class KiwiScreen extends Screen {

    private Root root;

    public KiwiScreen() {
        super(new StringTextComponent("test"));
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        super.init(mc, width, height);

        UIContext ctx = new UIContext();
        ctx.screen = this;

        root = new Root(ctx);
        root.node.SetJustifyContent(YogaJustify.Center);
        //root.node.SetAlignContent(YogaAlign.Center);
        //root.node.SetAlignItems(YogaAlign.SpaceAround);
        root.node.SetWidth(YogaValue.Pt(width));
        root.node.SetHeight(YogaValue.Pt(height));

        NestedWidget main = new NestedWidget(ctx);
        main.background = DefaultDrawables.getPanel();

        //main.node.SetMargin(YogaValue.AUTO);
        //        main.node.SetMarginHorizontal(YogaValue.AUTO);
        main.node.SetMarginLeft(YogaValue.AUTO);
        main.node.SetMarginRight(YogaValue.AUTO);
        //main.node.SetMarginTop(YogaValue.AUTO);
        //main.node.SetMarginBottom(YogaValue.AUTO);

        main.node.SetWidth(YogaValue.Pt(100));
        main.node.SetHeight(YogaValue.Pt(100));

        //main.node.SetFlexDirection(YogaFlexDirection.Row);
        root.addChild(0, main);

        Widget root_child0 = new Widget(ctx);
        root_child0.background = new FillDrawable(0xffff0000);
        root_child0.node.SetFlexGrow(1);
        root_child0.node.SetFlexBasis(YogaValue.Pt(50));
        main.addChild(0, root_child0);

        Widget root_child1 = new Widget(ctx);
        root_child1.background = new FillDrawable(0xff0000ff);
        root_child1.node.SetFlexGrow(1);
        main.addChild(1, root_child1);

        root_child0.node.SetMargin(YogaValue.Pt(5));
        root_child1.node.SetMargin(YogaValue.Pt(5));

        SlotWidget slotWidget = null;
        slotWidget = new SlotWidget(ctx);
        slotWidget.node.SetFlexGrow(1);
        main.addChild(2, slotWidget);

        root.node.SetStyleDirection(YogaDirection.LeftToRight);
        root.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float pTicks) {
        root.draw(mouseX, mouseY, pTicks);
        super.render(mouseX, mouseY, pTicks);
    }

}
