package snownee.cuisine.base.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import snownee.kiwi.network.Packet;

public abstract class PlayerPacket extends Packet {

    public void send(ServerPlayerEntity player) {
        send(PacketDistributor.PLAYER.with(() -> player));
    }

}
