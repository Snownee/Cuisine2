package snownee.cuisine.data.research;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import snownee.cuisine.api.ResearchInfo;

public class ResearchData extends WorldSavedData {

    private final Map<UUID, ResearchInfo> players = Maps.newHashMap();

    public ResearchData() {
        super("cuisine_research");
    }

    @Override
    public void read(CompoundNBT data) {
        ListNBT listNBT = data.getList("ResearchData", 10);
        for (INBT i : listNBT) {
            CompoundNBT nbt = (CompoundNBT) i;
            ResearchInfoImpl info = new ResearchInfoImpl();
            info.read(nbt);
            UUID id = nbt.getUniqueId("Id");
            players.put(id, info);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        ListNBT list = new ListNBT();
        for (UUID id : players.keySet()) {
            ResearchInfo info = players.getOrDefault(id, ResearchInfo.Empty.INSTANCE);
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUniqueId("Id", id);
            info.write(nbt);
            list.add(nbt);
        }
        data.put("ResearchData", list);
        return data;
    }

    @Override
    public void save(File fileIn) {
        super.save(fileIn);
    }

    public ResearchInfo get(Entity entity) {
        if (!players.containsKey(entity.getUniqueID()))
            players.put(entity.getUniqueID(), new ResearchInfoImpl());
        return players.getOrDefault(entity.getUniqueID(), ResearchInfo.Empty.INSTANCE);
    }

}
