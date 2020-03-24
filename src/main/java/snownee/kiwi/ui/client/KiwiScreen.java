package snownee.kiwi.ui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import snownee.kiwi.ui.client.widget.NestedWidget;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.Widget;
import third_party.com.facebook.yoga.YogaAlign;
import third_party.com.facebook.yoga.YogaDirection;
import third_party.com.facebook.yoga.YogaFlexDirection;
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

        root = new Root();
        root.node.SetJustifyContent(YogaJustify.Center);
        //root.node.SetAlignContent(YogaAlign.Center);
        root.color = 0x55ffffff;
        //root.node.SetAlignItems(YogaAlign.SpaceAround);
        root.node.SetWidth(YogaValue.Pt(width));
        root.node.SetHeight(YogaValue.Pt(height));

        NestedWidget main = new NestedWidget();
        main.color = 0xffffffff;

        //main.node.SetMargin(YogaValue.AUTO);
        main.node.SetMarginHorizontal(YogaValue.AUTO);
        //main.node.SetMarginLeft(YogaValue.AUTO);
        //main.node.SetMarginRight(YogaValue.AUTO);
        //main.node.SetMarginTop(YogaValue.AUTO);
        //main.node.SetMarginBottom(YogaValue.AUTO);

        main.node.SetWidth(YogaValue.Pt(100));
        main.node.SetHeight(YogaValue.Pt(100));

        main.node.SetFlexDirection(YogaFlexDirection.Row);
        root.addChild(0, main);

        Widget root_child0 = new Widget();
        root_child0.color = 0xffff0000;
        root_child0.node.SetFlexGrow(1);
        root_child0.node.SetFlexBasis(YogaValue.Pt(50));
        main.addChild(0, root_child0);

        Widget root_child1 = new Widget();
        root_child1.color = 0xffff00ff;
        root_child1.node.SetFlexGrow(1);
        main.addChild(1, root_child1);

        root_child0.node.SetMargin(YogaValue.Pt(5));
        root_child1.node.SetMargin(YogaValue.Pt(5));

        root.node.SetStyleDirection(YogaDirection.LeftToRight);
        root.done();
    }

    @Override
    public void render(int mouseX, int mouseY, float pTicks) {
        root.draw(pTicks);
        super.render(mouseX, mouseY, pTicks);
    }

}
