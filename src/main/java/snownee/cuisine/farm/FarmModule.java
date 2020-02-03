package snownee.cuisine.farm;

import net.minecraft.block.Blocks;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;

@KiwiModule(name = "farm")
@KiwiModule.Group("decorations")
@KiwiModule.Optional
public class FarmModule extends AbstractModule {

    public static final CuisineCropsBlock PEANUT = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock CHILI = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SCALLION = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SPINACH = new CuisineCropsBlock(blockProp(Blocks.POTATOES));

}
