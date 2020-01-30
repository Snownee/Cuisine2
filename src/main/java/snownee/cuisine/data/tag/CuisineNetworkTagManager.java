package snownee.cuisine.data.tag;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CuisineNetworkTagManager implements IFutureReloadListener {
    private final ForgeNetworkTagCollection<Spice> spices = new ForgeNetworkTagCollection<>(CuisineRegistries.SPICES, "tags/cuisine_spices", "spice");
    private final ForgeNetworkTagCollection<Material> materials = new ForgeNetworkTagCollection<>(CuisineRegistries.MATERIALS, "tags/cuisine_materials", "material");

    public static CuisineNetworkTagManager read(PacketBuffer buffer) {
        CuisineNetworkTagManager cuisineNetworkTagManager = new CuisineNetworkTagManager();
        cuisineNetworkTagManager.getSpices().read(buffer);
        cuisineNetworkTagManager.getMaterials().read(buffer);
        return cuisineNetworkTagManager;
    }

    public ForgeNetworkTagCollection<Spice> getSpices() {
        return this.spices;
    }

    public ForgeNetworkTagCollection<Material> getMaterials() {
        return this.materials;
    }

    public void write(PacketBuffer buffer) {
        this.spices.write(buffer);
        this.materials.write(buffer);
    }

    public CompletableFuture<Void> reload(IFutureReloadListener.IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        CompletableFuture<Map<ResourceLocation, Tag.Builder<Spice>>> completablefuture = this.spices.reload(resourceManager, backgroundExecutor);
        CompletableFuture<Map<ResourceLocation, Tag.Builder<Material>>> completablefuture1 = this.materials.reload(resourceManager, backgroundExecutor);
        /**
         * @see ReloadListener#reload
         */
        return completablefuture1.thenCompose(stage::markCompleteAwaitingOthers).thenAcceptAsync((p_215269_3_) -> {
            reloadProfiler.startSection("Loading " + p_215269_3_.toString());
            this.materials.registerAll(p_215269_3_);
            MaterialTags.setCollection(this.materials);
            reloadProfiler.endSection();
        }, gameExecutor);
    }

    public static class ReloadResults {
        final Map<ResourceLocation, Tag.Builder<Spice>> spices;
        final Map<ResourceLocation, Tag.Builder<Material>> materials;


        public ReloadResults(Map<ResourceLocation, Tag.Builder<Spice>> spices, Map<ResourceLocation, Tag.Builder<Material>> materials) {
            this.spices = spices;
            this.materials = materials;

        }
    }
}