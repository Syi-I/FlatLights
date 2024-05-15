package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.network.PacketCurioToggleMessage;
import com.uberhelixx.flatlights.network.PacketGenericPlayerNotification;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public static final float RARE_CHANCE = 40.0f;
    public static final float EPIC_CHANCE = 20.0f;
    public static final float LEGENDARY_CHANCE = 5.0f;
    public static final float GROWTH_CHANCE = 1.0f;

    /**
     * Rolls the curio's Tier
     * @param worldIn The world of the player, just used for RNG
     * @return The float value associated with the rolled Tier, for use in the curio's NBT
     */
    public static float rollCurioTier(World worldIn) {
        double nextRoll = worldIn.rand.nextFloat() * 100;
        //roll above 99 for growth
        if(nextRoll >= 100 - GROWTH_CHANCE) {
            return CurioTier.GROWTH.MODEL_VALUE;
        }
        //roll above 95 for legendary
        if(nextRoll >= 100 - LEGENDARY_CHANCE) {
            return CurioTier.LEGENDARY.MODEL_VALUE;
        }
        //roll above 80 for epic
        else if(nextRoll >= 100 - EPIC_CHANCE) {
            return CurioTier.EPIC.MODEL_VALUE;
        }
        //roll above 60 for rare
        else if(nextRoll >= 100 - RARE_CHANCE) {
            return CurioTier.RARE.MODEL_VALUE;
        }
        //defaults to common tier if not
        else {
            return CurioTier.COMMON.MODEL_VALUE;
        }
    }

    /**
     * Sets the NBT for a curio based on input set and rolled tier
     * @param playerIn The player who just tried to roll the curio
     * @param handIn The hand slot of the player, for getting the held curio
     * @param worldIn The world of the player
     * @param setIn The Set that the curio belongs to
     * @param tierIn Guarantees the curio's Tier, leave null for random curio tiers instead of specifying
     * @param growthCapIn If the curio is a Growth type, specifies the cap for how much a curio can grow (Defaults to BaseCurio.DEFAULT_GROWTH_CAP)
     */
    public static void setCurioNbt(PlayerEntity playerIn, Hand handIn, World worldIn, String setIn, @Nullable Float tierIn, @Nullable Integer growthCapIn) {
        //get held itemstack, which should be the input curio
        ItemStack stack = playerIn.getHeldItem(handIn);

        //grab current nbt tag or create a new one if null
        CompoundNBT newTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();

        //check if tags are present yet, which they shouldn't be if it's a newly picked up item
        //then roll tier and apply the appropriate curio set
        if(!rollCheck(newTag)) {
            //checks for forced tier, if no forced tier then roll curio tier now
            float curioTier = tierIn != null ? tierIn : rollCurioTier(worldIn);
            newTag.putFloat(TIER, curioTier);
            newTag.putString(SET, setIn);

            //growth type curios get a tracker so that any stat boosts can use the value
            //for growth type curios, put an upper limit to how much gain the curio can get
            if(curioTier == CurioTier.GROWTH.MODEL_VALUE) {
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
    public static boolean rollCheck(CompoundNBT itemTag) {
        //no item tag means no roll
        if(itemTag == null) {
            return false;
        }

        return (!itemTag.isEmpty() || itemTag.contains(TIER) || itemTag.contains(SET));
    }

    /**
     * Get curio tier data and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @return The formatted Tier level tooltip in {@link ITextComponent} form
     */
    public static ITextComponent getTierTooltip(ItemStack curio) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(TIER)) {
            //get curio tier
            CurioTier curioTier = getCurioTier(curio);
            String tierName;
            if(curioTier == CurioTier.COMMON) {
                tierName = TextFormatting.GRAY + "Common";
            }
            else if(curioTier == CurioTier.RARE) {
                tierName = TextFormatting.BLUE + "Rare";
            }
            else if(curioTier == CurioTier.EPIC) {
                tierName = TextFormatting.DARK_PURPLE + "Epic";
            }
            else if(curioTier == CurioTier.LEGENDARY) {
                tierName = TextFormatting.GOLD + "Legendary";
            }
            else if(curioTier == CurioTier.GROWTH) {
                tierName = TextFormatting.GREEN + "Growth";
            }
            else {
                tierName = "" + TextFormatting.BLACK + TextFormatting.OBFUSCATED + "Unknown";
            }
            data = TextHelpers.labelBrackets("Tier", null, tierName, null);
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a tier on this curio???");
            data = TextHelpers.labelBrackets("Tier", null, "Bugged Item", TextFormatting.RED);

        }
        return data;
    }

    /**
     * Get curio set name and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @return The formatted Set Name tooltip in {@link ITextComponent} form
     */
    public static ITextComponent getSetTooltip(ItemStack curio) {
        //reads nbt data and determines the set name tooltip
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(SET)) {
            //get translation text key from nbt tag
            String setTranslationText = tag.getString(SET);
            //get translation text string from key in nbt tag
            String setName = new TranslationTextComponent(setTranslationText).getString();
            data = TextHelpers.labelBrackets("Set", null, setName, TextFormatting.DARK_AQUA);
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a set on this curio???");
            data = TextHelpers.labelBrackets("Set", null, "Bugged Item", TextFormatting.RED);
        }
        return data;
    }

    /**
     * Formats a tooltip for Growth tier curios to show the progress of growth for that curio
     * @param curio The curio which has a Growth tier
     * @param showCap Whether to show the growth cap of this curio in the tooltip
     * @return The formatted tooltip for showing growth progress in {@link ITextComponent} form
     */
    public static ITextComponent getGrowthTooltip(ItemStack curio, boolean showCap) {
        //reads nbt data and determines the growth value tooltip based off the numbers
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(GROWTH_TRACKER)) {
            //get growth tracker and cap values
            int growthTracker = tag.getInt(GROWTH_TRACKER);
            int growthCap = tag.getInt(GROWTH_CAP);
            float growthPercent = growthTracker / growthCap;
            TextFormatting colorProgress = TextFormatting.RED;
            if(growthPercent > 0.33 && growthPercent <= 0.66) {
                colorProgress = TextFormatting.YELLOW;
            }
            else if(growthPercent > 0.66) {
                colorProgress = TextFormatting.GREEN;
            }
            String formattedTracker = colorProgress + String.valueOf(growthTracker) + TextFormatting.WHITE + "/" + TextFormatting.GREEN + growthCap;
            //hides growth cap in tooltip
            if(!showCap) {
                formattedTracker = colorProgress + String.valueOf(growthTracker);
            }
            data = TextHelpers.labelBrackets("Progress", null, formattedTracker, null);
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Why are we calling this for a non growth type curio???");
            data = TextHelpers.labelBrackets("Progress", null, "Bugged Item", TextFormatting.RED);
        }
        return data;
    }

    /**
     * Gets a formatted tooltip to display the {@code Set Effect} name and toggle state
     * @param curio The curio whose tooltip is being created
     * @return The formatted tooltip for the {@code Set Effect} as an {@link ITextComponent}
     */
    public static ITextComponent getSetEffectTooltip(ItemStack curio) {
        CompoundNBT tag = curio.getTag();
        String setEffect = "Nothing";
        TextFormatting color = TextFormatting.LIGHT_PURPLE;
        if(tag != null && !tag.isEmpty()) {
            //if set effect toggle-able curio, choose green(on) or red(off), otherwise leave aqua for things without the set effect
            if(tag.contains(SET_EFFECT_TOGGLE)) {
                //determines if effect is enabled or disabled, for choosing tooltip color
                if(tag.getBoolean(SET_EFFECT_TOGGLE) && canTriggerSetEffect(Minecraft.getInstance().player)) {
                    color = TextFormatting.GREEN;
                }
                else {
                    color = TextFormatting.RED;
                }
            }
            setEffect = new TranslationTextComponent(CurioSetNames.getEffect(CurioSetNames.getName(curio))).getString();
        }
        return TextHelpers.labelBrackets("Set Effect", null, setEffect, color);
    }

    /**
     * Gets the curio's {@code Set Effect Description} based off the input curio's {@code Set Name}
     * @param curio The curio whose description tooltip is being created
     * @return The formatted tooltip for the {@code Set Effect Description} as an {@link ITextComponent}
     */
    public static ITextComponent getSetDescriptionTooltip(ItemStack curio) {
        CompoundNBT tag = curio.getTag();
        String setEffect = "Nothing";
        TextFormatting color = TextFormatting.DARK_PURPLE;
        if(tag != null && !tag.isEmpty()) {
            setEffect = new TranslationTextComponent(CurioSetNames.getDescription(CurioSetNames.getName(curio))).getString();
        }
        return TextHelpers.labelBrackets("Effect Description", null, setEffect, color);
    }

    /**
     * Gets the Tier of a curio and returns the associated curio tier value
     * @param curio The curio which we are trying to get the Tier value of
     * @return The matching {@link CurioTier} associated with the float NBT tier data of the curio
     */
    public static CurioTier getCurioTier(ItemStack curio) {
        CompoundNBT tag = curio.getTag();
        //make sure the given curio has nbt data
        if(tag != null) {
            //check the nbt tag for rolled tier data and get the tier value from the tag
            if(tag.contains(TIER)) {
                float curioTier = tag.getFloat(TIER);

                //match tier from tag and return the associated tier enum
                if(curioTier == CurioTier.COMMON.MODEL_VALUE) {
                    return CurioTier.COMMON;
                }
                else if(curioTier == CurioTier.RARE.MODEL_VALUE) {
                    return CurioTier.RARE;
                }
                else if(curioTier == CurioTier.EPIC.MODEL_VALUE) {
                    return CurioTier.EPIC;
                }
                else if(curioTier == CurioTier.LEGENDARY.MODEL_VALUE) {
                    return CurioTier.LEGENDARY;
                }
                else if(curioTier == CurioTier.GROWTH.MODEL_VALUE) {
                    return CurioTier.GROWTH;
                }
            }
        }
        //if it matches none then the item is bugged, so return ERROR tier
        MiscHelpers.debugLogger("[Base Curio] No matching tier value.");
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
        if(curioTier == CurioTier.COMMON) {
            return CurioTier.COMMON.TIER_MULTIPLIER;
        }
        else if(curioTier == CurioTier.RARE) {
            return CurioTier.RARE.TIER_MULTIPLIER;
        }
        else if(curioTier == CurioTier.EPIC) {
            return CurioTier.EPIC.TIER_MULTIPLIER;
        }
        else if(curioTier == CurioTier.LEGENDARY) {
            return CurioTier.LEGENDARY.TIER_MULTIPLIER;
        }
        else if(curioTier == CurioTier.GROWTH) {
            return CurioTier.GROWTH.TIER_MULTIPLIER;
        }

        //if it matches none then the item is bugged, so return 0
        else {
            MiscHelpers.debugLogger("[Base Curio] No matching tier value. Returning multiplier of 0.");
            return 0.0f;
        }
    }

    /**
     * Gets the worn curios from only our specific slot types (cube/prism/sphere)
     * @param playerIn The player whose curios we are checking
     * @return A list of curios in {@link ItemStack} form, present in the given slot types
     */
    public static List<ItemStack> getWornCurios(PlayerEntity playerIn) {
        List<SlotResult> curioSlots = getWornCurioSlots(playerIn);

        //convert the slot results into actual ItemStack
        List<ItemStack> curioList = new ArrayList<>();
        for(int i = 0; i < curioSlots.size(); i++) {
            curioList.add(curioSlots.get(i).getStack());
        }

        return curioList;
    }

    /**
     * Gets a list of curios in the cube/prism/sphere slot types
     * @param playerIn The player whose curio slots are being checked
     * @return A list of {@link SlotResult} for the given player
     */
    public static List<SlotResult> getWornCurioSlots(PlayerEntity playerIn) {
        //get all the curios in slots of our mod's type (cube/prism/sphere slots only)
        List<SlotResult> cubeSlot = CuriosApi.getCuriosHelper().findCurios(playerIn, CUBE_SLOT_ID);
        List<SlotResult> prismSlot = CuriosApi.getCuriosHelper().findCurios(playerIn, PRISM_SLOT_ID);
        List<SlotResult> sphereSlot = CuriosApi.getCuriosHelper().findCurios(playerIn, SPHERE_SLOT_ID);

        //put all curios from each slot type into a list so that we can go through all curios and update the appropriate ones
        List<SlotResult> curioList = new ArrayList<>();
        for(int i = 0; i < cubeSlot.size(); i++) {
            curioList.add(cubeSlot.get(i));
        }
        for(int i = 0; i < prismSlot.size(); i++) {
            curioList.add(prismSlot.get(i));
        }
        for(int i = 0; i < sphereSlot.size(); i++) {
            curioList.add(sphereSlot.get(i));
        }

        return curioList;
    }

    /**
     * Determines whether the player is wearing three curios of the same set
     * @param playerIn The player whose curios are being checked
     * @return True if all three curios in our slot types are the same set name, false if not the same set name or not 3 curios
     */
    public static boolean canTriggerSetEffect(PlayerEntity playerIn) {
        //get curios in our specific slot types only
        List<SlotResult> curios = getWornCurioSlots(playerIn);
        String cubeSet = null;
        String prismSet = null;
        String sphereSet = null;
        //check the curio in each of the slots to get the set name from nbt if present
        for(SlotResult curio : curios) {
            //get slot id from curio to check if we are wearing one of each curio slot
            String slotID = curio.getSlotContext().getIdentifier();
            CompoundNBT tag = curio.getStack().getTag();
            if(slotID.equals(CUBE_SLOT_ID) && tag != null) {
                cubeSet = tag.getString(SET);
            }
            if(slotID.equals(PRISM_SLOT_ID) && tag != null) {
                prismSet = tag.getString(SET);
            }
            if(slotID.equals(SPHERE_SLOT_ID) && tag != null) {
                sphereSet = tag.getString(SET);
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
    public static String getSetEffect(PlayerEntity playerIn) {
        String setName = null;
        //this is only true if all three curios worn are the same set
        if(canTriggerSetEffect(playerIn)) {
            //get all curios and just get the set name from one of them since they should all share the same set
            List<ItemStack> curios = getWornCurios(playerIn);
            CompoundNBT firstCurioTag = curios.get(0).getTag();
            setName = firstCurioTag != null ? firstCurioTag.getString(SET) : null;
        }
        return setName;
    }

    /**
     * Applies all checks to make sure that the proper curio set is in place to be triggered
     * @param playerIn Player who is wearing the curios
     * @param curioSetIn The curio set that should be worn for the effect to trigger
     * @return True if the curio set is the one that should be triggered, false if the set effect is not the same
     */
    public static boolean correctSetEffect(PlayerEntity playerIn, String curioSetIn) {
        return getSetEffect(playerIn) != null && getSetEffect(playerIn).equals(curioSetIn) && canTriggerSetEffect(playerIn);
    }

    /**
     * Gets the value of the Growth Tracker for growth type curios
     * @param curio The curio whose growth tracker is being checked
     * @return The value of the growth tracker, or 0 if not a growth type curio
     */
    public static int getGrowthTracker(ItemStack curio) {
        CompoundNBT tag = curio.getTag();
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
        CompoundNBT tag = curio.getTag();
        if(tag != null && !tag.isEmpty()) {
            tag.putBoolean(SET_EFFECT_TOGGLE, false);
        }
    }

    /**
     * Toggles the set effect state of the input curio
     * @param curioIn The curio that is being toggled
     * @param playerIn The player wearing the curio who is trying to use the toggle
     */
    public static void toggleSetEffect(ItemStack curioIn, PlayerEntity playerIn) {
        //check if on server and if item is one of our curios and if set effect can be used at all
        if(!playerIn.getEntityWorld().isRemote() && curioIn.getItem() instanceof BaseCurio){
            if(canTriggerSetEffect(playerIn)) {
                //does this curio have the set effect toggle or not, check value if it does
                boolean active = curioIn.getOrCreateTag().contains(SET_EFFECT_TOGGLE) && curioIn.getOrCreateTag().getBoolean(SET_EFFECT_TOGGLE);
                //invert boolean state
                curioIn.getOrCreateTag().putBoolean(SET_EFFECT_TOGGLE, !active);
                PacketHandler.sendToPlayer((ServerPlayerEntity) playerIn, new PacketCurioToggleMessage(!active));
            }
            else {
                PacketHandler.sendToPlayer((ServerPlayerEntity) playerIn, new PacketGenericPlayerNotification("Cannot toggle curio set effect"));
            }
        }
    }
}
