package snownee.cuisine.cookware.tile;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.tile.KitchenTile;

abstract public class AbstractCookwareTile extends KitchenTile {

    protected CuisineRecipe cachedLastRecipe;
    private final Cookware cookware;

    public AbstractCookwareTile(TileEntityType<?> tileEntityTypeIn, Cookware cookware, String... textureKeys) {
        super(tileEntityTypeIn, textureKeys);
        this.cookware = cookware;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

        }
        return super.getCapability(cap, side);
    }

    public CuisineRecipe cook(Entity entity) {
        FoodBuilder<AbstractCookwareTile> builder = foodBuilder(this, entity);
        if (cachedLastRecipe != null && cachedLastRecipe.matches(builder)) {
            return cachedLastRecipe;
        }
        Optional<CuisineRecipe> recipe = builder.findRecipe();
        if (recipe.isPresent()) {
            return cachedLastRecipe = recipe.get();
        } else {
            return null;
        }
    }

    public <C> FoodBuilder<C> foodBuilder(C context, @Nullable Entity cook) {
        FoodBuilder<C> builder = CuisineAPI.foodBuilder(getCookware(), context, cook);
        /* off */
        getMaterialItems().stream()
                .map(CuisineAPI::findMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(builder::add);
        /* on */
        return builder;
    }

    abstract public NonNullList<ItemStack> getMaterialItems();

    public Cookware getCookware() {
        return cookware;
    }

}
