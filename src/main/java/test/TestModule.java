package test;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.event.FoodCookedEvent;
import snownee.cuisine.api.registry.MaterialInstance;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiModule;

import java.util.List;

@KiwiModule(name = "test")
@KiwiModule.Subscriber
public class TestModule extends AbstractModule {

    @Override
    protected void serverInit(FMLServerStartingEvent event) {
        TestCommand.register(event.getCommandDispatcher(), !event.getServer().isDedicatedServer());

    }

    @SubscribeEvent
    public void onFoodCookedEvent(FoodCookedEvent event) {
        List<MaterialInstance> list = event.foodBuilder.getMaterials();
        for (MaterialInstance i : list) {
            CuisineAPI.getResearchInfo(event.foodBuilder.getCook()).addStar(i.material, 1);
            CuisineAPI.getResearchInfo(event.foodBuilder.getCook()).addProgress(i.material, 1);
            System.out.println(i.toString() + " up");
        }
    }

}
