package snownee.cuisine.tag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import snownee.kiwi.network.Packet;

import java.util.function.Supplier;

public class CuisineSTagsListPacket extends Packet {
    private CuisineNetworkTagManager tags;

    public CuisineSTagsListPacket() {
    }

    public CuisineSTagsListPacket(CuisineNetworkTagManager p_i48211_1_) {
        this.tags = p_i48211_1_;
    }

    public CuisineNetworkTagManager getTags() {
        return this.tags;
    }

    public static class Handler extends PacketHandler<CuisineSTagsListPacket> {

        @Override
        public void encode(CuisineSTagsListPacket msg, PacketBuffer buffer) {
            msg.tags.write(buffer);
        }

        @Override
        public CuisineSTagsListPacket decode(PacketBuffer buffer) {
            return new CuisineSTagsListPacket(CuisineNetworkTagManager.read(buffer));
        }

        @Override
        public void handle(CuisineSTagsListPacket message, Supplier<Context> ctx) {
            CuisineNetworkTagManager cuisineNetworkTagManager = message.getTags();
            MaterialTags.setCollection(cuisineNetworkTagManager.getMaterials());
            SpiceTags.setCollection(cuisineNetworkTagManager.getSpices());
            Minecraft.getInstance().getSearchTree(SearchTreeManager.TAGS).recalculate();
        }

    }
}
