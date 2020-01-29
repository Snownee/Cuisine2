package snownee.cuisine.api;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.api.registry.Utensil;

public class FoodBuilder {

    private final Utensil utensil;
    private final List<Material> materials = Lists.newArrayList();
    private final List<Spice> spices = Lists.newArrayList();

    public FoodBuilder(Utensil utensil) {
        this.utensil = utensil;
    }

    public void add(Material material) {
        materials.add(material);
    }

    public void add(Spice spice) {
        spices.add(spice);
    }

    public List<Material> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    public List<Spice> getSpices() {
        return Collections.unmodifiableList(spices);
    }

    public Utensil getUtensil() {
        return utensil;
    }

}
