package snownee.cuisine.data;

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
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
    private ImmutableSet<Spice> spices;
    // https://stackoverflow.com/questions/4062919/why-does-exist-weakhashmap-but-absent-weakset
    public final Set<PlayerEntity> syncedPlayers = Collections.newSetFromMap(new WeakHashMap<>());

    public RecordData(int id) {
        super("cuisine/recipe_" + id);
    }

    @Override
    public void read(CompoundNBT data) {
        cookware = CuisineRegistries.COOKWARES.getValue(Util.RL(data.getString(CuisineConst.COOKWARE), CuisineAPI.MODID));
        result = CuisineRegistries.FOODS.getValue(Util.RL(data.getString(CuisineConst.FOOD), CuisineAPI.MODID));

        ListNBT matList = data.getList(CuisineConst.MATERIALS, Constants.NBT.TAG_STRING);
        ImmutableList.Builder<Material> matBuilder = ImmutableList.builder();
        for (INBT tag : matList) {
            matBuilder.add(CuisineRegistries.MATERIALS.getValue(Util.RL(tag.getString(), CuisineAPI.MODID)));
        }
        materials = matBuilder.build();

        ListNBT foodList = data.getList(CuisineConst.FOODS, Constants.NBT.TAG_STRING);
        ImmutableList.Builder<CuisineFood> foodBuilder = ImmutableList.builder();
        for (INBT tag : foodList) {
            foodBuilder.add(CuisineRegistries.FOODS.getValue(Util.RL(tag.getString(), CuisineAPI.MODID)));
        }
        foods = foodBuilder.build();

        ListNBT spiceList = data.getList(CuisineConst.SPICES, Constants.NBT.TAG_STRING);
        ImmutableSet.Builder<Spice> spiceBuilder = ImmutableSet.builder();
        for (INBT tag : spiceList) {
            spiceBuilder.add(CuisineRegistries.SPICES.getValue(Util.RL(tag.getString(), CuisineAPI.MODID)));
        }
        spices = spiceBuilder.build();
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data.putString(CuisineConst.COOKWARE, Util.trimRL(cookware.getRegistryName(), CuisineAPI.MODID));
        data.putString("Result", Util.trimRL(result.getRegistryName(), CuisineAPI.MODID));
        ListNBT matList = new ListNBT();
        for (Material material : materials) {
            matList.add(StringNBT.valueOf(Util.trimRL(material.getRegistryName(), CuisineAPI.MODID)));
        }
        data.put(CuisineConst.MATERIALS, matList);
        ListNBT foodList = new ListNBT();
        for (CuisineFood food : foods) {
            foodList.add(StringNBT.valueOf(Util.trimRL(food.getRegistryName(), CuisineAPI.MODID)));
        }
        data.put(CuisineConst.FOODS, foodList);
        ListNBT spiceList = new ListNBT();
        for (Spice spice : spices) {
            spiceList.add(StringNBT.valueOf(Util.trimRL(spice.getRegistryName(), CuisineAPI.MODID)));
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
        spices = ImmutableSet.copyOf(builder.getSpices());
        markDirty();
    }

    public static RecordData read(PacketBuffer buf, int rid) {
        RecordData data = new RecordData(rid);
        data.cookware = buf.readRegistryIdUnsafe(CuisineRegistries.COOKWARES);
        data.result = buf.readRegistryIdUnsafe(CuisineRegistries.FOODS);

        ImmutableList.Builder<Material> matBuilder = ImmutableList.builder();
        for (int id : buf.readVarIntArray(16)) {
            matBuilder.add(CuisineRegistries.MATERIALS.getValue(id));
        }
        data.materials = matBuilder.build();

        ImmutableList.Builder<CuisineFood> foodBuilder = ImmutableList.builder();
        for (int id : buf.readVarIntArray(16)) {
            foodBuilder.add(CuisineRegistries.FOODS.getValue(id));
        }
        data.foods = foodBuilder.build();

        ImmutableSet.Builder<Spice> spiceBuilder = ImmutableSet.builder();
        for (int id : buf.readVarIntArray(16)) {
            spiceBuilder.add(CuisineRegistries.SPICES.getValue(id));
        }
        data.spices = spiceBuilder.build();

        data.markDirty();
        return data;
    }

    public void write(PacketBuffer buf, int id) {
        buf.writeVarInt(id);
        buf.writeRegistryIdUnsafe(CuisineRegistries.COOKWARES, cookware);
        buf.writeRegistryIdUnsafe(CuisineRegistries.FOODS, result);

        buf.writeVarInt(materials.size());
        for (Material material : materials) {
            buf.writeRegistryIdUnsafe(CuisineRegistries.MATERIALS, material);
        }

        buf.writeVarInt(foods.size());
        for (CuisineFood food : foods) {
            buf.writeRegistryIdUnsafe(CuisineRegistries.FOODS, food);
        }

        buf.writeVarInt(spices.size());
        for (Spice spice : spices) {
            buf.writeRegistryIdUnsafe(CuisineRegistries.SPICES, spice);
        }
    }

    public Cookware getCookware() {
        return cookware;
    }

    public CuisineFood getResult() {
        return result;
    }

}
