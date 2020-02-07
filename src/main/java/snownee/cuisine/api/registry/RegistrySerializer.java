package snownee.cuisine.api.registry;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistrySerializer<T extends IForgeRegistryEntry<T>> {

    T read(PacketBuffer buf);

    void write(PacketBuffer buf, T entry);

}
