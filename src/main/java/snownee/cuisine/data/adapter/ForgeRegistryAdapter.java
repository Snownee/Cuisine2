package snownee.cuisine.data.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import snownee.kiwi.util.Util;

public class ForgeRegistryAdapter<T extends IForgeRegistryEntry<T>> implements JsonDeserializer<T> {

    private final LazyValue<IForgeRegistry<T>> registry;

    public ForgeRegistryAdapter(Class<T> superClass) {
        registry = new LazyValue<IForgeRegistry<T>>(() -> RegistryManager.ACTIVE.getRegistry(superClass));
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ResourceLocation id = Util.RL(json.getAsString());
        return registry.getValue().getValue(id);
    }

}
