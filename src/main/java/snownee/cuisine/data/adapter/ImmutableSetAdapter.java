package snownee.cuisine.data.adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public enum ImmutableSetAdapter implements JsonDeserializer<ImmutableSet<?>> {
    INSTANCE;

    @Override
    public ImmutableSet<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        final Type parametrizedType = listOf(typeArguments[0]).getType();
        final List<?> list = context.deserialize(json, parametrizedType);
        return ImmutableSet.copyOf(list);
    }

    private static <E> TypeToken<List<E>> listOf(final Type arg) {
        return new TypeToken<List<E>>() {}.where(new TypeParameter<E>() {}, (TypeToken<E>) TypeToken.of(arg));
    }

}
