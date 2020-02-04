package snownee.cuisine.plugin;

import mezz.jei.api.JeiPlugin;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snownee.cuisine.processing.ProcessingModule;
import snownee.cuisine.processing.crafting.MillingRecipe;

import java.util.ArrayList;
import java.util.List;

public class MillingCategory implements IRecipeCategory<MillingRecipe> {
    private final String localizedName;
    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;
    private static final Logger LOGGER = LogManager.getLogger();

    public static final int width = 116;
    public static final int height = 54;

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
        ArrayList<List<ItemStack>> item = new ArrayList<>();
        ArrayList<List<FluidStack>> fluid = new ArrayList<>();
        item.add(millingRecipe.getInputItemStack());
        fluid.add(millingRecipe.getInputFluidStack());
        iIngredients.setInputLists(VanillaTypes.ITEM,item);
        iIngredients.setInputLists(VanillaTypes.FLUID,fluid);
        iIngredients.setOutput(VanillaTypes.ITEM,millingRecipe.getOutputItemStack());
        iIngredients.setOutput(VanillaTypes.FLUID,millingRecipe.getOutputFluidStack());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, MillingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 94, 18);
        guiItemStacks.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        guiItemStacks.setBackground(0, guiHelper.getSlotDrawable());
        IGuiFluidStackGroup fluidStackGroup = layout.getFluidStacks();
        fluidStackGroup.init(1,true,20,20);
        fluidStackGroup.set(1,ingredients.getOutputs(VanillaTypes.FLUID).get(0));
        fluidStackGroup.setBackground(1, guiHelper.getSlotDrawable());

    }
}
