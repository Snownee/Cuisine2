package snownee.cuisine.base;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import snownee.cuisine.Cuisine;
import snownee.cuisine.base.block.SpiceRackBlock;
import snownee.cuisine.base.client.SpiceRackScreen;
import snownee.cuisine.base.container.SpiceRackContainer;
import snownee.cuisine.base.crafting.SpiceBottleFillingRecipe;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.cuisine.base.tile.SpiceRackTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiManager;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;

@KiwiModule(name = "base", dependencies = "@core")
@KiwiModule.Group("cuisine:base")
@KiwiModule.Optional
public class BaseModule extends AbstractModule {
    static {
        KiwiManager.addItemGroup(Cuisine.MODID, "base", new ItemGroup("base") {
            @Override
            @Nonnull
            public ItemStack createIcon() {
                return SPICE_BOTTLE.getDefaultInstance();
            }
        });
    }
    public static final SpiceBottleItem SPICE_BOTTLE = new SpiceBottleItem(50, itemProp());

    public static final IRecipeSerializer<?> SPICE_BOTTLE_FILL = new SpiceBottleFillingRecipe.Serializer();

    public static final SpiceRackBlock SPICE_RACK = new SpiceRackBlock(blockProp(Material.WOOD));

    public static final Set<Block> SPICE_RACK_VALID_BLOCKS = Sets.newHashSet(SPICE_RACK);
    @Name("spice_rack")
    public static final TileEntityType<SpiceRackTile> SPICE_RACK_TILE = new TileEntityType<>(SpiceRackTile::new, SPICE_RACK_VALID_BLOCKS, null);
    @Name("spice_rack")
    public static final ContainerType<SpiceRackContainer> SPICE_RACK_CONTAINER = new ContainerType<>(SpiceRackContainer::new);

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientInit(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(SPICE_RACK_CONTAINER, SpiceRackScreen::new);
    }
    
}
