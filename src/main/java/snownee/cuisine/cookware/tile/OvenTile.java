package snownee.cuisine.cookware.tile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.inventory.container.OvenContainer;
import snownee.cuisine.util.InvHandlerWrapper;

public class OvenTile extends AbstractCookwareTile implements INamedContainerProvider {

    private final ItemStackHandler inputHandler = new ItemStackHandler(9);
    private final ItemStackHandler outputHandler = new ItemStackHandler();
    private final LazyOptional<IItemHandlerModifiable> inputProvider = LazyOptional.of(() -> inputHandler);
    private final LazyOptional<IItemHandlerModifiable> outputProvider = LazyOptional.of(() -> outputHandler);
    private final LazyOptional<IItemHandlerModifiable> unsidedProvider = LazyOptional.of(() -> new CombinedInvWrapper(inputHandler, outputHandler));

    public OvenTile() {
        super(CookwareModule.OVEN_TILE, CookwareModule.OVEN_TYPE);
    }

    @Override
    public List<ItemStack> getMaterialItems() {
        IItemHandler handler = getInputHandler();
        return IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot).filter($ -> !$.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new OvenContainer(id, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuisine.oven");
    }

    public IInventory getInventory() {
        return new InvHandlerWrapper(unsidedProvider.orElseGet(EmptyHandler::new));
    }

    @Override
    public IItemHandlerModifiable getInputHandler() {
        return inputHandler;
    }

    @Override
    public IItemHandlerModifiable getOutputHandler() {
        return outputHandler;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getItemCap(side).cast();
        }
        return super.getCapability(cap, side);
    }

    public LazyOptional<? extends IItemHandler> getItemCap(@Nullable Direction side) {
        switch (side) {
        case UP:
            return inputProvider;
        case DOWN:
            return outputProvider;
        default:
            return unsidedProvider;
        }
    }

    @Override
    public void remove() {
        super.remove();
        unsidedProvider.invalidate();
        inputProvider.invalidate();
        outputProvider.invalidate();
    }

    @Override
    public void read(CompoundNBT compound) {
        inputHandler.deserializeNBT(compound.getCompound("Input"));
        outputHandler.deserializeNBT(compound.getCompound("Output"));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Input", inputHandler.serializeNBT());
        compound.put("Output", outputHandler.serializeNBT());
        return super.write(compound);
    }

}
