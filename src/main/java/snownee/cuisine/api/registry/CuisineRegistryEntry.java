package snownee.cuisine.api.registry;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.LogicalServerSide;

public abstract class CuisineRegistryEntry<V extends CuisineRegistryEntry<V>> extends ForgeRegistryEntry<V> {

    protected volatile boolean valid;

    @LogicalServerSide
    public abstract boolean validate();

    @LogicalServerSide
    public void invalidate() {
        valid = false;
    }

    @LogicalServerSide
    public boolean isValid() {
        return valid;
    }

    public abstract ITextComponent getDisplayName();

}
