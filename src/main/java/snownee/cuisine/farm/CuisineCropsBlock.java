package snownee.cuisine.farm;

import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import snownee.kiwi.RenderLayer;
import snownee.kiwi.RenderLayer.Layer;

@RenderLayer(Layer.CUTOUT)
public class CuisineCropsBlock extends CropsBlock {
    public CuisineCropsBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return this;
    }
}
