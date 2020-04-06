package snownee.kiwi.ui.client;

import org.w3c.dom.Document;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import snownee.kiwi.ui.UIModule;
import snownee.kiwi.ui.client.factory.UILoader;
import snownee.kiwi.ui.client.widget.Root;
import snownee.kiwi.ui.client.widget.Widget;

public class KiwiScreen extends Screen {

    protected final ResourceLocation id;
    private Root root;
    protected boolean ready;

    public KiwiScreen(ResourceLocation id) {
        super(new StringTextComponent(id.toString()));
        this.id = id;
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        super.init(mc, width, height);

        UIContext ctx = new UIContext();
        ctx.screen = this;

        Document doc = UIModule.KXML_LOADER.map.get(id);
        if (doc == null) {
            return;
        }
        root = UILoader.INSTANCE.load(doc, ctx);

        /*
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

        Button root_child0 = new Button(ctx);
        root_child0.node.SetFlexGrow(1);
        root_child0.node.SetFlexBasis(YogaValue.Pt(50));
        //root_child0.background = new FillDrawable(0xffff0000);

        root_child0.bus.bind("click", btn -> {
            System.out.println("1");
            return false;
        });
        root_child0.bus.bind("click", btn -> {
            System.out.println("2");
            return true;
        });

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
        */
        root.init();
        ready = true;
    }

    @Override
    public void onClose() {
        super.onClose();
        if (root != null) {
            ready = false;
            root.destroy();
            root = null;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float pTicks) {
        if (ready) {
            root.draw(mouseX, mouseY, pTicks);
        }
        super.render(mouseX, mouseY, pTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int type) {
        if (!ready) {
            return false;
        }
        return root.mouseClicked(mouseX, mouseY, type);
    }

    @Binding(target = "#btn", event = "click")
    public void testClick(Widget widget) {
        System.out.println("hi");
    }

}
