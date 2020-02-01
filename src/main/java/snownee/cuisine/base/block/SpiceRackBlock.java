package snownee.cuisine.base.block;

import net.minecraft.block.HorizontalBlock;
import snownee.kiwi.block.ModBlock;

public class SpiceRackBlock extends HorizontalBlock {

    public SpiceRackBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

}
