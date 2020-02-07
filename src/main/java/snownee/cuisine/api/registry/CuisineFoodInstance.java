package snownee.cuisine.api.registry;

import org.apache.commons.lang3.tuple.Pair;

public class CuisineFoodInstance extends Pair<CuisineFood, Integer> {

    public final CuisineFood food;
    public int star;

    public CuisineFoodInstance(CuisineFood food, int star) {
        this.food = food;
        this.star = star;
    }

    @Override
    public Integer setValue(Integer star) {
        return this.star = star;
    }

    @Override
    public CuisineFood getLeft() {
        return food;
    }

    @Override
    public Integer getRight() {
        return star;
    }
}
