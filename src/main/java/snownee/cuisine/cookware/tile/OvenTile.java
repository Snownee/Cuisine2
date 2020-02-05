package snownee.cuisine.cookware.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.inventory.container.OvenContainer;
import snownee.cuisine.util.InvHandlerWrapper;

public class OvenTile extends AbstractCookwareTile implements INamedContainerProvider {

    private final ItemStackHandler inputHandler = new ItemStackHandler(9);
    private final ItemStackHandler outputHandler = new ItemStackHandler();

    public OvenTile() {
        super(CookwareModule.OVEN_TILE, CookwareModule.OVEN_TYPE);
    }

    @Override
    public NonNullList<ItemStack> getMaterialItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.add(new ItemStack(Items.RED_MUSHROOM));
        items.add(new ItemStack(Items.BROWN_MUSHROOM));
        return items;
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
        return new InvHandlerWrapper(new CombinedInvWrapper(inputHandler, outputHandler));
    }

    @Override
    public IItemHandlerModifiable getInputHandler() {
        return inputHandler;
    }

    @Override
    public IItemHandlerModifiable getOutputHandler() {
        return outputHandler;
    }

}
