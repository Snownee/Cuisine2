package test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.kiwi.Kiwi;
import snownee.kiwi.util.LootDumper;

import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher, boolean integrated) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(Kiwi.MODID);
        if (integrated) {
            builder.then(Commands.literal("test")
                    .executes(ctx -> dumpLoots(ctx.getSource())));
        }

        dispatcher.register(builder);
    }
    public static int dumpLoots(CommandSource source) throws CommandSyntaxException {
        try {
            System.out.println(111);
            for (ResourceLocation i :CuisineRegistries.MATERIALS.getKeys()){
                System.out.println(CuisineAPI.getResearchInfo(source.getEntity()).getProgress(CuisineRegistries.MATERIALS.getValue(i)));
                CuisineAPI.getResearchInfo(source.getEntity()).setProgress(CuisineRegistries.MATERIALS.getValue(i),
                        CuisineAPI.getResearchInfo(source.getEntity()).getProgress(CuisineRegistries.MATERIALS.getValue(i))+1);
            }
        } catch (PatternSyntaxException e) {
            System.out.println(222);

        }
        return 0;
    }
}
