package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * NOTE: for balancing purposes:tm: the curios are put into three rough categories, those being cubes, prisms, and spheres.
 * Should only ever have one curio slot of each type, prevents stacking from only using one type of curio to negate everything.
 * Cube: augments some attribute/stats of the player, such as armor/toughness/speed (likely flat stat increases and not %)
 * Prism: does something for attacking and weapons, such as increasing attack speed/damage/reach, or add some sort of attacking mechanic
 * Sphere: adds some sort of potion effect(s) or interacts with the environment, such as providing water breathing or negating suffocation damage
 * Set bonuses should be a strong buff for wearing a set, could be a basic large % stat increase or some effect that triggers upon meeting requirements.
 * Set bonus effect goes on the Cube curio only, since that will have the least moving parts as it's only stat increases
 */
public class BaseCurio extends Item implements ICurioItem {

    public BaseCurio(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isImmuneToFire() { return true; }

    //all the formatting for curio tooltips should be done here since it should be uniform for all curios we make
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //basic info tooltip
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            tooltip.add(getSetTooltip(stack));
            tooltip.add(getTierTooltip(stack));
            if(stack.getTag().contains(GROWTH_TRACKER)) {
                tooltip.add(getGrowthTooltip(stack, true) );
            }
        }
        //how to use curio when not rolled yet
        else {
            ITextComponent useTooltip = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.GRAY + "Right-click to roll." + TextFormatting.AQUA + "]");
            tooltip.add(useTooltip);
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /**
     * Curio Tier values with associated item model texture override float values and curio buff tier multipliers
     */
    public enum CurioTier {
        COMMON (0.1f, 1.0f),
        RARE (0.2f, 1.5f),
        EPIC (0.3f, 2.0f),
        LEGENDARY (0.4f, 2.5f),
        GROWTH (0.5f, 2.5f),
        ERROR(0.6f, 0.0f);

        public final float MODEL_VALUE;
        public final float TIER_MULTIPLIER;
        CurioTier(float modelOverride, float tierMultiplier) {
            this.MODEL_VALUE = modelOverride;
            this.TIER_MULTIPLIER = tierMultiplier;
        }
    }

    //curio tier nbt key, used to determine buff scaling
    public static final String TIER = "flatlights.curiotier";
    //curio set nbt key, used to determine what set the curio is a part of
    public static final String SET = "flatlights.curioset";
    //curio slot identifiers
    public static final String CUBE_SLOT_ID = "flatlights.curios.cube";
    public static final String PRISM_SLOT_ID = "flatlights.curios.prism";
    public static final String SPHERE_SLOT_ID = "flatlights.curios.sphere";
    //curio growth tier cap
    public static final String GROWTH_CAP = "flatlights.curioGrowthCap";
    //curio data tracker for the growth stat
    public static final String GROWTH_TRACKER = "flatlights.curioGrowthTracker";
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
    public float rollCurioTier(World worldIn) {
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
    public void setCurioNbt(PlayerEntity playerIn, Hand handIn, World worldIn, String setIn, @Nullable Float tierIn, @Nullable Integer growthCapIn) {
        //get held itemstack, which should be the input curio
        ItemStack stack = playerIn.getHeldItem(handIn);

        //grab current nbt tag or create a new one if null
        CompoundNBT newTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();

        //check if tags are present yet, which they shouldn't be if it's a newly picked up item
        //then roll tier and apply the appropriate curio set
        if(!rollCheck(newTag)) {
            //checks for forced tier, if no forced tier then roll curio tier now
            float curioTier = tierIn != null ? tierIn : rollCurioTier(worldIn);
            newTag.putFloat(BaseCurio.TIER, curioTier);
            newTag.putString(BaseCurio.SET, setIn);

            //growth type curios get a tracker so that any stat boosts can use the value
            //for growth type curios, put an upper limit to how much gain the curio can get
            if(curioTier == CurioTier.GROWTH.MODEL_VALUE) {
                //check if a specific growth cap has been passed in, otherwise use default value
                int growthCap = growthCapIn != null ?  growthCapIn : DEFAULT_GROWTH_CAP;
                newTag.putInt(BaseCurio.GROWTH_TRACKER, 0);
                newTag.putInt(BaseCurio.GROWTH_CAP, growthCap);
            }

            stack.setTag(newTag);
        }
    }

    /**
     * Checks the input tag to see if the curio was already rolled or not
     * @param itemTag The NBT data from the curio
     * @return True if data is present indicating that the curio has been rolled, false if no roll data yet
     */
    public boolean rollCheck(CompoundNBT itemTag) {
        //no item tag means no roll
        if(itemTag == null) {
            return false;
        }

        return (!itemTag.isEmpty() || itemTag.contains(BaseCurio.TIER) || itemTag.contains(BaseCurio.SET));
    }

    /**
     * Get curio tier data and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @return The formatted Tier level tooltip in {@link ITextComponent} form
     */
    public ITextComponent getTierTooltip(ItemStack curio) {
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
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Tier: " + tierName + TextFormatting.AQUA + "]");
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a tier on this curio???");
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Tier: " + TextFormatting.RED + "Bugged Item" + TextFormatting.AQUA + "]");
        }
        return data;
    }

    /**
     * Get curio set name and format for tooltip
     * @param curio The curio which is getting a tooltip
     * @return The formatted Set Name tooltip in {@link ITextComponent} form
     */
    public ITextComponent getSetTooltip(ItemStack curio) {
        //reads nbt data and determines the set name tooltip
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(SET)) {
            //get translation text key from nbt tag
            String setTranslationText = tag.getString(SET);
            //get translation text string from key in nbt tag
            String setName = new TranslationTextComponent(setTranslationText).getString();
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Set: " + TextFormatting.DARK_AQUA + setName + TextFormatting.AQUA + "]");
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a set on this curio???");
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Set: " + TextFormatting.RED + "Bugged Item" + TextFormatting.AQUA + "]");
        }
        return data;
    }

    /**
     * Formats a tooltip for Growth tier curios to show the progress of growth for that curio
     * @param curio The curio which has a Growth tier
     * @param showCap Whether to show the growth cap of this curio in the tooltip
     * @return The formatted tooltip for showing growth progress in {@link ITextComponent} form
     */
    public ITextComponent getGrowthTooltip(ItemStack curio, boolean showCap) {
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
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Progress: " + formattedTracker + TextFormatting.AQUA + "]");
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Why are we calling this for a non growth type curio???");
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Progress: " + TextFormatting.RED + "Bugged Item" + TextFormatting.AQUA + "]");
        }
        return data;
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
    public float getTierMultiplier(ItemStack curio) {
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
}
