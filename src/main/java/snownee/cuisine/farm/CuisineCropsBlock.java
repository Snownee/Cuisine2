package snownee.cuisine.farm;

import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import snownee.kiwi.RenderLayer;

@RenderLayer(RenderLayer.Layer.CUTOUT)
public class CuisineCropsBlock extends CropsBlock {
    public CuisineCropsBlock(Properties p_i48421_1_) {
        super(p_i48421_1_);
    }

    protected IItemProvider getSeedsItem() {
        return this.asItem();
    }

}
