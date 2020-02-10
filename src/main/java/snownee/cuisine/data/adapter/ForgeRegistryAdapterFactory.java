package snownee.cuisine.data.adapter;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import snownee.kiwi.util.Util;

public class ForgeRegistryAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class clazz = type.getRawType();
        IForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(clazz);
        if (registry != null) {
            return new ForgeRegistryAdapter(registry, gson, type, this);
        }
        return null;
    }

    public static class ForgeRegistryAdapter<T extends IForgeRegistryEntry<T>> extends TypeAdapter<T> {

        private final IForgeRegistry<T> registry;
        private final Gson gson;
        private final TypeAdapterFactory skipPast;
        private final TypeToken<T> typeToken;
        private TypeAdapter<T> delegate;

        private ForgeRegistryAdapter(IForgeRegistry<T> registry, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory skipPast) {
            this.registry = registry;
            this.gson = gson;
            this.typeToken = typeToken;
            this.skipPast = skipPast;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public T read(JsonReader in) throws IOException {
            JsonElement json = Streams.parse(in);
            if (json.isJsonNull()) {
                return null;
            }
            if (json.isJsonObject()) {
                if (!CraftingHelper.processConditions(json.getAsJsonObject(), "conditions")) {
                    throw new ConditionsNotMetException();
                }
                return delegate().fromJsonTree(json);
            } else {
                ResourceLocation id = Util.RL(json.getAsString());
                return registry.getValue(id);
            }
        }

        private TypeAdapter<T> delegate() {
            TypeAdapter<T> d = delegate;
            return d != null ? d : (delegate = gson.getDelegateAdapter(skipPast, typeToken));
        }

    }

    public static final class ConditionsNotMetException extends RuntimeException {}
}
