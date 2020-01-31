package snownee.cuisine.data.tag;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ForgeNetworkTagCollection<T extends IForgeRegistryEntry<T>> extends TagCollection<T> {
    private final ForgeRegistry<T> registry;

    public ForgeNetworkTagCollection(ForgeRegistry<T> registry, String resourceLocationPrefix, String typeName) {
        super(id -> Optional.ofNullable(registry.getValue(id)), resourceLocationPrefix, false, typeName);
        this.registry = registry;
    }

    public void write(PacketBuffer buf) {
        Map<ResourceLocation, Tag<T>> map = this.getTagMap();
        buf.writeVarInt(map.size());

        for (Map.Entry<ResourceLocation, Tag<T>> entry : map.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeVarInt(entry.getValue().getAllElements().size());

            for (T t : entry.getValue().getAllElements()) {
                buf.writeVarInt(this.registry.getID(t));
            }
        }

    }

    public void read(PacketBuffer buf) {
        Map<ResourceLocation, Tag<T>> map = Maps.newHashMap();
        int i = buf.readVarInt();

        for (int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = buf.readResourceLocation();
            int k = buf.readVarInt();
            Tag.Builder<T> builder = Tag.Builder.create();

            for (int l = 0; l < k; ++l) {
                builder.add(this.registry.getValue(buf.readVarInt()));
            }

            map.put(resourcelocation, builder.build(resourcelocation));
        }

        this.toImmutable(map);
    }
}
