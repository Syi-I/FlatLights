package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.util.lib.LibBlockNames;

import java.util.HashMap;
import java.util.Map;

public class ColorPairUtils {
    public static Map<String, String> colorPairs = new HashMap<>();
    
    /**
     * Pair up the corresponding {@code Vanilla Dye} and {@code Hue Shift} colors
     */
    public static void pairUp() {
        colorPairs.put("black", LibBlockNames.BLACK_COLOR_SHIFT);
        colorPairs.put("blue", LibBlockNames.BLUE_COLOR_SHIFT);
        colorPairs.put("brown", LibBlockNames.BROWN_COLOR_SHIFT);
        colorPairs.put("cyan", LibBlockNames.CYAN_COLOR_SHIFT);
        colorPairs.put("gray", LibBlockNames.GRAY_COLOR_SHIFT);
        colorPairs.put("green", LibBlockNames.GREEN_COLOR_SHIFT);
        colorPairs.put("light_blue", LibBlockNames.LIGHT_BLUE_COLOR_SHIFT);
        colorPairs.put("light_gray", LibBlockNames.LIGHT_GRAY_COLOR_SHIFT);
        colorPairs.put("lime", LibBlockNames.LIME_COLOR_SHIFT);
        colorPairs.put("magenta", LibBlockNames.MAGENTA_COLOR_SHIFT);
        colorPairs.put("orange", LibBlockNames.ORANGE_COLOR_SHIFT);
        colorPairs.put("pink", LibBlockNames.PINK_COLOR_SHIFT);
        colorPairs.put("purple", LibBlockNames.PURPLE_COLOR_SHIFT);
        colorPairs.put("red", LibBlockNames.RED_COLOR_SHIFT);
        colorPairs.put("white", LibBlockNames.WHITE_COLOR_SHIFT);
        colorPairs.put("yellow", LibBlockNames.YELLOW_COLOR_SHIFT);
    }
    
}
