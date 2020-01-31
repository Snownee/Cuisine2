package snownee.cuisine.data.tag;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.registry.Material;

import java.util.Collection;
import java.util.Optional;

public class MaterialTags {

    public static final Tag<Material> VEGE = makeWrapperTag("vege");

    private static TagCollection<Material> collection = new TagCollection<>((p_203643_0_) -> {
        return Optional.empty();
    }, "", false, "");
    private static int generation;

    public static TagCollection<Material> getCollection() {
        return collection;
    }

    public static void setCollection(TagCollection<Material> collectionIn) {
        collection = collectionIn;
        ++generation;
    }

    public static int getGeneration() {
        return generation;
    }

    private static Tag<Material> makeWrapperTag(String p_199901_0_) {
        return new MaterialTags.Wrapper(new ResourceLocation(p_199901_0_));
    }

    public static class Wrapper extends Tag<Material> {
        private int lastKnownGeneration = -1;
        private Tag<Material> cachedTag;

        public Wrapper(ResourceLocation resourceLocationIn) {
            super(resourceLocationIn);
        }

        @Override
        public boolean contains(Material materialIn) {
            if (this.lastKnownGeneration != MaterialTags.generation) {
                this.cachedTag = MaterialTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = MaterialTags.generation;
            }

            return this.cachedTag.contains(materialIn);
        }

        @Override
        public Collection<Material> getAllElements() {
            if (this.lastKnownGeneration != MaterialTags.generation) {
                this.cachedTag = MaterialTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = MaterialTags.generation;
            }

            return this.cachedTag.getAllElements();
        }

        @Override
        public Collection<ITagEntry<Material>> getEntries() {
            if (this.lastKnownGeneration != MaterialTags.generation) {
                this.cachedTag = MaterialTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = MaterialTags.generation;
            }

            return this.cachedTag.getEntries();
        }
    }
}
