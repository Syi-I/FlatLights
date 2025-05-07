package com.uberhelixx.flatlights.common.loot;

import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.item.curio.ModCurios;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class LootTableModifier {
    public static List<Supplier<ItemStack>> CURIO_LOOT = new ArrayList<>();
    
    public static Random rand = new Random();
    static {
        //puts all curios from the ModCurios registry into the CURIO_LOOT pool
        for(RegistryObject<Item> nextCurio : ModCurios.CURIOS.getEntries()) {
            CURIO_LOOT.add(() -> new ItemStack(nextCurio.get(), 1));
        }
    }
    
    /**
     * Gets a single random item from the passed in loot pool
     * @param lootPool The loot pool that the item is being picked from
     * @return The randomly picked {@link ItemStack}
     */
    private static ItemStack getRandomItem(List<Supplier<ItemStack>> lootPool){
        //Makes sure loot pool has items, then gets one item randomly from the pool and returns that item
        return lootPool.isEmpty() ? ItemStack.EMPTY : lootPool.get(rand.nextInt(lootPool.size())).get();
    }
    
    /**
     * Rolls the extra loot to be added to a loot table. Guarantees glowstone dust, but randomly rolls curios
     * @param modifier The loot modifier that is being used
     * @param rollBonus Any roll bonus modifier that can increase the chance value of the loot roll
     * @return The list of {@link ItemStack}s that are being added into a modified loot pool
     */
    public static List<ItemStack> getLootTableRoll(CurioStructureAdditionModifier modifier, float rollBonus){
        List<ItemStack> stacks = new ArrayList<>();
        
        //lower roll value means better loot, so subtract rollBonus
        float rollValue = Mth.clamp(rand.nextFloat() - rollBonus, 0, 1);
        //calculate number of curios to add based on rollValue
        int curioRolls = getCurioRolls(modifier, rollValue);
        
        //guaranteed 24-48 glowstone dust
        stacks.add(new ItemStack(Items.GLOWSTONE_DUST, 24 + rand.nextInt(16)));
        
        //chance to roll 1-4 motivational chairs
        if(rollValue < 0.4) {
            stacks.add(new ItemStack(ModBlocks.MOTIVATIONAL_CHAIR.get().asItem(), 1 + rand.nextInt(3)));
        }
        
        //roll a random curio a specified number of times
        for(int i = 0; i < curioRolls; i++) {
            stacks.add(new ItemStack(ModCurios.RANDOM_CURIO.get(), 1));
        }
        
        return stacks;
    }
    
    /**
     * Gets the amount of curio rolls based on the roll value
     * @param modifier Loot modifier that is being used
     * @param rollValue The roll value calculated from a random float + any bonuses from the modifier
     * @return The number of times that a random curio will be rolled and added to the loot pool
     */
    private static int getCurioRolls(CurioStructureAdditionModifier modifier, float rollValue) {
        int curioRolls = 0;
        //first roll chance
        if(rollValue < modifier.firstRoll) {
            curioRolls++;
        }
        //second roll chance
        if(rollValue < modifier.secondRoll) {
            curioRolls++;
        }
        //third roll chance
        if(rollValue < modifier.thirdRoll) {
            curioRolls++;
        }
        //if anyone adds more curio rolls, if we roll above this then add the amount of bonus rolls
        if(rollValue < modifier.bonusRollChance) {
            curioRolls += modifier.bonusRollCount;
        }
        return curioRolls;
    }
}
