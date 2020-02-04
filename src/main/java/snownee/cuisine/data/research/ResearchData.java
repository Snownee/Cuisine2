package snownee.cuisine.data.research;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

public class ResearchData extends WorldSavedData {

    private final Map<UUID, ResearchInfoImpl> players = Maps.newHashMap();

    public ResearchData() {
        super("cuisine_research");
    }

    @Override
    public void read(CompoundNBT data) {
        // TODO Auto-generated method stub
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        ListNBT list = new ListNBT();
        
        return data;
    }

    @Override
    public void save(File fileIn) {
        //markDirty(); // for testing
        super.save(fileIn);
    }

}
