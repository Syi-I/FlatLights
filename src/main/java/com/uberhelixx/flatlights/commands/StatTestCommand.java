package com.uberhelixx.flatlights.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PLAYER_CORETRACKER_TAG;

public class StatTestCommand {
    public StatTestCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("flatlights").then(Commands.literal("test").executes((command) -> {
            return getStat(command.getSource());
        })));
    }

    private int getStat(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        if(!player.getPersistentData().isEmpty() && player.getPersistentData().getCompound("PlayerPersisted").contains(PLAYER_CORETRACKER_TAG)) {
            source.sendFeedback(new StringTextComponent("Total Cores: " + MiscHelpers.coloredText(TextFormatting.DARK_PURPLE,"" + player.getPersistentData().getCompound("PlayerPersisted").getInt(PLAYER_CORETRACKER_TAG))), true);
            return 1;
        }
        else {
            String theSilly = "wee woo";
            double rng = Math.random();
            if(rng > 0.9987) {
                theSilly = "When the gacha luck finally hits but it's just a test command in Minecraft:";
            }
            else if(rng > 0.9) {
                theSilly = "*dies from peak fiction*";
            }
            else if(rng > 0.85) {
                theSilly = "Never cook again.";
            }
            else if(rng > 0.8) {
                theSilly = "Google en passant";
            }
            else if(rng > 0.75) {
                theSilly = "Okay but have you tried GTNH it's a great beginner modpack";
            }
            source.sendFeedback(new StringTextComponent(theSilly), true);
            return -1;
        }
    }
}
