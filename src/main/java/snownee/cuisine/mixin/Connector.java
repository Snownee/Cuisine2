package snownee.cuisine.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

import snownee.cuisine.Cuisine;

public class Connector implements IMixinConnector {

    @Override
    public void connect() {
        Cuisine.logger.info("Invoking Mixin Connector");
        Mixins.addConfiguration("assets/cuisine/cuisine.mixins.json");
        Cuisine.mixin = true;
    }

}
