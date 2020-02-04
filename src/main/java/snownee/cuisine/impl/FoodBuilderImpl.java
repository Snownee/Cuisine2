package snownee.cuisine.impl;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

public class FoodBuilderImpl implements FoodBuilder {

    private final Cookware cookware;
    private final List<Material> materials = Lists.newArrayList();
    private final Object2IntOpenHashMap<Spice> spices = new Object2IntOpenHashMap<>();

    public FoodBuilderImpl(Cookware utensil) {
        this.cookware = utensil;
    }

    @Override
    public void add(Material material) {
        materials.add(material);
    }

    @Override
    public void add(Spice spice, int incr) {
        spices.addTo(spice, incr);
    }

    @Override
    public void add(Spice spice) {
        add(spice, 1);
    }

    @Override
    public boolean has(Object o) {
        if (o instanceof Material) {
            return materials.contains(o);
        }
        if (o instanceof Spice) {
            return spices.containsKey(o);
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public int count(Object o) {
        if (o instanceof Material) {
            return (int) materials.stream().filter(o::equals).count();
        } else if (o instanceof Spice) {
            return spices.getInt(o);
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public List<Material> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    @Override
    public Object2IntMap<Spice> getSpices() {
        return Object2IntMaps.unmodifiable(spices);
    }

    @Override
    public Cookware getCookware() {
        return cookware;
    }

}
