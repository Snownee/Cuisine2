package snownee.cuisine.api;

import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;

public interface IResearchInfo {

    int getStar(Material material);

    int getStar(CuisineFood food);

    int getProgress(Material material);

    int getProgress(CuisineFood food);

}
