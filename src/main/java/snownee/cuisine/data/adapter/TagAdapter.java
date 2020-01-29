package snownee.cuisine.data.adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import snownee.kiwi.util.Util;

public class TagAdapter implements JsonDeserializer<Tag<?>> {

    @Override
    public Tag<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Type[] typeArguments = ((ParameterizedType) typeOfT).getActualTypeArguments();
        final Type parametrizedType = typeArguments[0];

        ResourceLocation id = Util.RL(json.getAsString());
        if (id != null) {
            if (parametrizedType == Block.class) {
                return BlockTags.getCollection().get(id);
            }
            if (parametrizedType == Item.class) {
                return ItemTags.getCollection().get(id);
            }
            if (parametrizedType == Fluid.class) {
                return FluidTags.getCollection().get(id);
            }
        }
        return null;
    }

}
