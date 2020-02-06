package snownee.cuisine.data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import snownee.cuisine.Cuisine;
import snownee.cuisine.client.ColorLookup;

public enum DeferredReloadListener implements IFutureReloadListener {
    INSTANCE;

    public final ListMultimap<LoadingStage, IFutureReloadListener> listeners = ArrayListMultimap.create(3, 3);
    private CompletableFuture<Void> registryCompleted;
    private int count;

    @Override
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        registryCompleted = new CompletableFuture<>();
        count = 0;
        Function<IFutureReloadListener, CompletableFuture<?>> mapper = listener -> listener.reload(DummyStage.INSTANCE, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        /* off */
        return stage
                .markCompleteAwaitingOthers(null)
                .thenCompose($ -> make(LoadingStage.REGISTRY, mapper))
                .thenCompose($ -> registryCompleted)
                .thenCompose($ -> make(LoadingStage.TAG, mapper))
                .thenCompose($ -> make(LoadingStage.RECIPE, mapper));
        /* on */
    }

    public synchronized <T extends IForgeRegistryEntry<T>> void complete(IForgeRegistry<T> registry) {
        if (++count >= 2) {
            registryCompleted.complete(null);
            ColorLookup.invalidateAll();
        }
    }

    private CompletableFuture<Void> make(LoadingStage loadingStage, Function<IFutureReloadListener, CompletableFuture<?>> mapper) {
        Cuisine.logger.info("Loading data: " + loadingStage);
        CompletableFuture<?>[] futures = {};
        listeners.get(loadingStage).stream().map(mapper).collect(Collectors.toList()).toArray(futures);
        return CompletableFuture.allOf(futures);
    }

    public static enum LoadingStage {
        REGISTRY, // 3
        TAG, // 1
        RECIPE // 1
    }

    public static enum DummyStage implements IStage {
        INSTANCE;

        @Override
        public <T> CompletableFuture<T> markCompleteAwaitingOthers(T backgroundResult) {
            return CompletableFuture.completedFuture(backgroundResult);
        }

    }

}
