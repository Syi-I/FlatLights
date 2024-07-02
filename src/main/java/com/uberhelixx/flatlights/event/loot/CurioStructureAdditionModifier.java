package com.uberhelixx.flatlights.event.loot;

import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.uberhelixx.flatlights.FlatLights.LOGGER;

public class CurioStructureAdditionModifier extends LootModifier {
    //list of items that have a chance of being added to the loot pool
    private final List<Item> itemsToAdd;
    //this string array must be in the exact same order as in 'resources > data > flatlights > loot_modifiers > curio_structure_loot.json'
    //otherwise items will be mismatched
    private final static String[] curios = {
            "dragon_cube", "dragon_prism", "dragon_sphere",
            "shore_cube", "shore_prism", "shore_sphere",
            "sun_cube", "sun_prism", "sun_sphere"
            };
    

    //this is being used for structure chest loot not block drops
    //passes in conditions to check before modifying the loot that is generated, and what item(s) to be added
    protected CurioStructureAdditionModifier(ILootCondition[] conditionsIn, List<Item> addition) {
        super(conditionsIn);
        this.itemsToAdd = addition;
    }
    
    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        List<ItemStack> additionalItems = new ArrayList<>();
        //the loot table that we are trying to modify
        ResourceLocation queriedLootTable = context.getQueriedLootTableId();
        //some free glowstone dust as a treat
        additionalItems.add(new ItemStack(Items.GLOWSTONE_DUST.getItem(), context.getRandom().nextInt(16) + 24));
        
        //chance of finding a chair in loot, can get 1-4 of them
        if (context.getRandom().nextFloat() < 0.4) {
            additionalItems.add(new ItemStack(ModBlocks.MOTIVATIONAL_CHAIR.get().asItem(), context.getRandom().nextInt(3) + 1));
        }
        
        //roll a random value to determine how many curios are put into the chest
        float rolledChance = context.getRandom().nextFloat();
        //get dimension ID of the chest and use it to boost odds in certain dimensions
        ResourceLocation dimID = context.getWorld().getDimensionKey().getLocation();
        
        //if it's a stronghold chest, increase odds by 10%
        if (queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CROSSING) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_LIBRARY)) {
            rolledChance = MathHelper.clamp(rolledChance - 0.1f, 0, rolledChance);
        }
        //if it's a structure in the NETHER, increase odds by 15%
        if (dimID.equals(DimensionType.THE_NETHER_ID)) {
            rolledChance = MathHelper.clamp(rolledChance - 0.15f, 0, rolledChance);
        }
        //if it's a structure in the END, increase odds by 25%
        if (dimID.equals(DimensionType.THE_END_ID)) {
            rolledChance = MathHelper.clamp(rolledChance - 0.25f, 0, rolledChance);
        }
        
        //we do it like this since otherwise you would just generate multiple of the same curio by changing the count
        //also lets you tune how often you would find more curios
        //85% chance to get 1
        if (rolledChance < 0.85) {
            //gets a random curio from the passed in list 'addition'
            additionalItems.add(new ItemStack(itemsToAdd.get(context.getRandom().nextInt(curios.length)), 1));
        }
        //50% chance to get 2
        if (rolledChance < 0.5) {
            additionalItems.add(new ItemStack(itemsToAdd.get(context.getRandom().nextInt(curios.length)), 1));
        }
        //30% chance to get 3
        if (rolledChance < 0.3) {
            additionalItems.add(new ItemStack(itemsToAdd.get(context.getRandom().nextInt(curios.length)), 1));
        }
       
        if(!(new HashSet<>(generatedLoot).containsAll(additionalItems))) {
            for(ItemStack item : generatedLoot) {
                LOGGER.info("[Structure Chest] Base Generated Item: " + item.toString());
            }
            for(ItemStack item : additionalItems) {
                LOGGER.info("[Structure Chest] Additional Item: " + item.toString());
            }
            generatedLoot.addAll(additionalItems);
            LOGGER.info("[Structure Chest] Added extra items to loot table.");
            return generatedLoot;
        }
        LOGGER.info("[Structure Chest] Unmodified loot table returned.");
        //return the modified list of loot
        //List<ItemStack> duplicateChest = new ArrayList<>();
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CurioStructureAdditionModifier> {

        @Override
        public CurioStructureAdditionModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            //list of items from the json file
            //gets each curio from the json file via the String array 'curios' which has the same keys
            List<Item> addition = new ArrayList<>();
            for(String curioName : curios) {
                addition.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, curioName))));
            }
            return new CurioStructureAdditionModifier(conditionsIn, addition);
        }

        @Override
        public JsonObject write(CurioStructureAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            //makes the list in the same order as the json, based on the 'curios' String array
            for(int i = 0; i < curios.length; i++) {
                json.addProperty(curios[i], ForgeRegistries.ITEMS.getKey(instance.itemsToAdd.get(i)).toString());
            }
            return json;
        }
    }
}
