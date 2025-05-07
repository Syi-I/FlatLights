package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.util.lib.LibTagKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class TooltipHelper {
    /**
     * Formats the toggle tooltip text for tools that have a toggleable state and appends it to the input tooltip
     * @param input The text descriptor that goes before 'enabled/disabled' text
     * @param stackIn The ItemStack for the tooltip being made
     * @param tooltipIn The item's tooltip list
     */
    public static void toggleText(String input, ItemStack stackIn, List<Component> tooltipIn) {
        CompoundTag tag = stackIn.getTag();
        boolean toggled = tag != null && tag.getBoolean(LibTagKeys.MODE_TAG);
        
        if(toggled) {
            tooltipIn.add(Component.literal(input).append(Component.translatable("flatlights.enabled").withStyle(ChatFormatting.GREEN)));
        }
        else {
            tooltipIn.add(Component.literal(input).append(Component.translatable("flatlights.disabled").withStyle(ChatFormatting.RED)));
        }
    }
    
    /**
     * Append the shift tooltip text hint to the input tooltip
     * @param tooltipIn The tooltip of the item
     */
    public static void shiftHint(List<Component> tooltipIn) {
        tooltipIn.add(Component.literal("[").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("tooltip.flatlights.hold_shift").withStyle(ChatFormatting.WHITE))
                .append(Component.literal("]").withStyle(ChatFormatting.GOLD)));
    }
    
    /**
     * Format the usage description of a tooltip's text and append the formatted text to the input tooltip
     * @param tooltipIn The tooltip of the item
     * @param translateKeyIn The translation key for the item's usage description
     */
    public static void formatUsage(List<Component> tooltipIn, String translateKeyIn) {
        tooltipIn.add(Component.translatable(translateKeyIn).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
    }
    
    /**
     * Puts text input in between brackets and appends the formatted text to the input tooltip
     * @param tooltipIn The tooltip of the item
     * @param input The text that goes between the brackets
     * @param styleIn An optional color/style for the input text
     */
    public static void genericBrackets(List<Component> tooltipIn, String input, @Nullable Style styleIn) {
        //no style input, leave information between brackets with default style
        if(styleIn == null) {
            tooltipIn.add(Component.literal("[").withStyle(ChatFormatting.AQUA)
                    .append(Component.literal(input).withStyle(ChatFormatting.RESET))
                    .append(Component.literal("]").withStyle(ChatFormatting.AQUA)));
        }
        else {
            tooltipIn.add(Component.literal("[").withStyle(ChatFormatting.AQUA)
                    .append(Component.literal(input).withStyle(styleIn))
                    .append(Component.literal("]").withStyle(ChatFormatting.AQUA)));
        }
    }
    
    /**
     * Create and append formatted bracket tooltip (i.e. [<label>:<desc>])
     * @param tooltipIn Tooltip of the item
     * @param label Label string for the tooltip
     * @param color1 Color/style formatting for the label text of the tooltip. Defaults to WHITE if null.
     * @param desc Description string that follows after the label text
     * @param color2 Color/style formatting for the description text of the tooltip. Defaults to WHITE if null.
     */
    public static void labelBrackets(List<Component> tooltipIn, String label, @Nullable Style color1, String desc, @Nullable Style color2) {
        //if color1 is null default to white text
        Style style1 = color1 != null ? color1 : Style.EMPTY.withColor(ChatFormatting.WHITE);
        Component formattedLabel;
        if(color2 != null) {
            formattedLabel = Component.literal(label).withStyle(style1)
                    .append(Component.literal(": ").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(desc).withStyle(color2));
        }
        else {
            formattedLabel = Component.literal(label).withStyle(style1)
                    .append(Component.literal(": ").withStyle(ChatFormatting.RESET))
                    .append(Component.literal(desc));
        }
        tooltipIn.add(Component.literal("[").withStyle(ChatFormatting.AQUA)
                .append(formattedLabel)
                .append(Component.literal("]").withStyle(ChatFormatting.AQUA)));
    }
    
    /**
     * Create and append formatted bracket tooltip (i.e. [<label>:<desc>])
     * @param tooltipIn Tooltip of the item.
     * @param label Label string for the tooltip.
     * @param color1 Color/style formatting for the label text of the tooltip. Defaults to WHITE if null.
     * @param desc Description component that follows after the label text.
     */
    public static void labelBrackets(List<Component> tooltipIn, String label, @Nullable Style color1, Component desc) {
        //if color1 is null default to white text
        Style style1 = color1 != null ? color1 : Style.EMPTY.withColor(ChatFormatting.WHITE);
        Component formattedLabel;
        formattedLabel = Component.literal(label).withStyle(style1)
                .append(Component.literal(": ").withStyle(ChatFormatting.RESET))
                .append(desc);
        tooltipIn.add(Component.literal("[").withStyle(ChatFormatting.AQUA)
                .append(formattedLabel)
                .append(Component.literal("]").withStyle(ChatFormatting.AQUA)));
    }
    
    /**
     * Creates a tooltip component for potion effects, similar to what stat attributes look like (e.g. +2 Attack Damage, in blue text at the bottom of a tooltip description)
     * @param attribute The potion effect name that is being formatted to look like an attribute
     */
    public static void potionAttribute(String attribute, List<Component> tooltipIn) {
        tooltipIn.add(Component.literal("+Potion Effect: " + attribute).withStyle(ChatFormatting.BLUE));
    }
}
