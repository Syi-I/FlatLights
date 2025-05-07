package com.uberhelixx.flatlights.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.uberhelixx.flatlights.FlatLights;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class CurioStructureAdditionModifier extends LootModifier {
    
    public double firstRoll;
    public double secondRoll;
    public double thirdRoll;
    public double bonusRollChance;
    public int bonusRollCount;
    //this is being used for structure chest loot not block drops
    //passes in conditions to check before modifying the loot that is generated, and what item(s) to be added
    public CurioStructureAdditionModifier(LootItemCondition[] conditionsIn, double firstRoll, double secondRoll, double thirdRoll, double bonusRollChance, int bonusRollCount) {
        super(conditionsIn);
        
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.thirdRoll = thirdRoll;
        
        this.bonusRollChance = bonusRollChance;
        this.bonusRollCount = bonusRollCount;
    }
    
    //default CurioStructureAdditionModifier if the json doesn't specify the roll value chances
    protected CurioStructureAdditionModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
        this.firstRoll = 0.85;
        this.secondRoll = 0.5;
        this.thirdRoll = 0.3;
        
        this.bonusRollChance = 0;
        this.bonusRollCount = 0;
    }
    
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        List<ItemStack> additionalItems;
        //the loot table that we are trying to modify
        ResourceLocation queriedLootTable = lootContext.getQueriedLootTableId();
        
        //get dimension ID of the chest and use it to boost odds in certain dimensions
        ResourceLocation dimID = lootContext.getLevel().dimension().location();
        
        //if it's a stronghold chest, increase odds by 10%
        if (queriedLootTable.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR) || queriedLootTable.equals(BuiltInLootTables.STRONGHOLD_CROSSING) || queriedLootTable.equals(BuiltInLootTables.STRONGHOLD_LIBRARY)) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.1f);
        }
        //if it's a structure in the NETHER, increase odds by 15%
        else if (dimID.equals(Level.NETHER.location())) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.15f);
        }
        //if it's a structure in the END, increase odds by 25%
        else if (dimID.equals(Level.END.location())) {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.25f);
        }
        //defaults to no roll bonus
        else {
            additionalItems = LootTableModifier.getLootTableRoll(this, 0.0f);
        }
        
        if(!(new HashSet<>(objectArrayList).containsAll(additionalItems))) {
            for(ItemStack item : objectArrayList) {
                //FlatLights.LOGGER.info("[Structure Chest] Base Generated Item: " + item.toString());
            }
            for(ItemStack item : additionalItems) {
                //FlatLights.LOGGER.info("[Structure Chest] Additional Item: " + item.toString());
            }
            objectArrayList.addAll(additionalItems);
            //FlatLights.LOGGER.info("[Structure Chest] Added extra items to loot table.");
            return objectArrayList;
        }
        //FlatLights.LOGGER.info("[Structure Chest] Unmodified loot table returned.");
        //return the modified list of loot
        //List<ItemStack> duplicateChest = new ArrayList<>();
        return objectArrayList;
    }
    
    public static final Supplier<Codec<CurioStructureAdditionModifier>> CODEC = () -> RecordCodecBuilder.create(instance -> instance.group(
            LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions),
            Codec.DOUBLE.fieldOf("first_roll").forGetter(d -> d.firstRoll),
            Codec.DOUBLE.fieldOf("second_roll").forGetter(d -> d.secondRoll),
            Codec.DOUBLE.fieldOf("third_roll").forGetter(d -> d.thirdRoll),
            Codec.DOUBLE.fieldOf("bonus_roll_chance").forGetter(d -> d.bonusRollChance),
            Codec.INT.fieldOf("bonus_roll_count").forGetter(d -> d.bonusRollCount)
    ).apply(instance, CurioStructureAdditionModifier::new));
    
    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
