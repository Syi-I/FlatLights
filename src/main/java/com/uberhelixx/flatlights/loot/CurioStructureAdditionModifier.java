package com.uberhelixx.flatlights.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;

public class CurioStructureAdditionModifier extends LootModifier {
    public double firstRoll;
    public double secondRoll;
    public double thirdRoll;
    public double bonusRollChance;
    public int bonusRollCount;
    //this is being used for structure chest loot not block drops
    //passes in conditions to check before modifying the loot that is generated, and what item(s) to be added
    protected CurioStructureAdditionModifier(ILootCondition[] conditionsIn, double firstRoll, double secondRoll, double thirdRoll, double bonusRollChance, int bonusRollCount) {
        super(conditionsIn);
        
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.thirdRoll = thirdRoll;
        
        this.bonusRollChance = bonusRollChance;
        this.bonusRollCount = bonusRollCount;
    }
    
    //default CurioStructureAdditionModifier if the json doesn't specify the roll value chances
    protected CurioStructureAdditionModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
        this.firstRoll = 0.85;
        this.secondRoll = 0.5;
        this.thirdRoll = 0.3;
        
        this.bonusRollChance = 0;
        this.bonusRollCount = 0;
    }
    
    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        List<ItemStack> additionalItems;
        //the loot table that we are trying to modify
        ResourceLocation queriedLootTable = context.getQueriedLootTableId();
        
        //get dimension ID of the chest and use it to boost odds in certain dimensions
        ResourceLocation dimID = context.getWorld().getDimensionKey().getLocation();
        
        //if it's a stronghold chest, increase odds by 10%
        if (queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CROSSING) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_LIBRARY)) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.1f);
        }
        //if it's a structure in the NETHER, increase odds by 15%
        else if (dimID.equals(DimensionType.THE_NETHER_ID)) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.15f);
        }
        //if it's a structure in the END, increase odds by 25%
        else if (dimID.equals(DimensionType.THE_END_ID)) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.25f);
        }
        //defaults to no roll bonus
        else {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.0f);
        }
       
        if(!(new HashSet<>(generatedLoot).containsAll(additionalItems))) {
            for(ItemStack item : generatedLoot) {
                //LOGGER.info("[Structure Chest] Base Generated Item: " + item.toString());
            }
            for(ItemStack item : additionalItems) {
                //LOGGER.info("[Structure Chest] Additional Item: " + item.toString());
            }
            generatedLoot.addAll(additionalItems);
            //LOGGER.info("[Structure Chest] Added extra items to loot table.");
            return generatedLoot;
        }
        //LOGGER.info("[Structure Chest] Unmodified loot table returned.");
        //return the modified list of loot
        //List<ItemStack> duplicateChest = new ArrayList<>();
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CurioStructureAdditionModifier> {

        @Override
        public CurioStructureAdditionModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            //return values from list of properties from the json file
            
            return new CurioStructureAdditionModifier(
                    conditionsIn,
                    object.get("first_roll").getAsDouble(),
                    object.get("second_roll").getAsDouble(),
                    object.get("third_roll").getAsDouble(),
                    object.get("bonus_roll_chance").getAsDouble(),
                    object.get("bonus_roll_count").getAsInt()
            );
        }

        @Override
        public JsonObject write(CurioStructureAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            
            json.addProperty("first_roll", instance.firstRoll);
            json.addProperty("second_roll", instance.secondRoll);
            json.addProperty("third_roll", instance.thirdRoll);
            json.addProperty("bonus_roll_chance", instance.bonusRollChance);
            json.addProperty("bonus_roll_count", instance.bonusRollCount);
            
            return json;
        }
    }
}
