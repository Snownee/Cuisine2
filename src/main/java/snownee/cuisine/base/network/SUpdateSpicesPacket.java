package snownee.cuisine.base.network;

import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Spice;

public class SUpdateSpicesPacket extends PlayerPacket {

    private final Object2IntMap<Spice> spices;

    public SUpdateSpicesPacket(Object2IntMap<Spice> spices) {
        this.spices = spices;
    }

    public static class Handler extends PacketHandler<SUpdateSpicesPacket> {

        @Override
        public SUpdateSpicesPacket decode(PacketBuffer buf) {
            int size = buf.readVarInt();
            Object2IntMap<Spice> spices = new Object2IntArrayMap<>(size);
            for (int i = 0; i < size; i++) {
                spices.put(buf.readRegistryIdUnsafe(CuisineRegistries.SPICES), buf.readVarInt());
            }
            return new SUpdateSpicesPacket(spices);
        }

        @Override
        public void encode(SUpdateSpicesPacket pkt, PacketBuffer buf) {
            buf.writeVarInt(pkt.spices.size());
            Object2IntMaps.fastIterable(pkt.spices).forEach(e -> {
                buf.writeRegistryIdUnsafe(CuisineRegistries.SPICES, e.getKey());
                buf.writeVarInt(e.getIntValue());
            });
        }

        @Override
        public void handle(SUpdateSpicesPacket pkt, Supplier<Context> ctx) {
//            if (ctx.get().getSender().container != null) {
                System.out.println(pkt.spices);
//            }
            ctx.get().setPacketHandled(true);
        }

    }

}
