package snownee.cuisine.api;

import net.minecraft.nbt.CompoundNBT;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;

public interface ResearchInfo {

    int getStar(Material material);

    int getStar(CuisineFood food);

    int getProgress(Material material);

    int getProgress(CuisineFood food);

    void setStar(Material material, int star);

    void setStar(CuisineFood food, int star);

    void setProgress(Material material, int progress);

    void setProgress(CuisineFood food, int progress);

    void addStar(Material material, int star);

    void addStar(CuisineFood food, int star);

    void addProgress(Material material, int progress);

    void addProgress(CuisineFood food, int progress);

    default void read(CompoundNBT data) {

    }

    default CompoundNBT write(CompoundNBT data) {
        return data;
    }

    public static enum Empty implements ResearchInfo {
        INSTANCE;

        @Override
        public int getStar(Material material) {
            return 0;
        }

        @Override
        public int getStar(CuisineFood food) {
            return 0;
        }

        @Override
        public int getProgress(Material material) {
            return 0;
        }

        @Override
        public int getProgress(CuisineFood food) {
            return 0;
        }

        @Override
        public void setStar(Material material, int star) {}

        @Override
        public void setStar(CuisineFood food, int star) {}

        @Override
        public void setProgress(Material material, int progress) {}

        @Override
        public void setProgress(CuisineFood food, int progress) {}

        @Override
        public void addStar(Material material, int star) {

        }

        @Override
        public void addStar(CuisineFood food, int star) {

        }

        @Override
        public void addProgress(Material material, int progress) {

        }

        @Override
        public void addProgress(CuisineFood food, int progress) {

        }
    }

}
