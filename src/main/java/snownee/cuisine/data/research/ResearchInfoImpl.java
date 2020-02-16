package snownee.cuisine.data.research;

import static snownee.cuisine.CoreModule.makeResearchDataDirty;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.ResearchInfo;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.kiwi.util.Util;

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
        materialStars.addTo(material.getRegistryName(), star);
        makeResearchDataDirty();

    }

    @Override
    public void addStar(CuisineFood food, int star) {
        foodStars.addTo(food.getRegistryName(), star);
        makeResearchDataDirty();

    }

    @Override
    public void addProgress(Material material, int incr) {
        materialProgresses.addTo(material.getRegistryName(), incr);
        makeResearchDataDirty();

    }

    @Override
    public void addProgress(CuisineFood food, int incr) {
        foodProgresses.addTo(food.getRegistryName(), incr);
        makeResearchDataDirty();

    }

    @Override
    public void read(CompoundNBT data) {
        ListNBT list = data.getList("MaterialProgresses", Constants.NBT.TAG_COMPOUND);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            ResourceLocation id = Util.RL(nbt.getString("k"), CuisineAPI.MODID);
            materialProgresses.put(id, nbt.getInt("v"));
        }
        list = data.getList("MaterialStars", Constants.NBT.TAG_COMPOUND);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            ResourceLocation id = Util.RL(nbt.getString("k"), CuisineAPI.MODID);
            materialStars.put(id, nbt.getInt("v"));
        }
        list = data.getList("FoodProgresses", Constants.NBT.TAG_COMPOUND);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            ResourceLocation id = Util.RL(nbt.getString("k"), CuisineAPI.MODID);
            foodProgresses.put(id, nbt.getInt("v"));
        }
        list = data.getList("FoodStars", Constants.NBT.TAG_COMPOUND);
        for (INBT i : list) {
            CompoundNBT nbt = (CompoundNBT) i;
            ResourceLocation id = Util.RL(nbt.getString("k"), CuisineAPI.MODID);
            foodStars.put(id, nbt.getInt("v"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data.put("MaterialProgresses", writeList(materialProgresses));
        data.put("MaterialStars", writeList(materialStars));
        data.put("FoodProgresses", writeList(foodProgresses));
        data.put("FoodStars", writeList(foodStars));
        return data;
    }

    private ListNBT writeList(Object2IntOpenHashMap<ResourceLocation> map) {
        ListNBT list = new ListNBT();
        for (ResourceLocation id : map.keySet()) {
            int r = foodProgresses.getInt(id);
            if (r > 0) {
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putString("k", Util.trimRL(id, CuisineAPI.MODID));
                compoundNBT.putInt("v", r);
                list.add(compoundNBT);
            }
        }
        return list;
    }

}
