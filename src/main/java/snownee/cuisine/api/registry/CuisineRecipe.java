package snownee.cuisine.api.registry;

import java.util.Collections;
import java.util.List;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.RecipeRule;

public class CuisineRecipe extends ForgeRegistryEntry<CuisineRecipe> {

    private CuisineFood result;
    private int priority;
    private Cookware cookware;
    private List<RecipeRule> rules = Collections.EMPTY_LIST;

    private CuisineRecipe() {}

    public int getPriority() {
        return priority;
    }

    public CuisineFood getResult() {
        return result;
    }

    public Cookware getCookware() {
        return cookware;
    }

    @LogicalServerSide
    public boolean isValid() {
        return getResult() != null && rules.stream().allMatch(rule -> rule.acceptCookware(cookware));
    }

    @LogicalServerSide
    public boolean matches(FoodBuilder<?> builder) {
        return builder.getCookware() == getCookware() && rules.stream().allMatch(rule -> rule.test(builder));
    }

    @Override
    public String toString() {
        return "CuisineRecipe{" + getRegistryName() + "}";
    }

    public static class Serializer implements RegistrySerializer<CuisineRecipe> {

        @Override
        public CuisineRecipe read(PacketBuffer buf) {
            CuisineRecipe recipe = new CuisineRecipe();
            recipe.cookware = buf.readRegistryIdUnsafe(CuisineRegistries.COOKWARES);
            recipe.result = buf.readRegistryIdUnsafe(CuisineRegistries.FOODS);
            return recipe.setRegistryName(buf.readResourceLocation());
        }

        @Override
        public void write(PacketBuffer buf, CuisineRecipe entry) {

        }

    }
}
