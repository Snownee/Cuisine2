package snownee.cuisine.cookware;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.cookware.block.CookwareBlock;
import snownee.cuisine.cookware.client.CookwareScreen;
import snownee.cuisine.cookware.container.BowlContainer;
import snownee.cuisine.cookware.container.CookwareContainer;
import snownee.cuisine.cookware.network.CBeginCookingPacket;
import snownee.cuisine.cookware.tile.CookwareTile;
import snownee.cuisine.cookware.tile.HeatingCookwareTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;
import snownee.kiwi.network.NetworkChannel;

@KiwiModule(name = "cookware", dependencies = "@core")
@KiwiModule.Optional
@KiwiModule.Group(CuisineAPI.MODID)
public final class CookwareModule extends AbstractModule {

    public static final CookwareBlock OVEN = new CookwareBlock(blockProp(Material.IRON));
    @Name("oven")
    public static final Cookware OVEN_TYPE = new Cookware();
    @Name("oven")
    public static final TileEntityType<CookwareTile> OVEN_TILE = TileEntityType.Builder.create(() -> new CookwareTile(OVEN_TYPE), OVEN).build(null);
    @Name("oven")
    public static final ContainerType<CookwareContainer> OVEN_CONTAINER = new ContainerType<>((id, inv) -> new CookwareContainer(OVEN_TYPE, id, inv));

    public static final CookwareBlock SAUCEPAN = new CookwareBlock(blockProp(Material.IRON));
    @Name("saucepan")
    public static final Cookware SAUCEPAN_TYPE = new Cookware();
    @Name("saucepan")
    public static final TileEntityType<HeatingCookwareTile> SAUCEPAN_TILE = TileEntityType.Builder.create(() -> new HeatingCookwareTile(SAUCEPAN_TYPE), SAUCEPAN).build(null);
    @Name("saucepan")
    public static final ContainerType<CookwareContainer> SAUCEPAN_CONTAINER = new ContainerType<>((id, inv) -> new CookwareContainer(SAUCEPAN_TYPE, id, inv));

    public static final CookwareBlock DRINKRO = new CookwareBlock(blockProp(Material.IRON));
    @Name("drinkro")
    public static final Cookware DRINKRO_TYPE = new Cookware();
    @Name("drinkro")
    public static final TileEntityType<CookwareTile> DRINKRO_TILE = TileEntityType.Builder.create(() -> new CookwareTile(DRINKRO_TYPE), DRINKRO).build(null);
    @Name("drinkro")
    public static final ContainerType<CookwareContainer> DRINKRO_CONTAINER = new ContainerType<>((id, inv) -> new CookwareContainer(DRINKRO_TYPE, id, inv));

    public static final CookwareBlock BOWL = new CookwareBlock(blockProp(Material.WOOD));
    @Name("bowl")
    public static final Cookware BOWL_TYPE = new Cookware().setInputSlots(4);
    @Name("bowl")
    public static final TileEntityType<CookwareTile> BOWL_TILE = TileEntityType.Builder.create(CookwareModule::createBowl, BOWL).build(null);
    @Name("bowl")
    public static final ContainerType<CookwareContainer> BOWL_CONTAINER = new ContainerType<>(BowlContainer::new);

    @Override
    protected void preInit() {
        NetworkChannel.register(CBeginCookingPacket.class, new CBeginCookingPacket.Handler());
    }

    @Override
    protected void init(FMLCommonSetupEvent event) {
        OVEN_TYPE.setBlocks(OVEN_TILE, OVEN).setContainer(OVEN_CONTAINER);
        SAUCEPAN_TYPE.setBlocks(SAUCEPAN_TILE, SAUCEPAN).setContainer(SAUCEPAN_CONTAINER);
        DRINKRO_TYPE.setBlocks(DRINKRO_TILE, DRINKRO).setContainer(DRINKRO_CONTAINER);
        BOWL_TYPE.setBlocks(BOWL_TILE, BOWL).setContainer(BOWL_CONTAINER);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientInit(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(OVEN_CONTAINER, CookwareScreen::new);
        ScreenManager.registerFactory(SAUCEPAN_CONTAINER, CookwareScreen::new);
        ScreenManager.registerFactory(DRINKRO_CONTAINER, CookwareScreen::new);
        ScreenManager.registerFactory(BOWL_CONTAINER, CookwareScreen::new);
    }

    private static CookwareTile createBowl() {
        return new CookwareTile(BOWL_TYPE) {
            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                return new BowlContainer(id, playerInventory, this);
            }
        };
    }

}
