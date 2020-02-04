package snownee.cuisine.plugin.jei;

import java.util.Collections;
import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import snownee.cuisine.processing.ProcessingModule;
import snownee.cuisine.processing.crafting.MillingRecipe;

public class MillingCategory implements IRecipeCategory<MillingRecipe> {
    private final String localizedName;
    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;

    public static final int width = 116;
    public static final int height = 90;

    public MillingCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        localizedName = I18n.format("gui.cuisine.jei.category.milling");
        background = guiHelper.createBlankDrawable(width, height);
        icon = guiHelper.createDrawableIngredient(ProcessingModule.MILL.asItem().getDefaultInstance());
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.MILLING;
    }

    @Override
    public Class<? extends MillingRecipe> getRecipeClass() {
        return MillingRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(MillingRecipe millingRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> item = Collections.singletonList(millingRecipe.getInputItemStack());
        List<List<FluidStack>> fluid = Collections.singletonList(millingRecipe.getInputFluidStack());
        iIngredients.setInputLists(VanillaTypes.ITEM, item);
        iIngredients.setInputLists(VanillaTypes.FLUID, fluid);
        iIngredients.setOutput(VanillaTypes.ITEM, millingRecipe.getOutputItemStack());
        iIngredients.setOutput(VanillaTypes.FLUID, millingRecipe.getOutputFluidStack());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, MillingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        IGuiFluidStackGroup fluidStackGroup = layout.getFluidStacks();
        guiItemStacks.init(0, true, 74, 20);
        guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        guiItemStacks.setBackground(0, guiHelper.getSlotDrawable());
        fluidStackGroup.init(1, true, 24, 20);
        fluidStackGroup.set(1, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        fluidStackGroup.setBackground(1, guiHelper.getSlotDrawable());
        guiItemStacks.init(2, true, 74, 50);
        guiItemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        guiItemStacks.setBackground(2, guiHelper.getSlotDrawable());
        fluidStackGroup.init(3, true, 24, 50);
        fluidStackGroup.set(3, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
        fluidStackGroup.setBackground(3, guiHelper.getSlotDrawable());

    }
}
