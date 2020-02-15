package test;

import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.KiwiCommand;
import snownee.kiwi.KiwiModule;

@KiwiModule(name = "test")
@KiwiModule.Subscriber
public class TestModule extends AbstractModule {

    protected void serverInit(FMLServerStartingEvent event) {
        TestCommand.register(event.getCommandDispatcher(), !event.getServer().isDedicatedServer());

    }

}
