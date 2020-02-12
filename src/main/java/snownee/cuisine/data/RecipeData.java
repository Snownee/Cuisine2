package snownee.cuisine.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import snownee.cuisine.api.FoodBuilder;

public class RecipeData extends WorldSavedData {

    public RecipeData(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void read(CompoundNBT data) {
        // TODO Auto-generated method stub

    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        // TODO Auto-generated method stub
        return data;
    }

    public void put(FoodBuilder<?> builder) {
        markDirty();
    }

}
