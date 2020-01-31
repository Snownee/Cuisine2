package snownee.cuisine.data.tag;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.registry.Spice;

import java.util.Collection;
import java.util.Optional;

public class SpiceTags {
    private static TagCollection<Spice> collection = new TagCollection<>((p_203643_0_) -> {
        return Optional.empty();
    }, "", false, "");
    private static int generation;

    public static TagCollection<Spice> getCollection() {
        return collection;
    }

    public static void setCollection(TagCollection<Spice> collectionIn) {
        collection = collectionIn;
        ++generation;
    }

    public static int getGeneration() {
        return generation;
    }

    private static Tag<Spice> makeWrapperTag(String p_199901_0_) {
        return new SpiceTags.Wrapper(new ResourceLocation(p_199901_0_));
    }

    public static class Wrapper extends Tag<Spice> {
        private int lastKnownGeneration = -1;
        private Tag<Spice> cachedTag;

        public Wrapper(ResourceLocation resourceLocationIn) {
            super(resourceLocationIn);
        }

        @Override
        public boolean contains(Spice spiceIn) {
            if (this.lastKnownGeneration != SpiceTags.generation) {
                this.cachedTag = SpiceTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = SpiceTags.generation;
            }

            return this.cachedTag.contains(spiceIn);
        }

        @Override
        public Collection<Spice> getAllElements() {
            if (this.lastKnownGeneration != SpiceTags.generation) {
                this.cachedTag = SpiceTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = SpiceTags.generation;
            }

            return this.cachedTag.getAllElements();
        }

        @Override
        public Collection<ITagEntry<Spice>> getEntries() {
            if (this.lastKnownGeneration != SpiceTags.generation) {
                this.cachedTag = SpiceTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = SpiceTags.generation;
            }

            return this.cachedTag.getEntries();
        }
    }
}
