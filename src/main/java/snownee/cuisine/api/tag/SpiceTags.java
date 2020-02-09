package snownee.cuisine.api.tag;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.registry.Spice;

public class SpiceTags {

    private static TagCollection<Spice> collection = new TagCollection<>(id -> Optional.empty(), "", false, "Cuisine spice");
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

    public static Tag<Spice> makeWrapperTag(String id) {
        return new SpiceTags.Wrapper(new ResourceLocation(id));
    }

    public static class Wrapper extends Tag<Spice> {
        private int lastKnownGeneration = -1;
        private Tag<Spice> cachedTag;

        public Wrapper(ResourceLocation resourceLocationIn) {
            super(resourceLocationIn);
        }

        @Override
        public boolean contains(Spice SpiceIn) {
            if (this.lastKnownGeneration != SpiceTags.generation) {
                this.cachedTag = SpiceTags.collection.getOrCreate(this.getId());
                this.lastKnownGeneration = SpiceTags.generation;
            }

            return this.cachedTag.contains(SpiceIn);
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
