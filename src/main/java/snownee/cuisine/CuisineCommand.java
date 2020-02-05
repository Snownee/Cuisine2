package snownee.cuisine;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.util.ForgeRegistryArgument;

public final class CuisineCommand {

    private CuisineCommand() {}

    public static LiteralArgumentBuilder<CommandSource> init(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(Cuisine.MODID);

        /* off */
        builder
        .then(Commands
                .literal("material")
                .then(Commands.argument("name", new ForgeRegistryArgument<>(CuisineRegistries.MATERIALS))
                        .executes(CuisineCommand::materialInfo)
                        .then(Commands
                                .literal("star")
                                .then(Commands.argument("star", IntegerArgumentType.integer(0))
                                        .executes(CuisineCommand::setMaterialStar)
                                )
                        )
                )
        )
        .then(Commands
                .literal("food")
                .then(Commands.argument("name", new ForgeRegistryArgument<>(CuisineRegistries.FOODS))
                        .executes(CuisineCommand::foodInfo)
                )
        )
        .then(Commands
                .literal("spice")
                .then(Commands.argument("name", new ForgeRegistryArgument<>(CuisineRegistries.SPICES))
                        .executes(CuisineCommand::spiceInfo)
                )
        );
        /* on */

        return builder;
    }

    private static int materialInfo(CommandContext<CommandSource> ctx) {
        Material material = ctx.getArgument("name", Material.class);
        System.out.println(material.getRegistryName());
        return 0;
    }

    private static int setMaterialStar(CommandContext<CommandSource> ctx) {
        return 0;
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
}
