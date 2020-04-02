package snownee.kiwi.client.element;

import snownee.kiwi.client.DrawUtil;

public class FillDrawable implements IDrawable {

    public int color;
    public int color1 = -1;

    public FillDrawable(int color) {
        this.color = color;
    }

    public FillDrawable(int color0, int color1) {
        this.color = color0;
        if (color1 == -1) {
            color1 = 0x00010101;
        } else {
            this.color1 = color1;
        }
    }

    @Override
    public void draw(float left, float top, float width, float height, float pTicks) {
        if (color1 == -1) {
            DrawUtil.fill(left, top, left + width, top + height, color);
        } else {
            DrawUtil.fillGradient(left, top, left + width, top + height, color, color1);
        }
    }

}
