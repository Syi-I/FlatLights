package com.uberhelixx.flatlights.common.item.curio;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.util.ClientUtils;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurioUtils {
    //curio tier nbt key, used to determine buff scaling
    public static final String TIER = "flatlights.curiotier";
    //curio set nbt key, used to determine what set the curio is a part of
    public static final String SET = "flatlights.curioset";
    //curio slot identifiers
    public static final String CUBE_SLOT_ID = "flatlights.curios.cube";
    public static final String PRISM_SLOT_ID = "flatlights.curios.prism";
    public static final String SPHERE_SLOT_ID = "flatlights.curios.sphere";
    //curio growth tier cap nbt key
    public static final String GROWTH_CAP = "flatlights.curioGrowthCap";
    //curio data tracker nbt key for the growth stat
    public static final String GROWTH_TRACKER = "flatlights.curioGrowthTracker";
    //curio set effect toggle nbt key
    public static final String SET_EFFECT_TOGGLE = "flatlights.curioSetToggle";
    //default cap for growth tracker if a value doesn't get specifically passed in
    public static final int DEFAULT_GROWTH_CAP = 1000;
    
    //percent chances to roll each tier
    public static final float COMMON_CHANCE = 45.0f;
    public static final float RARE_CHANCE = 30.0f;
    public static final float EPIC_CHANCE = 15.0f;
    public static final float LEGENDARY_CHANCE = 9.0f;
    public static final float GROWTH_CHANCE = 1.0f;
    
    /**
     * Rolls the curio's Tier
     * @param playerIn The player rolling the curio, just used for RNG
     * @return The float value associated with the rolled Tier, for use in the curio's NBT
     */
    public static float rollCurioTier(Player playerIn) {
        float nextRoll = playerIn.level().getRandom().nextFloat() * 100;
        if(MiscUtils.uuidCheck(playerIn.getUUID())) {
            nextRoll = Mth.clamp(nextRoll + 20, 0, nextRoll);
        }
        //0-45 common chance roll
        if(nextRoll < COMMON_CHANCE) {
            return CurioTier.getModel(CurioTier.COMMON);
        }
        //45-75 rare chance roll
        else if(nextRoll < COMMON_CHANCE + RARE_CHANCE) {
            return CurioTier.getModel(CurioTier.RARE);
        }
        //75-90 epic chance roll
        else if(nextRoll < COMMON_CHANCE + RARE_CHANCE + EPIC_CHANCE) {
            return CurioTier.getModel(CurioTier.EPIC);
        }
        //90-99 legendary chance roll
        else if(nextRoll < COMMON_CHANCE + RARE_CHANCE + EPIC_CHANCE + LEGENDARY_CHANCE) {
            return CurioTier.getModel(CurioTier.LEGENDARY);
        }
        //99-100 growth chance roll
        else {
            return CurioTier.getModel(CurioTier.GROWTH);
        }
    }
    
    /**
     * Sets the NBT for a curio based on input set and rolled tier
     * @param playerIn The player who just tried to roll the curio
     * @param handIn The hand slot of the player, for getting the held curio
     * @param setIn The Set that the curio belongs to
     * @param tierIn Guarantees the curio's Tier, leave null for random curio tiers instead of specifying
     * @param growthCapIn If the curio is a Growth type, specifies the cap for how much a curio can grow (Defaults to BaseCurio.DEFAULT_GROWTH_CAP)
     */
    public static void setCurioNbt(Player playerIn, InteractionHand handIn, String setIn, @Nullable Float tierIn, @Nullable Integer growthCapIn) {
        //get held itemstack, which should be the input curio
        ItemStack stack = playerIn.getItemInHand(handIn);
        
        //grab current nbt tag or create a new one if null
        CompoundTag newTag = stack.getOrCreateTag();
        
        //check if tags are present yet, which they shouldn't be if it's a newly picked up item
        //then roll tier and apply the appropriate curio set
        if(!rollCheck(newTag)) {
            //checks for forced tier, if no forced tier then roll curio tier now
            float curioTier = tierIn != null ? tierIn : rollCurioTier(playerIn);
            newTag.putFloat(TIER, curioTier);
            newTag.putString(SET, setIn);
            
            //growth type curios get a tracker so that any stat boosts can use the value
            //for growth type curios, put an upper limit to how much gain the curio can get
            if(curioTier == CurioTier.getModel(CurioTier.GROWTH)) {
                //check if a specific growth cap has been passed in, otherwise use default value
                int growthCap = growthCapIn != null ?  growthCapIn : DEFAULT_GROWTH_CAP;
                newTag.putInt(GROWTH_TRACKER, 0);
                newTag.putInt(GROWTH_CAP, growthCap);
            }
            
            stack.setTag(newTag);
        }
    }
    
    /**
     * Checks the input tag to see if the curio was already rolled or not
     * @param itemTag The NBT data from the curio
     * @return True if data is present indicating that the curio has been rolled, false if no roll data yet
     */
    public static boolean rollCheck(CompoundTag itemTag) {
        //no item tag means no roll
        if(itemTag == null) {
            return false;
        }
        
        return (!itemTag.isEmpty() || itemTag.contains(TIER) || itemTag.contains(SET));
    }
    
    /**
     * Get curio tier data and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @param tooltipIn The tooltip of the curio
     */
    public static void getTierTooltip(ItemStack curio, List<Component> tooltipIn) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundTag tag = curio.getTag();
        if (tag != null && tag.contains(TIER)) {
            //get curio tier information for formatting
            CurioTier curioTier = getCurioTier(curio);
            String tierName = CurioTier.getName(curioTier);
            Style tierColor = CurioTier.getStyle(curioTier);
            
            TooltipHelper.labelBrackets(tooltipIn, "Tier", null, tierName, tierColor);
        }
        else {
            MiscUtils.infoLog("[Base Curio] Somehow we failed to put a tier on this curio???");
            TooltipHelper.labelBrackets(tooltipIn, "Tier", null, "Bugged Item", Style.EMPTY.withColor(ChatFormatting.RED));
        }
    }
    
    /**
     * Get curio set name and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @param tooltipIn The tooltip of the curio
     */
    public static void getSetTooltip(ItemStack curio, List<Component> tooltipIn) {
        //reads nbt data and determines the set name tooltip
        CompoundTag tag = curio.getTag();
        if (curio.getTag() != null && curio.getTag().contains(SET)) {
            //get translation text key from nbt tag
            String setTranslationText = tag.getString(SET);
            //get translation text string from key in nbt tag
            String setName = Component.translatable(setTranslationText).getString();
            TooltipHelper.labelBrackets(tooltipIn, "Set", null, setName, Style.EMPTY.withColor(ChatFormatting.DARK_AQUA));
        }
        else {
            MiscUtils.infoLog("[Base Curio] Somehow we failed to put a set on this curio???");
            TooltipHelper.labelBrackets(tooltipIn, "Tier", null, "Bugged Item", Style.EMPTY.withColor(ChatFormatting.RED));
        }
    }
    
    /**
     * Formats a tooltip for Growth tier curios to show the progress of growth for that curio
     * @param curio The curio which has a Growth tier
     * @param showCap Whether to show the growth cap of this curio in the tooltip
     * @param tooltipIn The tooltip of the curio
     */
    public static void getGrowthTooltip(ItemStack curio, boolean showCap, List<Component> tooltipIn) {
        //reads nbt data and determines the growth value tooltip based off the numbers
        CompoundTag tag = curio.getTag();
        if (curio.getTag() != null && curio.getTag().contains(GROWTH_TRACKER)) {
            //get growth tracker and cap values
            int growthTracker = tag.getInt(GROWTH_TRACKER);
            int growthCap = tag.getInt(GROWTH_CAP);
            float growthPercent = (float) growthTracker / growthCap;
            Style colorProgress = Style.EMPTY.withColor(ChatFormatting.RED);
            if(growthPercent > 0.33 && growthPercent <= 0.66) {
                colorProgress = Style.EMPTY.withColor(ChatFormatting.YELLOW);
            }
            else if(growthPercent > 0.66) {
                colorProgress = Style.EMPTY.withColor(ChatFormatting.GREEN);
            }
            Component formattedTracker = Component.literal(String.valueOf(growthTracker)).withStyle(colorProgress)
                    .append(Component.literal("/").withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(String.valueOf(growthCap)).withStyle(ChatFormatting.GREEN));
            //hides growth cap in tooltip
            if(!showCap) {
                formattedTracker = Component.literal(String.valueOf(growthTracker)).withStyle(colorProgress);
            }
            TooltipHelper.labelBrackets(tooltipIn, "Progress", null, formattedTracker);
        }
        else {
            MiscUtils.infoLog("[Base Curio] Why are we calling this for a non growth type curio???");
            TooltipHelper.labelBrackets(tooltipIn, "Progress", null, "Bugged Item", Style.EMPTY.withColor(ChatFormatting.RED));
        }
    }
    
    /**
     * Gets a formatted tooltip to display the {@code Set Effect} name and toggle state
     * @param curio The curio whose tooltip is being created
     * @param tooltipIn The tooltip of the curio
     */
    public static void getSetEffectTooltip(ItemStack curio, List<Component> tooltipIn) {
        CompoundTag tag = curio.getTag();
        String setEffect = "Nothing";
        Style color = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);
        if(tag != null && !tag.isEmpty()) {
            //if set effect toggle-able curio, choose green(on) or red(off), otherwise leave aqua for things without the set effect
            if(tag.contains(SET_EFFECT_TOGGLE)) {
                //determines if effect is enabled or disabled, for choosing tooltip color
                if(tag.getBoolean(SET_EFFECT_TOGGLE) && canTriggerSetEffect(ClientUtils.getPlayer())) {
                    color = Style.EMPTY.withColor(ChatFormatting.GREEN);
                }
                else {
                    color = Style.EMPTY.withColor(ChatFormatting.RED);
                }
            }
            //gets the translation key string for the set effect
            setEffect = Component.translatable(CurioSetNames.getEffect(CurioSetNames.getName(curio))).getString();
        }
        TooltipHelper.labelBrackets(tooltipIn, "Set Effect", null, setEffect, color);
    }
    
    /**
     * Gets the curio's {@code Set Effect Description} based off the input curio's {@code Set Name}
     * @param curio The curio whose description tooltip is being created
     * @param tooltipIn The tooltip of the curio
     */
    public static void getSetDescriptionTooltip(ItemStack curio, List<Component> tooltipIn) {
        CompoundTag tag = curio.getTag();
        String setEffect = "Nothing";
        Style color = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE);
        if(tag != null && !tag.isEmpty()) {
            setEffect = Component.translatable(CurioSetNames.getDescription(CurioSetNames.getName(curio))).getString();
        }
        TooltipHelper.labelBrackets(tooltipIn, "Effect Description", null, setEffect, color);
    }
    
    /**
     * Gets the Tier of a curio and returns the associated curio tier value
     * @param curio The curio which we are trying to get the Tier value of
     * @return The matching {@link CurioTier} associated with the integer NBT tier data of the curio
     */
    public static CurioTier getCurioTier(ItemStack curio) {
        CompoundTag tag = curio.getTag();
        //make sure the given curio has nbt data
        if(tag != null) {
            //check the nbt tag for rolled tier data and get the tier value from the tag
            if(tag.contains(TIER)) {
                float curioTier = tag.getFloat(TIER);
                
                //match tier from tag and return the associated tier enum
                if(curioTier == CurioTier.getModel(CurioTier.COMMON)) {
                    return CurioTier.COMMON;
                }
                else if(curioTier == CurioTier.getModel(CurioTier.RARE)) {
                    return CurioTier.RARE;
                }
                else if(curioTier == CurioTier.getModel(CurioTier.EPIC)) {
                    return CurioTier.EPIC;
                }
                else if(curioTier == CurioTier.getModel(CurioTier.LEGENDARY)) {
                    return CurioTier.LEGENDARY;
                }
                else if(curioTier == CurioTier.getModel(CurioTier.GROWTH)) {
                    return CurioTier.GROWTH;
                }
            }
        }
        //if it matches none then the item is bugged, so return ERROR tier
        MiscUtils.infoLog("[Base Curio] No matching tier value.");
        return CurioTier.ERROR;
    }
    
    /**
     * Gets the multiplier amount for a curio, typically used for increasing the effect amount/duration of a curio's ability between Tiers
     * @param curio The curio which we are trying to get the multiplier for
     * @return The float value of the multiplier which matches the Tier of the curio
     */
    public static float getTierMultiplier(ItemStack curio) {
        //get curio tier and then return appropriate multiplier value
        CurioTier curioTier = getCurioTier(curio);
        return CurioTier.getMultiplier(curioTier);
    }
    
    /**
     * Gets the worn curios from only our specific slot types (cube/prism/sphere)
     * @param playerIn The player whose curios we are checking
     * @return A list of curios in {@link ItemStack} form, present in the given slot types, returns null if no curio slots
     */
    //TODO: test and fix the getWornCurioSlots related methods using the new Map<String, ICurioStacksHandler>
    public static List<ItemStack> getWornCurios(Player playerIn) {
        Map<String, ICurioStacksHandler> curioSlots = getWornCurioSlots(playerIn);
        if(curioSlots.isEmpty()) {
            return null;
        }
        List<ItemStack> curioList = new ArrayList<>();
        for(int slotIndex = 0; slotIndex < curioSlots.get(CUBE_SLOT_ID).getSlots(); slotIndex++) {
            curioList.add(curioSlots.get(CUBE_SLOT_ID).getStacks().getStackInSlot(slotIndex));
        }
        for(int slotIndex = 0; slotIndex < curioSlots.get(PRISM_SLOT_ID).getSlots(); slotIndex++) {
            curioList.add(curioSlots.get(PRISM_SLOT_ID).getStacks().getStackInSlot(slotIndex));
        }
        for(int slotIndex = 0; slotIndex < curioSlots.get(SPHERE_SLOT_ID).getSlots(); slotIndex++) {
            curioList.add(curioSlots.get(SPHERE_SLOT_ID).getStacks().getStackInSlot(slotIndex));
        }
        return curioList;
    }
    
    /**
     * Gets a list of curios in the cube/prism/sphere slot types
     * @param playerIn The player whose curio slots are being checked
     * @return A Map<String, ICurioStacksHandler> for the given player
     */
    //TODO: test and fix the getWornCurioSlots related methods using the new Map<String, ICurioStacksHandler>
    public static Map<String, ICurioStacksHandler> getWornCurioSlots(Player playerIn) {
        Map<String, ICurioStacksHandler> curioInv = new HashMap<>();
        //gets the curios inventory
        try {
            CuriosApi.getCuriosInventory(playerIn).ifPresent(curiosInventory -> {
                //string is slot type identifier i.e. "ring", ICurioStacksHandler is slot inventory (but not item directly)
                Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();
                curioInv.put(CUBE_SLOT_ID, curios.get(CUBE_SLOT_ID));
                curioInv.put(PRISM_SLOT_ID, curios.get(PRISM_SLOT_ID));
                curioInv.put(SPHERE_SLOT_ID, curios.get(SPHERE_SLOT_ID));
                //gets slot inventory of a specified slot type
                //curiosInventory.getStacksHandler(CUBE_SLOT_ID).ifPresent(slotInventory -> {});
            });
        } catch (Exception e) {
            FlatLights.LOGGER.error("[CurioUtils#getWornCurioSlots] Could not get curio inventory from player. Returning empty map.");
        }
        return curioInv;
    }
    
    /**
     * Determines whether the player is wearing three curios of the same set
     * @param playerIn The player whose curios are being checked
     * @return True if all three curios in our slot types are the same set name, false if not the same set name or not 3 curios
     */
    //TODO: test and fix the getWornCurioSlots related methods using the new Map<String, ICurioStacksHandler>
    public static boolean canTriggerSetEffect(Player playerIn) {
        String cubeSet = null;
        String prismSet = null;
        String sphereSet = null;
        
        //get curios in our specific slot types only
        Map<String, ICurioStacksHandler> curios = getWornCurioSlots(playerIn);
        if(curios.isEmpty()) {
            return false;
        }
        //should only ever have 1 of each slot type, if you cheat and have more then no set effects work
        if(curios.get(CUBE_SLOT_ID).getSlots() == 1 && curios.get(PRISM_SLOT_ID).getSlots() == 1 && curios.get(SPHERE_SLOT_ID).getSlots() == 1) {
            //get curio ItemStack in the first (and only) slot of the player
            ItemStack cube = curios.get(CUBE_SLOT_ID).getStacks().getStackInSlot(0);
            //grab set String NBT data for each curio
            if(cube.getTag() != null && cube.getTag().contains(SET)) {
                cubeSet = cube.getTag().getString(SET);
            }
            ItemStack prism = curios.get(PRISM_SLOT_ID).getStacks().getStackInSlot(0);
            if(prism.getTag() != null && prism.getTag().contains(SET)) {
                prismSet = prism.getTag().getString(SET);
            }
            ItemStack sphere = curios.get(SPHERE_SLOT_ID).getStacks().getStackInSlot(0);
            if(sphere.getTag() != null && sphere.getTag().contains(SET)) {
                sphereSet = sphere.getTag().getString(SET);
            }
        }
    
        //make sure these have actual sets and aren't unrolled or something that would for some reason not have a set name
        if(cubeSet == null || prismSet == null || sphereSet == null) {
            return false;
        }
        //makes sure that all three curios from the different slot types are of the same set name
        return cubeSet.equals(prismSet) && cubeSet.equals(sphereSet);
    }
    
    /**
     * Gets the set name for the curios from this player, if all three curios are of the same set
     * @param playerIn The player whose curios are being checked
     * @return The curio Set Name if all three curios are of the same set, or null otherwise
     */
    public static String getSetEffect(Player playerIn) {
        String setName = null;
        //this is only true if all three curios worn are the same set
        if(canTriggerSetEffect(playerIn)) {
            //get all curios and just get the set name from one of them since they should all share the same set
            List<ItemStack> curios = getWornCurios(playerIn);
            assert curios != null;
            CompoundTag firstCurioTag = curios.get(0).getTag();
            setName = firstCurioTag != null ? firstCurioTag.getString(SET) : null;
        }
        return setName;
    }
    
    /**
     * Applies all checks to make sure that the proper curio set is in place and can be triggered
     * @param playerIn Player who is wearing the curios
     * @param curioSetIn The curio set that should be worn for the effect to trigger
     * @return True if the curio set is the one that should be triggered, false if the set effect is not the same
     */
    public static boolean correctSetEffect(Player playerIn, String curioSetIn) {
        return getSetEffect(playerIn) != null && getSetEffect(playerIn).equals(curioSetIn) && canTriggerSetEffect(playerIn);
    }
    
    /**
     * Gets the value of the Growth Tracker for growth type curios
     * @param curio The curio whose growth tracker is being checked
     * @return The value of the growth tracker, or 0 if not a growth type curio
     */
    public static int getGrowthTracker(ItemStack curio) {
        CompoundTag tag = curio.getTag();
        if(tag == null || tag.isEmpty()) {
            return 0;
        }
        else {
            if(getCurioTier(curio) == CurioTier.GROWTH && tag.contains(GROWTH_TRACKER) && tag.contains(GROWTH_CAP)) {
                return tag.getInt(GROWTH_TRACKER);
            }
        }
        return 0;
    }
    
    /**
     * Adds NBT data for the set effect toggle keybind
     * @param curio The curio that we're putting the NBT data on
     */
    public static void addSetToggle(ItemStack curio) {
        CompoundTag tag = curio.getTag();
        if(tag != null && !tag.isEmpty()) {
            tag.putBoolean(SET_EFFECT_TOGGLE, false);
        }
    }
    
    /**
     * Toggles the set effect state of the input curio
     * @param curioIn The curio that is being toggled
     * @param playerIn The player wearing the curio who is trying to use the toggle
     */
    public static void toggleSetEffect(ItemStack curioIn, Player playerIn) {
        //check if on server and if item is one of our curios and if set effect can be used at all
        if(!playerIn.level().isClientSide() && curioIn.getItem() instanceof BaseCurio){
            if(canTriggerSetEffect(playerIn)) {
                //does this curio have the set effect toggle or not, check value if it does
                boolean active = curioIn.getOrCreateTag().contains(SET_EFFECT_TOGGLE) && curioIn.getOrCreateTag().getBoolean(SET_EFFECT_TOGGLE);
                //invert boolean state
                curioIn.getOrCreateTag().putBoolean(SET_EFFECT_TOGGLE, !active);
                Component toggleMsg = Component.literal("Toggled curio effect ").append(Component.literal("on.").withStyle(ChatFormatting.GREEN));
                MiscUtils.modeSwitchSound(playerIn, !active);
                if(active) {
                    toggleMsg = Component.literal("Toggled curio effect ").append(Component.literal("off.").withStyle(ChatFormatting.RED));
                }
                playerIn.displayClientMessage(toggleMsg, true);
            }
            else {
                Component toggleMsg = Component.literal("Cannot toggle curio set effect.");
                playerIn.displayClientMessage(toggleMsg, true);
            }
        }
    }
    
    /**
     * Gets the curio from the specific slot ID
     * @param playerIn The player whose curios are being searched
     * @param slotIn The slot ID for the curio we are searching for (CUBE_SLOT_ID/PRISM_SLOT_ID/SPHERE_SLOT_ID)
     * @return The curio from the specific slot, or {@code null} if no item in the specified slot
     */
    //TODO: test and fix the getWornCurioSlots related methods using the new Map<String, ICurioStacksHandler>
    public static ItemStack getCurioFromSlot(Player playerIn, String slotIn) {
        //get the specific slot curio from the player
        Map<String, ICurioStacksHandler> curioSlots = CurioUtils.getWornCurioSlots(playerIn);
        if(curioSlots.isEmpty()) {
            return null;
        }
        //MiscUtils.infoLog("[CurioUtils#getWornCurioSlots] Map from curioSlots = " + curioSlots);
        //MiscUtils.infoLog("[CurioUtils#getWornCurioSlots] playerIn = " + playerIn);
        //MiscUtils.infoLog("[CurioUtils#getWornCurioSlots] slotIn = " + slotIn);
        ItemStack curio = null;
        for(int slotIndex = 0; slotIndex < curioSlots.get(slotIn).getSlots(); slotIndex++) {
            curio = curioSlots.get(slotIn).getStacks().getStackInSlot(slotIndex);
        }
        return curio;
    }
    
    /**
     * Gets the player from the SlotContext, if any
     * @param context The {@link SlotContext}
     * @return The {@link Player} entity from the context, or NULL if not a player
     */
    public static Player getPlayer(SlotContext context) {
        return context.entity() instanceof Player ? (Player) context.entity() : null;
    }
}
