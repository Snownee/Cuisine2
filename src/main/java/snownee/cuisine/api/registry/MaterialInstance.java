package snownee.cuisine.api.registry;

import org.apache.commons.lang3.tuple.Pair;

public class MaterialInstance extends Pair<Material, Integer> {

    public final Material material;
    public int star;

    public MaterialInstance(Material material, int star) {
        this.material = material;
        this.star = star;
    }

    @Override
    public Integer setValue(Integer star) {
        return this.star = star;
    }

    @Override
    public Material getLeft() {
        return material;
    }

    @Override
    public Integer getRight() {
        return star;
    }
}
