package snownee.cuisine.data.network;

import java.util.Collection;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.RegistrySerializer;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.data.DeferredDataReloader;
import snownee.kiwi.network.Packet;

public class SSyncRegistryPacket<T extends IForgeRegistryEntry<T>> extends Packet {

    public final ForgeRegistry<T> registry;
    public final RegistrySerializer<T> serializer;
    private PacketBuffer buf;

    public SSyncRegistryPacket(ForgeRegistry<T> registry) {
        this.registry = registry;
        this.serializer = getSerializer(registry);
    }

    public SSyncRegistryPacket(ForgeRegistry<T> registry, PacketBuffer buf) {
        this(registry);
        this.buf = buf;
    }

    public void send(ServerPlayerEntity player) {
        send(PacketDistributor.PLAYER.with(() -> player));
    }

    public void handle() {
        boolean noWarning = true;
        ModLoadingContext ctx = ModLoadingContext.get();
        registry.unfreeze();
        registry.clear();
        int n = buf.readVarInt();
        for (int i = 0; i < n; i++) {
            ResourceLocation id = buf.readResourceLocation();
            if (noWarning)
                ctx.setActiveContainer(ModList.get().getModContainerById(id.getNamespace()).orElse(null), ctx.extension());
            registry.register(serializer.read(buf).setRegistryName(id));
        }
        registry.freeze();
        if (noWarning)
            ctx.setActiveContainer(null, ctx.extension());
        DeferredDataReloader.INSTANCE.complete(registry);
    }

    public static class Handler extends PacketHandler<SSyncRegistryPacket> {

        @Override
        public SSyncRegistryPacket decode(PacketBuffer buf) {
            ForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(buf.readResourceLocation());
            return new SSyncRegistryPacket(registry, buf);
        }

        @Override
        public void encode(SSyncRegistryPacket pkt, PacketBuffer buf) {
            buf.writeResourceLocation(pkt.registry.getRegistryName());
            Collection<IForgeRegistryEntry> values = pkt.registry.getValues();
            buf.writeVarInt(values.size());
            values.forEach($ -> {
                buf.writeResourceLocation($.getRegistryName());
                pkt.serializer.write(buf, $);
            });
        }

        @Override
        public void handle(SSyncRegistryPacket pkt, Supplier<Context> ctx) {
            DeferredDataReloader.INSTANCE.addPacket(pkt);
            ctx.get().setPacketHandled(true);
        }

    }

    public static <T extends IForgeRegistryEntry<T>> RegistrySerializer<T> getSerializer(ForgeRegistry<T> registry) {
        switch (registry.getRegistryName().getPath()) {
        case "material":
            return (RegistrySerializer<T>) new Material.Serializer();
        case "spice":
            return (RegistrySerializer<T>) new Spice.Serializer();
        case "food":
            return (RegistrySerializer<T>) new CuisineFood.Serializer();
        case "recipe":
            return (RegistrySerializer<T>) new CuisineRecipe.Serializer();
        default:
            throw new IllegalArgumentException();
        }
    }
}
