package com.uberhelixx.flatlights.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class TextHelpers {

    /**
     * Puts text argument in between {@code TextFormatting.AQUA} colored square brackets
     * @param textIn The text string that is between the brackets
     * @param colorIn The color of the text between the brackets, if null then no color formatting will be applied to the input text
     * @return The formatted text, now between brackets
     */
    public static ITextComponent genericBrackets(String textIn, @Nullable TextFormatting colorIn) {
        //don't do any color formatting for the input text if colorIn is null
        if(colorIn == null) {
            return ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + "[" + TextFormatting.RESET + textIn + TextFormatting.AQUA + "]");
        }
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + "[" + colorIn + textIn + TextFormatting.AQUA + "]");
    }

    /**
     * Creates a label tooltip between {@code TextFormatting.AQUA} brackets, with separated colors for the label and description
     * @param label The text for the label of the tooltip
     * @param labelColor The color for the label text, leaving null defaults to {@code TextFormatting.WHITE}
     * @param description The description that follows the label text
     * @param descColor The color of the description text, can be null if you are putting in extra/conditional formatting
     * @return The formatted label tooltip
     */
    public static ITextComponent labelBrackets(String label, @Nullable TextFormatting labelColor, String description, @Nullable TextFormatting descColor) {
        TextFormatting color1 = labelColor != null ? labelColor : TextFormatting.WHITE;
        TextFormatting color2 = descColor;
        String formattedLabel;
        //if description color is not null, use input color
        if(color2 != null) {
            formattedLabel = color1 + label + ": " + color2 + description;
        }
        //if description color is null, keep whatever input string formatting was given
        else {
            formattedLabel = color1 + label + ": " + TextFormatting.RESET + description;
        }

        //use generic brackets with the formatted text label
        return genericBrackets(formattedLabel, null);
    }

    /**
     * Creates the {@code SHIFT} tooltip hint
     * @param description A short text description of what holding SHIFT does/shows, can be left null to just have the SHIFT hint
     * @return The formatted SHIFT tooltip w/ optional description
     */
    public static ITextComponent shiftTooltip(@Nullable String description) {
        //if there's a description after the SHIFT prompt add it
        if(description != null) {
            return ITextComponent.getTextComponentOrEmpty(TextFormatting.GOLD + "[" + TextFormatting.WHITE + "Shift" + TextFormatting.GOLD + "] " + TextFormatting.WHITE + description);
        }
        //otherwise just add the SHIFT tooltip hint
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.GOLD + "[" + TextFormatting.WHITE + "Shift" + TextFormatting.GOLD + "]");
    }
    
    /**
     * Creates a tooltip component for potion effects, similar to what stat attributes look like (e.g. +2 Attack Damage, in blue text at the bottom of a tooltip description)
     * @param attribute The potion effect name that is being formatted to look like an attribute
     * @return The formatted tooltip as an {@link ITextComponent}
     */
    public static ITextComponent potionAttribute(String attribute) {
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.BLUE + "+Potion Effect: " + attribute);
    }
}
