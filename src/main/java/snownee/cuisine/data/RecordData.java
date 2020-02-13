package snownee.cuisine.data;

import java.io.File;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineConst;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.MaterialInstance;
import snownee.cuisine.api.registry.Spice;
import snownee.kiwi.util.Util;

public class RecordData extends WorldSavedData {

    private CuisineFood result;
    private Cookware cookware;
    private ImmutableList<Material> materials;
    private ImmutableList<CuisineFood> foods;
    private Object2IntMap<Spice> spices;

    public RecordData(String name) {
        super(name);
    }

    @Override
    public void read(CompoundNBT data) {
        cookware = CuisineRegistries.COOKWARES.getValue(Util.RL(data.getString(CuisineConst.COOKWARE), CuisineAPI.MODID));
        result = CuisineRegistries.FOODS.getValue(Util.RL(data.getString(CuisineConst.FOOD), CuisineAPI.MODID));

        ListNBT materialList = data.getList(CuisineConst.MATERIALS, Constants.NBT.TAG_STRING);
        ImmutableList.Builder<Material> materialBuilder = ImmutableList.builder();
        for (INBT tag : materialList) {
            materialBuilder.add(CuisineRegistries.MATERIALS.getValue(Util.RL(tag.getString(), CuisineAPI.MODID)));
        }
        materials = materialBuilder.build();

        ListNBT foodList = data.getList(CuisineConst.FOODS, Constants.NBT.TAG_STRING);
        ImmutableList.Builder<CuisineFood> foodBuilder = ImmutableList.builder();
        for (INBT tag : foodList) {
            foodBuilder.add(CuisineRegistries.FOODS.getValue(Util.RL(tag.getString(), CuisineAPI.MODID)));
        }
        foods = foodBuilder.build();

        ListNBT spiceList = data.getList(CuisineConst.SPICES, Constants.NBT.TAG_COMPOUND);
        spices = new Object2IntArrayMap<>(spiceList.size());
        for (INBT tag : foodList) {
            Spice spice = CuisineRegistries.SPICES.getValue(Util.RL(((CompoundNBT) tag).getString("Id"), CuisineAPI.MODID));
            int amount = ((CompoundNBT) tag).getInt("Amount");
            spices.put(spice, amount);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data.putString(CuisineConst.COOKWARE, Util.trimRL(cookware.getRegistryName(), CuisineAPI.MODID));
        data.putString("Result", Util.trimRL(result.getRegistryName(), CuisineAPI.MODID));
        ListNBT materialList = new ListNBT();
        for (Material material : materials) {
            materialList.add(StringNBT.valueOf(Util.trimRL(material.getRegistryName(), CuisineAPI.MODID)));
        }
        data.put(CuisineConst.MATERIALS, materialList);
        ListNBT foodList = new ListNBT();
        for (CuisineFood food : foods) {
            foodList.add(StringNBT.valueOf(Util.trimRL(food.getRegistryName(), CuisineAPI.MODID)));
        }
        data.put(CuisineConst.FOODS, foodList);
        ListNBT spiceList = new ListNBT();
        for (Spice spice : spices.keySet()) {
            CompoundNBT spiceTag = new CompoundNBT();
            spiceTag.putString("Id", Util.trimRL(spice.getRegistryName(), CuisineAPI.MODID));
            spiceTag.putInt("Amount", spices.getInt(spice));
            spiceList.add(spiceTag);
        }
        data.put(CuisineConst.SPICES, spiceList);
        return data;
    }

    @Override
    public void save(File fileIn) {
        if (isDirty()) {
            File parentFile = fileIn.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
        }
        super.save(fileIn);
    }

    public void put(FoodBuilder<?> builder, CuisineRecipe recipe) {
        //TODO context support
        cookware = builder.getCookware();
        result = recipe.getResult();
        materials = builder.getMaterials().stream().map(MaterialInstance::getLeft).collect(ImmutableList.toImmutableList());
        foods = builder.getFoods().stream().map(CuisineFoodInstance::getLeft).collect(ImmutableList.toImmutableList());
        spices = new Object2IntArrayMap<>(builder.getSpices());
        markDirty();
    }

}
