package snownee.cuisine.base.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import snownee.cuisine.api.tile.KitchenTile;
import snownee.cuisine.base.BaseModule;
import snownee.cuisine.util.InvHandlerWrapper;

public class CabinetTile extends KitchenTile implements INamedContainerProvider {

    private final LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> new ItemStackHandler(27));

    public CabinetTile() {
        super(BaseModule.CABINET_TILE, "0", "1");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return ChestContainer.createGeneric9X3(id, playerInventory, new InvHandlerWrapper(itemHandler.orElse(null)));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Util.makeTranslationKey("container", getType().getRegistryName()));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        itemHandler.invalidate();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.orElse(null).deserializeNBT(compound.getCompound("Inv"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inv", itemHandler.orElse(null).serializeNBT());
        return super.write(compound);
    }

}
