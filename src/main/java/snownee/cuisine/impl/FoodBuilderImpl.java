package snownee.cuisine.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.MaterialInstance;
import snownee.cuisine.api.registry.Spice;

public class FoodBuilderImpl<C> implements FoodBuilder<C> {

    private final Cookware cookware;
    private final C context;
    private final List<MaterialInstance> materials = Lists.newArrayList();
    private final List<CuisineFoodInstance> foods = Lists.newArrayList();
    private final Object2IntOpenHashMap<Spice> spices = new Object2IntOpenHashMap<>();
    private final @Nullable Entity cook;

    public FoodBuilderImpl(Cookware cookware, C context, Entity cook) {
        this.cookware = cookware;
        this.context = context;
        this.cook = cook;
    }

    @Override
    public void add(MaterialInstance materialInstance) {
        materials.add(materialInstance);
    }

    @Override
    public void add(Material material) {
        int star = CuisineAPI.getResearchInfo(cook).getStar(material);
        add(new MaterialInstance(material, star));
    }

    @Override
    public void add(CuisineFoodInstance foodInstance) {
        foods.add(foodInstance);
    }

    @Override
    public void add(CuisineFood food) {
        int star = CuisineAPI.getResearchInfo(cook).getStar(food);
        add(new CuisineFoodInstance(food, star));
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
            return materials.stream().map($ -> $.material).anyMatch(o::equals);
        }
        if (o instanceof CuisineFood) {
            return foods.stream().map($ -> $.food).anyMatch(o::equals);
        }
        if (o instanceof Spice) {
            return spices.containsKey(o);
        }
        if (o instanceof MaterialInstance) {
            return materials.contains(o);
        }
        if (o instanceof CuisineFoodInstance) {
            return foods.contains(o);
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public int count(Object o) {
        if (o instanceof Material) {
            return (int) materials.stream().map($ -> $.material).filter(o::equals).count();
        }
        if (o instanceof CuisineFood) {
            return (int) foods.stream().map($ -> $.food).filter(o::equals).count();
        }
        if (o instanceof Spice) {
            return spices.getInt(o);
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public List<MaterialInstance> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    @Override
    public List<CuisineFoodInstance> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    @Override
    public Object2IntMap<Spice> getSpices() {
        return Object2IntMaps.unmodifiable(spices);
    }

    @Override
    public Cookware getCookware() {
        return cookware;
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    @Nullable
    public Entity getCook() {
        return cook;
    }

}
