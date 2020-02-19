package snownee.cuisine.api.registry;

import java.util.Collections;
import java.util.List;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.LogicalServerSide;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.impl.rule.NoFoodRecipeRule;

public class CuisineRecipe extends CuisineRegistryEntry<CuisineRecipe> {

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

    @Override
    @LogicalServerSide
    public boolean validate() {
        if (rules.stream().noneMatch(RecipeRule::isFoodRule)) {
            rules.add(NoFoodRecipeRule.INSTANCE);
        }
        return valid = getResult() != null && rules.stream().allMatch(rule -> rule.acceptCookware(cookware));
    }

    @LogicalServerSide
    public boolean matches(FoodBuilder<?> builder) {
        return isValid() && builder.getCookware() == getCookware() && rules.stream().allMatch(rule -> rule.test(builder));
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
            return recipe;
        }

        @Override
        public void write(PacketBuffer buf, CuisineRecipe entry) {
            buf.writeRegistryIdUnsafe(CuisineRegistries.COOKWARES, entry.cookware);
            buf.writeRegistryIdUnsafe(CuisineRegistries.FOODS, entry.result);
        }

    }

    @Override
    public ITextComponent getDisplayName() {
        return result.getDisplayName();
    }
}
