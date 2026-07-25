package com.boatmod;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class BoatCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("boat")
                .then(CommandManager.literal("bb")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        ServerPlayerEntity player = source.getPlayer();
                        if (player != null) {
                            BoatSpawnLogic.spawnBigBoat(player);
                            source.sendFeedback(new LiteralText("Big BOAT spawned!"), true);
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(CommandManager.literal("sb")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        ServerPlayerEntity player = source.getPlayer();
                        if (player != null) {
                            BoatSpawnLogic.spawnSmallBoat(player);
                            source.sendFeedback(new LiteralText("Small BOAT spawned!"), true);
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                )
            );
        });
    }
}
