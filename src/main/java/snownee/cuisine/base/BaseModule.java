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
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import snownee.cuisine.Cuisine;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.block.CabinetBlock;
import snownee.cuisine.base.block.ConnectorBlock;
import snownee.cuisine.base.block.CookstoveBlock;
import snownee.cuisine.base.block.SpiceRackBlock;
import snownee.cuisine.base.client.SpiceRackScreen;
import snownee.cuisine.base.container.SpiceRackContainer;
import snownee.cuisine.base.crafting.SpiceBottleFillingRecipe;
import snownee.cuisine.base.crafting.TextureStonecuttingRecipe;
import snownee.cuisine.base.item.ManualItem;
import snownee.cuisine.base.item.RecipeItem;
import snownee.cuisine.base.item.SpiceBottleItem;
import snownee.cuisine.base.network.SUpdateSpicesPacket;
import snownee.cuisine.base.tile.CabinetTile;
import snownee.cuisine.base.tile.SpiceRackTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.KiwiModule.Subscriber.Bus;
import snownee.kiwi.Name;
import snownee.kiwi.NoGroup;
import snownee.kiwi.client.model.TextureModel;
import snownee.kiwi.item.ModBlockItem;
import snownee.kiwi.item.ModItem;
import snownee.kiwi.network.NetworkChannel;

@SuppressWarnings("unused")
@KiwiModule(name = "base", dependencies = "@core")
@KiwiModule.Group(CuisineAPI.MODID)
@KiwiModule.Optional
@KiwiModule.Subscriber(Bus.MOD)
public class BaseModule extends AbstractModule {
    static {
        new ItemGroup(CuisineAPI.MODID) {
            @Override
            @Nonnull
            public ItemStack createIcon() {
                return SPICE_BOTTLE.getDefaultInstance();
            }
        };
    }
    public static final SpiceBottleItem SPICE_BOTTLE = new SpiceBottleItem(256, 8000, itemProp());

    public static final IRecipeSerializer<?> SPICE_BOTTLE_FILL = new SpiceBottleFillingRecipe.Serializer();

    public static final SpiceRackBlock SPICE_RACK = new SpiceRackBlock(blockProp(Material.WOOD));

    public static final Set<Block> SPICE_RACK_VALID_BLOCKS = Sets.newHashSet(SPICE_RACK);
    @Name("spice_rack")
    public static final TileEntityType<SpiceRackTile> SPICE_RACK_TILE = new TileEntityType<>(SpiceRackTile::new, SPICE_RACK_VALID_BLOCKS, null);
    @Name("spice_rack")
    public static final ContainerType<SpiceRackContainer> SPICE_RACK_CONTAINER = new ContainerType<>(SpiceRackContainer::new);

    public static final CabinetBlock CABINET = new CabinetBlock(blockProp(Material.ROCK).notSolid());
    @Name("cabinet")
    public static final TileEntityType<CabinetTile> CABINET_TILE = TileEntityType.Builder.create(CabinetTile::new, CABINET).build(null);

    public static final CookstoveBlock COOKSTOVE = new CookstoveBlock(blockProp(Material.ROCK).notSolid());

    public static final ConnectorBlock CONNECTOR = new ConnectorBlock(blockProp(Material.ROCK).notSolid());

    public static final Set<Block> CONNECTOR_VALID_BLOCKS = Sets.newHashSet(CONNECTOR, COOKSTOVE);
    @Name("connector")
    public static final TileEntityType<KitchenTile> CONNECTOR_TILE = new TileEntityType<>(BaseModule::createConnector, CONNECTOR_VALID_BLOCKS, null);

    public static final ManualItem MANUAL = new ManualItem();
    @NoGroup
    public static final RecipeItem RECIPE = new RecipeItem();
    public static final ModItem SALT = new ModItem(itemProp());
    public static final ModItem BROWN_SUGAR = new ModItem(itemProp());
    public static final ModItem FLOUR = new ModItem(itemProp());
    public static final ModItem DOUGH = new ModItem(itemProp());
    public static final ModItem PLAIN_CAKE = new ModItem(itemProp());

    public static final IRecipeSerializer<TextureStonecuttingRecipe> STONECUTTING_TEXTURE = new SingleItemRecipe.Serializer<>(TextureStonecuttingRecipe::new);

    @Override
    protected void preInit() {
        NetworkChannel.register(SUpdateSpicesPacket.class, new SUpdateSpicesPacket.Handler());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientInit(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(SPICE_RACK_CONTAINER, SpiceRackScreen::new);
    }

    @Override
    protected void serverInit(FMLServerStartingEvent event) {
        Cuisine.getServer().getWorld(DimensionType.OVERWORLD).getSavedData();
    }

    @Override
    protected void init(FMLCommonSetupEvent event) {
        ModBlockItem.INSTANT_UPDATE_TILES.add(CONNECTOR_TILE);
        ModBlockItem.INSTANT_UPDATE_TILES.add(CABINET_TILE);
    }

    public static KitchenTile createConnector() {
        return new KitchenTile(CONNECTOR_TILE, "0");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        TextureModel.register(event, CONNECTOR, CONNECTOR.getDefaultState(), "0");
        TextureModel.register(event, CABINET, CABINET.getDefaultState(), "0");
    }
}
