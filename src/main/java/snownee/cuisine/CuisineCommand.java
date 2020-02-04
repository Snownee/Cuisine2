package snownee.cuisine;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.registry.Material;
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
                        .executes(ctx -> {
                            Material material = ctx.getArgument("name", Material.class);
                            System.out.println(material.getRegistryName());
                            return 0;
                        })
                        .then(Commands
                                .literal("star")
                                .then(Commands.argument("star", IntegerArgumentType.integer(0))
                                        .executes(ctx -> 0))
                        )
                )
        )
        .then(Commands
                .literal("spice")
                .then(Commands.argument("name", new ForgeRegistryArgument<>(CuisineRegistries.SPICES)))
        );
        /* on */

        return builder;
    }
}
