package snownee.cuisine.agriculture;

import net.minecraft.block.Blocks;
import snownee.cuisine.agriculture.block.CuisineCropsBlock;
import snownee.cuisine.api.CuisineAPI;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.item.ModItem;

@KiwiModule(name = "agriculture")
@KiwiModule.Group(CuisineAPI.MODID)
@KiwiModule.Optional
public class AgricultureModule extends AbstractModule {

    public static final CuisineCropsBlock PEANUT = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock CHILI = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SCALLION = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SPINACH = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SESAME = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SOYBEAN = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock RICE = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock TOMATO = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock GARLIC = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock GINGER = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock SICHUAN_PEPPER = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock TURNIP = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock CABBAGE = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock LETTUCE = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    //TODO BlockCorn
    public static final CuisineCropsBlock CORN = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    //TODO BlockDoubleCrops
    public static final CuisineCropsBlock CUCUMBER = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock GREEN_PEPPER = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock RED_PEPPER = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock LEEK = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock ONION = new CuisineCropsBlock(blockProp(Blocks.POTATOES));
    public static final CuisineCropsBlock EGGPLANT = new CuisineCropsBlock(blockProp(Blocks.POTATOES));

    public static final ModItem CHILI_POWDER = new ModItem(itemProp());
    public static final ModItem SICHUAN_PEPPER_POWDER = new ModItem(itemProp());
    public static final ModItem TOFU = new ModItem(itemProp());
}
