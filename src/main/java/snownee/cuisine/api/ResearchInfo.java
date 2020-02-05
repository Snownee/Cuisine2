package snownee.cuisine.api;

import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;

public interface ResearchInfo {

    int getStar(Material material);

    int getStar(CuisineFood food);

    int getProgress(Material material);

    int getProgress(CuisineFood food);

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
    }

}
