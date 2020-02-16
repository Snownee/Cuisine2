package snownee.cuisine.data.research;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.ResearchInfo;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;

import static snownee.cuisine.CoreModule.makeResearchDataDirty;

public class ResearchInfoImpl implements ResearchInfo {
    private final Object2IntOpenHashMap<ResourceLocation> materialStars = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> foodStars = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> materialProgresses = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> foodProgresses = new Object2IntOpenHashMap<>();

    @Override
    public int getStar(Material material) {
        return materialStars.getInt(material.getRegistryName());
    }

    @Override
    public int getStar(CuisineFood food) {
        return foodStars.getInt(food.getRegistryName());
    }

    @Override
    public int getProgress(Material material) {
        return materialProgresses.getInt(material.getRegistryName());
    }

    @Override
    public int getProgress(CuisineFood food) {
        return foodProgresses.getInt(food.getRegistryName());
    }

    @Override
    public void setStar(Material material, int star) {
        if (star <= 0)
            materialStars.removeInt(material.getRegistryName());
        else
            materialStars.put(material.getRegistryName(), star);
        makeResearchDataDirty();

    }

    @Override
    public void setStar(CuisineFood food, int star) {
        if (star <= 0)
            foodStars.removeInt(food.getRegistryName());
        else
            foodStars.put(food.getRegistryName(), star);
        makeResearchDataDirty();

    }

    @Override
    public void setProgress(Material material, int progress) {
        if (progress <= 0)
            materialProgresses.removeInt(material.getRegistryName());
        else
            materialProgresses.put(material.getRegistryName(), progress);
        makeResearchDataDirty();

    }

    @Override
    public void setProgress(CuisineFood food, int progress) {
        if (progress <= 0)
            foodProgresses.removeInt(food.getRegistryName());
        else
            foodProgresses.put(food.getRegistryName(), progress);
        makeResearchDataDirty();
    }

    @Override
    public void addStar(Material material, int star) {
        materialStars.addTo(material.getRegistryName(),star);
        makeResearchDataDirty();

    }

    @Override
    public void addStar(CuisineFood food, int star) {
        foodStars.addTo(food.getRegistryName(),star);
        makeResearchDataDirty();

    }

    @Override
    public void addProgress(Material material, int progress) {
        materialProgresses.addTo(material.getRegistryName(),progress);
        makeResearchDataDirty();

    }

    @Override
    public void addProgress(CuisineFood food, int progress) {
        foodProgresses.addTo(food.getRegistryName(),progress);
        makeResearchDataDirty();

    }

    @Override
    public void read(CompoundNBT data) {
        ListNBT list = data.getList("MaterialProgresses", 10);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            Material material = CuisineRegistries.MATERIALS.getValue(new ResourceLocation(nbt.getString("k")));
            setProgress(material, nbt.getInt("v"));
        }
        list = data.getList("MaterialStars", 10);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            Material material = CuisineRegistries.MATERIALS.getValue(new ResourceLocation(nbt.getString("k")));
            setStar(material, nbt.getInt("v"));
        }
        list = data.getList("FoodProgresses", 10);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            CuisineFood material = CuisineRegistries.FOODS.getValue(new ResourceLocation(nbt.getString("k")));
            setProgress(material, nbt.getInt("v"));
        }
        list = data.getList("FoodStars", 10);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            CuisineFood material = CuisineRegistries.FOODS.getValue(new ResourceLocation(nbt.getString("k")));
            setStar(material, nbt.getInt("v"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data.put("MaterialProgresses", writeHelp(materialProgresses));
        data.put("MaterialStars", writeHelp(materialStars));
        data.put("FoodProgresses", writeHelp(foodProgresses));
        data.put("FoodStars", writeHelp(foodStars));
        return data;
    }

    private ListNBT writeHelp(Object2IntOpenHashMap<ResourceLocation> foodStars) {
        ListNBT list = new ListNBT();
        for (ResourceLocation food : foodStars.keySet()) {
            int r = foodProgresses.getInt(food);
            if (r>0){
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putString("k", food.toString());
                compoundNBT.putInt("v",r );
                list.add(compoundNBT);
            }
        }
        return list;
    }

}
