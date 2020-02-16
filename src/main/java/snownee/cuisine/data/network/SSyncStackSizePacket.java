package snownee.cuisine.data.network;

import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.cuisine.util.Tweaker;
import snownee.kiwi.network.Packet;

public class SSyncStackSizePacket extends Packet {

    private Object2IntLinkedOpenHashMap<Item> stackSize;

    public SSyncStackSizePacket(Object2IntLinkedOpenHashMap<Item> stackSize) {
        this.stackSize = stackSize;
    }

    public void send(ServerPlayerEntity player) {
        send(PacketDistributor.PLAYER.with(() -> player));
    }

    public static class Handler extends PacketHandler<SSyncStackSizePacket> {

        @Override
        public SSyncStackSizePacket decode(PacketBuffer buf) {
            int size = buf.readVarInt();
            Object2IntLinkedOpenHashMap<Item> map = new Object2IntLinkedOpenHashMap(size);
            for (int i = 0; i < size; i++) {
                map.put(buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS), buf.readVarInt());
            }
            return new SSyncStackSizePacket(map);
        }

        @Override
        public void encode(SSyncStackSizePacket pkt, PacketBuffer buf) {
            Object2IntLinkedOpenHashMap<Item> map = pkt.stackSize;
            buf.writeVarInt(map.size());
            map.keySet().forEach(item -> {
                buf.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item);
                buf.writeVarInt(map.getInt(item));
            });
        }

        @Override
        public void handle(SSyncStackSizePacket pkt, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Tweaker.clear();
                pkt.stackSize.keySet().forEach(item -> Tweaker.setStackSize(item, pkt.stackSize.getInt(item)));
            });
            ctx.get().setPacketHandled(true);
        }

    }

}
