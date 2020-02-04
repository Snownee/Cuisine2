package snownee.cuisine.plugin;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import snownee.cuisine.Cuisine;
import snownee.cuisine.processing.ProcessingModule;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final ResourceLocation MILLING = new ResourceLocation(Cuisine.MODID, "milling");

    @Override
    public ResourceLocation getPluginUid() {
        return MILLING;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MillingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Registry.RECIPE_TYPE.containsKey(MILLING)) {
            ClientWorld world = Minecraft.getInstance().world;
            RecipeManager recipeManager = world.getRecipeManager();
            registration.addRecipes(recipeManager.getRecipes(ProcessingModule.Mill_RECIPE_TYPE).values(), MILLING);
        }
    }
}