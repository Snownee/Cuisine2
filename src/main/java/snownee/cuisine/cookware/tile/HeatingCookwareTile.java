package snownee.cuisine.cookware.tile;

import net.minecraft.nbt.CompoundNBT;
import snownee.cuisine.api.registry.Cookware;

public class HeatingCookwareTile<C extends CookwareTile<C>> extends CookwareTile<C> {

    public boolean heating;

    public HeatingCookwareTile(Cookware cookware) {
        super(cookware);
    }

    @Override
    public boolean canCook() {
        return heating;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        heating = compound.getBoolean("Heating");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Heating", heating);
        return super.write(compound);
    }

}
