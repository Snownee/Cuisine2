package snownee.kiwi.client.element;

import snownee.kiwi.client.DrawUtil;

public class FillDrawable implements IDrawable {

    public int color;

    public FillDrawable(int color) {
        this.color = color;
    }

    @Override
    public void draw(float left, float top, float width, float height, float pTicks) {
        DrawUtil.fill(left, top, width, height, color);
    }

}
