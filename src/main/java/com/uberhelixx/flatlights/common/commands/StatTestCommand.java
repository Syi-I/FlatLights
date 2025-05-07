package com.uberhelixx.flatlights.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StatTestCommand {
    public StatTestCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("flatlights").then(Commands.literal("test").executes(this::getStat)));
    }
    
    private int getStat(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if(player != null && PrismaticBladeMk2.hasCoreTracker(player)) {
            int count = PrismaticBladeMk2.getPlayerCores(player);
            Component message = Component.literal("Total Cores: ").withStyle(ChatFormatting.WHITE)
                    .append(String.valueOf(count)).withStyle(ChatFormatting.DARK_PURPLE);
            context.getSource().sendSuccess(() -> message, true);
            return 1;
        }
        else {
            String theSilly;
            double rng = Math.random();
            if(rng > 0.9987) {
                theSilly = "When the gacha luck finally hits but it's just a test command in Minecraft:";
            } else {
                theSilly = "wee woo";
            }
            context.getSource().sendSuccess(() -> Component.literal(theSilly), true);
            return -1;
        }
    }
}
