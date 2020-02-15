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

public class ResearchInfoImpl implements ResearchInfo {
    private final Object2IntOpenHashMap<ResourceLocation> materialStars = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> foodStars = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> materialProgresses = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<ResourceLocation> foodProgresses = new Object2IntOpenHashMap<>();

    @Override
    public int getStar(Material material) {
        return materialStars.getOrDefault(material.getRegistryName(), 0);
    }

    @Override
    public int getStar(CuisineFood food) {
        return foodStars.getOrDefault(food.getRegistryName(), 0);
    }

    @Override
    public int getProgress(Material material) {
        System.out.println(materialProgresses.getOrDefault(material.getRegistryName(), 0));
        System.out.println(material);
        return materialProgresses.getOrDefault(material.getRegistryName(), 0);
    }

    @Override
    public int getProgress(CuisineFood food) {
        return foodProgresses.getOrDefault(food.getRegistryName(), 0);
    }

    @Override
    public void setStar(Material material, int star) {
        if (star <= 0)
            materialStars.removeInt(material.getRegistryName());
        else
            materialStars.put(material.getRegistryName(), star);
    }

    @Override
    public void setStar(CuisineFood food, int star) {
        if (star <= 0)
            foodStars.removeInt(food.getRegistryName());
        else
            foodStars.put(food.getRegistryName(), star);
    }

    @Override
    public void setProgress(Material material, int progress) {
        if (progress <= 0)
            materialProgresses.removeInt(material.getRegistryName());
        else
            materialProgresses.put(material.getRegistryName(), progress);
    }

    @Override
    public void setProgress(CuisineFood food, int progress) {
        if (progress <= 0)
            foodProgresses.removeInt(food.getRegistryName());
        else
            foodProgresses.put(food.getRegistryName(), progress);
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
        ListNBT list1 = new ListNBT();
        for (ResourceLocation material : materialProgresses.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("k", material.toString());
            compoundNBT.putInt("v", materialProgresses.getInt(material));
            list1.add(compoundNBT);
        }
        data.put("MaterialProgresses", list1);
        ListNBT list2 = new ListNBT();
        for (ResourceLocation material : materialStars.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("k", (material.toString()));
            compoundNBT.putInt("v", materialStars.getInt(material));
        }
        ListNBT list3 = new ListNBT();
        data.put("MaterialStars", list2);

        for (ResourceLocation food : foodProgresses.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("k", food.toString());
            compoundNBT.putInt("v", foodProgresses.getInt(food));
        }
        ListNBT list4 = new ListNBT();
        data.put("FoodProgresses", list3);

        for (ResourceLocation food : foodStars.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("k", food.toString());

            compoundNBT.putInt("v", foodStars.getInt(food));
        }
        data.put("FoodStars", list4);

        return data;
    }

}
