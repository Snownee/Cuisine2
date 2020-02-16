package snownee.cuisine;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.TranslationTextComponent;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.ResearchInfo;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.util.ForgeRegistryArgument;

public final class CuisineCommand {

    private static final SimpleCommandExceptionType INFO_NOT_FOUND = new SimpleCommandExceptionType(new TranslationTextComponent("commands.cuisine.star.info_not_found"));

    private CuisineCommand() {}

    public static LiteralArgumentBuilder<CommandSource> init(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(CuisineAPI.MODID);

        /* off */
        builder
        .then(Commands
                .literal("material")
                .then(Commands
                        .argument("name", new ForgeRegistryArgument<>(CuisineRegistries.MATERIALS))
                        .executes(CuisineCommand::materialInfo)
                        .then(Commands
                                .literal("star")
                                .then(Commands
                                        .argument("star", IntegerArgumentType.integer(0))
                                        .executes(CuisineCommand::starMaterial)
                                )
                        )
                )
        )
        .then(Commands
                .literal("food")
                .then(Commands
                        .argument("name", new ForgeRegistryArgument<>(CuisineRegistries.FOODS))
                        .executes(CuisineCommand::foodInfo)
                        .then(Commands
                                .literal("star")
                                .then(Commands
                                        .argument("star", IntegerArgumentType.integer(0))
                                        .executes(CuisineCommand::starFood)
                                )
                        )
                )
        )
        .then(Commands
                .literal("spice")
                .then(Commands
                        .argument("name", new ForgeRegistryArgument<>(CuisineRegistries.SPICES))
                        .executes(CuisineCommand::spiceInfo)
                )
        )
        .then(Commands
                .literal("hungry")
                .executes(CuisineCommand::hungry)
        );
        /* on */

        return builder;
    }

    private static int materialInfo(CommandContext<CommandSource> ctx) {
        Material material = ctx.getArgument("name", Material.class);
        System.out.println(material.getRegistryName());
        return 0;
    }

    private static int starMaterial(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Material material = ctx.getArgument("name", Material.class);
        ResearchInfo info = CuisineAPI.getResearchInfo(ctx.getSource().assertIsEntity());
        if (info == null) {
            throw INFO_NOT_FOUND.create();
        }
        int star = IntegerArgumentType.getInteger(ctx, "star");
        info.setProgress(material, star);
        return star;
    }

    private static int spiceInfo(CommandContext<CommandSource> ctx) {
        Spice spice = ctx.getArgument("name", Spice.class);
        System.out.println(spice.getRegistryName());
        return 0;
    }

    private static int foodInfo(CommandContext<CommandSource> ctx) {
        CuisineFood food = ctx.getArgument("name", CuisineFood.class);
        System.out.println(food.getRegistryName());
        return 0;
    }

    private static int starFood(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CuisineFood food = ctx.getArgument("name", CuisineFood.class);
        ResearchInfo info = CuisineAPI.getResearchInfo(ctx.getSource().assertIsEntity());
        if (info == null) {
            throw INFO_NOT_FOUND.create();
        }
        int star = IntegerArgumentType.getInteger(ctx, "star");
        info.setProgress(food, star);
        return star;
    }

    private static int hungry(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        try {
            FoodStats stats = ctx.getSource().asPlayer().getFoodStats();
            stats.setFoodLevel(2);
            stats.setFoodSaturationLevel(0);
        } catch (CommandSyntaxException e) {
            throw e;
        }
        return 1;
    }
}
