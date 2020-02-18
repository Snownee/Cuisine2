package snownee.cuisine.data.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.cuisine.base.network.PlayerPacket;
import snownee.cuisine.data.RecordData;

public class SSyncRecordPacket extends PlayerPacket {

    private final RecordData data;
    private final int id;

    public SSyncRecordPacket(RecordData data, int id) {
        this.data = data;
        this.id = id;
    }

    public static class Handler extends PacketHandler<SSyncRecordPacket> {

        @Override
        public SSyncRecordPacket decode(PacketBuffer buf) {
            int id = buf.readVarInt();
            return new SSyncRecordPacket(RecordData.read(buf, id), id);
        }

        @Override
        public void encode(SSyncRecordPacket pkt, PacketBuffer buf) {
            pkt.data.write(buf, pkt.id);
        }

        @Override
        public void handle(SSyncRecordPacket pkt, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {});
            ctx.get().setPacketHandled(true);
        }

    }

}
