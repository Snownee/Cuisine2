package snownee.cuisine.data;

import net.minecraftforge.registries.IForgeRegistry;
import snownee.cuisine.CoreModule;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.client.ColorLookup;
import snownee.cuisine.data.network.SSyncRegistryPacket;
import snownee.cuisine.data.network.SSyncTagsPacket;

public enum DeferredDataReloader {
    INSTANCE;

    private boolean spiceDone;
    private boolean materialDone;
    private boolean foodDone;
    private boolean tagsDone;
    private SSyncTagsPacket tagsPacket;
    private SSyncRegistryPacket recipePacket;

    public void addPacket(SSyncRegistryPacket packet) {
        if (packet.registry == CuisineRegistries.RECIPES) {
            recipePacket = packet;
            tryUpdateRecipes();
        } else {
            packet.handle();
        }
    }

    public void addPacket(SSyncTagsPacket packet) {
        this.tagsPacket = packet;
        tryUpdateTags();
    }

    public synchronized void complete(IForgeRegistry<?> registry) {
        switch (registry.getRegistryName().getPath()) {
        case "material":
            CoreModule.buildMaterialMap();
            materialDone = true;
            break;
        case "spice":
            CoreModule.buildSpiceMap();
            spiceDone = true;
            break;
        case "food":
            CoreModule.buildFoodMap();
            foodDone = true;
            break;
        case "recipe":
            reset();
            return;
        default:
            throw new IllegalArgumentException();
        }
        tryUpdateTags();
    }

    private synchronized void tryUpdateTags() {
        if (materialDone && spiceDone && foodDone && tagsPacket != null) {
            ++DeferredReloadListener.INSTANCE.dataPackID;
            SSyncTagsPacket packet = tagsPacket;
            tagsPacket = null;
            packet.handle();
        }
    }

    public synchronized void tryUpdateRecipes() {
        if (tagsDone && recipePacket != null) {
            SSyncRegistryPacket packet = recipePacket;
            recipePacket = null;
            packet.handle();
        }
    }

    public void reset() {
        materialDone = false;
        spiceDone = false;
        foodDone = false;
        tagsDone = false;
        tagsPacket = null;
        recipePacket = null;
        ColorLookup.invalidateAll();
    }

}
