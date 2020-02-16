package snownee.cuisine.cookware.network;

import java.util.function.Supplier;

import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.cuisine.cookware.container.CookwareContainer;
import snownee.kiwi.network.ClientPacket;

public class CBeginCookingPacket extends ClientPacket {

    private final boolean shiftClick;

    public CBeginCookingPacket(boolean shiftClick) {
        this.shiftClick = shiftClick;
    }

    public static class Handler extends PacketHandler<CBeginCookingPacket> {

        @Override
        public CBeginCookingPacket decode(PacketBuffer buf) {
            return new CBeginCookingPacket(buf.readBoolean());
        }

        @Override
        public void encode(CBeginCookingPacket pkt, PacketBuffer buf) {
            buf.writeBoolean(pkt.shiftClick);
        }

        @Override
        public void handle(CBeginCookingPacket pkt, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Container container = ctx.get().getSender().openContainer;
                if (container instanceof CookwareContainer) {
                    ((CookwareContainer) container).startCooking(pkt.shiftClick);
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }
}
