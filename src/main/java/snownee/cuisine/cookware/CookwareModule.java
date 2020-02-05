package snownee.cuisine.cookware;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.cookware.block.OvenBlock;
import snownee.cuisine.cookware.client.OvenScreen;
import snownee.cuisine.cookware.inventory.container.OvenContainer;
import snownee.cuisine.cookware.network.CBeginCookingPacket;
import snownee.cuisine.cookware.tile.OvenTile;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.Name;
import snownee.kiwi.network.NetworkChannel;

@KiwiModule(name = "cookware", dependencies = "@core")
@KiwiModule.Optional
@KiwiModule.Group("decorations")
public final class CookwareModule extends AbstractModule {

    public static final OvenBlock OVEN = new OvenBlock(blockProp(Material.IRON));
    @Name("oven")
    public static final Cookware OVEN_TYPE = new Cookware(OVEN);
    @Name("oven")
    public static final TileEntityType<OvenTile> OVEN_TILE = TileEntityType.Builder.create(OvenTile::new, OVEN).build(null);
    @Name("oven")
    public static final ContainerType<OvenContainer> OVEN_CONTAINER = new ContainerType<>(OvenContainer::new);

    public static final OvenBlock SAUCEPAN = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock DRINKRO = new OvenBlock(blockProp(Material.IRON));

    public static final OvenBlock BOWL = new OvenBlock(blockProp(Material.WOOD));
    @Name("bowl")
    public static final Cookware BOWL_TYPE = new Cookware(BOWL);

    @Override
    protected void preInit() {
        NetworkChannel.register(CBeginCookingPacket.class, new CBeginCookingPacket.Handler());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientInit(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(OVEN_CONTAINER, OvenScreen::new);
    }

}
