package snownee.cuisine.cookware.network;

import java.util.function.Supplier;

import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.cuisine.cookware.inventory.container.OvenContainer;
import snownee.kiwi.network.ClientPacket;

public class CBeginCookingPacket extends ClientPacket {

    public static class Handler extends PacketHandler<CBeginCookingPacket> {

        @Override
        public CBeginCookingPacket decode(PacketBuffer buf) {
            return new CBeginCookingPacket();
        }

        @Override
        public void encode(CBeginCookingPacket pkt, PacketBuffer buf) {}

        @Override
        public void handle(CBeginCookingPacket pkt, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Container container = ctx.get().getSender().openContainer;
                if (container instanceof OvenContainer) {
                    ((OvenContainer) container).cook();
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }
}
