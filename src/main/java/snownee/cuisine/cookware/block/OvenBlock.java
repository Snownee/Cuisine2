package snownee.cuisine.cookware.block;

import net.minecraft.block.HorizontalBlock;
import snownee.kiwi.block.ModBlock;

public class OvenBlock extends HorizontalBlock {

    public OvenBlock(Properties builder) {
        super(builder);
        ModBlock.deduceSoundAndHardness(this);
    }

}
