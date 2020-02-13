package snownee.cuisine.data.research;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.ResearchInfo;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;

import java.util.Map;

public class ResearchInfoImpl implements ResearchInfo {
    private final Map<Material, Integer> materialStars = Maps.newHashMap();
    private final Map<CuisineFood, Integer> foodStars = Maps.newHashMap();
    private final Map<Material, Integer> materialProgresses = Maps.newHashMap();
    private final Map<CuisineFood, Integer> foodProgresses = Maps.newHashMap();

    @Override
    public int getStar(Material material) {
        return materialStars.getOrDefault(material, 0);
    }

    @Override
    public int getStar(CuisineFood food) {
        return foodStars.getOrDefault(food, 0);
    }

    @Override
    public int getProgress(Material material) {
        return materialProgresses.getOrDefault(material, 0);
    }

    @Override
    public int getProgress(CuisineFood food) {
        return foodProgresses.getOrDefault(food, 0);
    }

    @Override
    public void setStar(Material material, int star) {
        if (star <= 0)
            materialStars.remove(material);
        else
            materialStars.put(material, star);
    }

    @Override
    public void setStar(CuisineFood food, int star) {
        if (star <= 0)
            foodStars.remove(food);
        else
            foodStars.put(food, star);
    }

    @Override
    public void setProgress(Material material, int progress) {
        if (progress <= 0)
            materialProgresses.remove(material);
        else
            materialProgresses.put(material, progress);
    }

    @Override
    public void setProgress(CuisineFood food, int progress) {
        if (progress <= 0)
            foodProgresses.remove(food);
        else
            foodProgresses.put(food, progress);
    }

    @Override
    public void read(CompoundNBT data) {
        ListNBT list = data.getList("MaterialProgresses",10);
        for (INBT i : list){
            CompoundNBT nbt = (CompoundNBT)i;
            Material material = CuisineRegistries.MATERIALS.getValue(nbt.getInt("k"));
            setProgress(material,nbt.getInt("v"));
        }
        list = data.getList("MaterialStars",10);
        for (INBT i : list){
            CompoundNBT nbt = (CompoundNBT)i;
            Material material = CuisineRegistries.MATERIALS.getValue(nbt.getInt("k"));
            setStar(material,nbt.getInt("v"));
        }
        list = data.getList("FoodProgresses",10);
        for (INBT i : list){
            CompoundNBT nbt = (CompoundNBT)i;
            CuisineFood material = CuisineRegistries.FOODS.getValue(nbt.getInt("k"));
            setProgress(material,nbt.getInt("v"));
        }
        list = data.getList("FoodStars",10);
        for (INBT i : list){
            CompoundNBT nbt = (CompoundNBT)i;
            CuisineFood material = CuisineRegistries.FOODS.getValue(nbt.getInt("k"));
            setStar(material,nbt.getInt("v"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        ListNBT list1 = new ListNBT();
        for (Material material : materialProgresses.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("k",CuisineRegistries.MATERIALS.getID(material));
            compoundNBT.putInt("v",getStar(material));
            list1.add(compoundNBT);
        }
        data.put("MaterialProgresses",list1);
        ListNBT list2 = new ListNBT();
        for (Material material : materialStars.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("k",CuisineRegistries.MATERIALS.getID(material));
            compoundNBT.putInt("v",getProgress(material));
        }
        ListNBT list3 = new ListNBT();
        data.put("MaterialStars",list2);

        for (CuisineFood food : foodProgresses.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("k",CuisineRegistries.FOODS.getID(food));
            compoundNBT.putInt("v",getProgress(food));
        }
        ListNBT list4 = new ListNBT();
        data.put("FoodProgresses",list3);

        for (CuisineFood food : foodStars.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("k",CuisineRegistries.FOODS.getID(food));
            compoundNBT.putInt("v",getStar(food));
        }
        data.put("FoodStars",list4);

        return data;
    }

}
