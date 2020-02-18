package snownee.cuisine.data.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.cuisine.CoreModule;
import snownee.cuisine.base.network.PlayerPacket;
import snownee.cuisine.data.DeferredDataReloader;
import snownee.cuisine.data.tag.CuisineNetworkTagManager;

public class SSyncTagsPacket extends PlayerPacket {
    private CuisineNetworkTagManager tags;
    private PacketBuffer buf;

    public SSyncTagsPacket(CuisineNetworkTagManager tags) {
        this.tags = tags;
    }

    public SSyncTagsPacket(PacketBuffer buf) {
        this.buf = buf;
    }

    public void handle() {
        tags = CuisineNetworkTagManager.read(buf);
        CoreModule.setNetworkTagManager(tags);
        DeferredDataReloader.INSTANCE.tryUpdateRecipes();
    }

    public static class Handler extends PacketHandler<SSyncTagsPacket> {

        @Override
        public SSyncTagsPacket decode(PacketBuffer buf) {
            return new SSyncTagsPacket(buf);
        }

        @Override
        public void encode(SSyncTagsPacket pkt, PacketBuffer buf) {
            pkt.tags.write(buf);
        }

        @Override
        public void handle(SSyncTagsPacket pkt, Supplier<Context> ctx) {
            DeferredDataReloader.INSTANCE.addPacket(pkt);
            ctx.get().setPacketHandled(true);
        }

    }

}
