package com.uberhelixx.flatlights.event.loot;

import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CurioStructureAdditionModifier extends LootModifier {
    //list of items that have a chance of being added to the loot pool
    private final List<Item> addition;
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
        this.addition = addition;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        
        //need to only modify chests and not other things like dispensers
        if(context.get(LootParameters.ORIGIN) != null) {
            //get BlockPos from context
            BlockPos chestPos = new BlockPos(context.get(LootParameters.ORIGIN));
            //get tile entity from chestPos
            TileEntity chest = context.getWorld().getTileEntity(chestPos);
            //the loot table that we are trying to modify
            ResourceLocation queriedLootTable = context.getQueriedLootTableId();
            /*
            check if this tile entity is an instance of a LockableLootTileEntity but not a dispenser or hopper
            don't want to mess with structures that use those for redstone machines/traps/puzzles
            
            also check if the queried loot table is from mineshafts since those use chest minecarts and those don't
            count as LockableLootTileEntity since minecarts are entities, but we still want to modify the loot
            
            if other structures use minecarts then whatever those just don't get loot I guess there isn't a good way to
            detect minecarts only with loot conditions unless I make a custom one or something
             */
            if((chest instanceof LockableLootTileEntity && !(chest instanceof DispenserTileEntity || chest instanceof HopperTileEntity)) || queriedLootTable.equals(LootTables.CHESTS_ABANDONED_MINESHAFT)) {
                //some free glowstone dust as a treat
                generatedLoot.add(new ItemStack(Items.GLOWSTONE_DUST.getItem(), context.getRandom().nextInt(16) + 24));
                
                //chance of finding a chair in loot, can get 1-4 of them
                if(context.getRandom().nextFloat() < 0.4) {
                    generatedLoot.add(new ItemStack(ModBlocks.MOTIVATIONAL_CHAIR.get().asItem(), context.getRandom().nextInt(3) + 1));
                }
                
                //roll a random value to determine how many curios are put into the chest
                float rolledChance = context.getRandom().nextFloat();
                ResourceLocation dimID = context.getWorld().getDimensionKey().getLocation();
                if(dimID.equals(DimensionType.THE_NETHER_ID)) {
                    System.out.println("this works for checking dimensions");
                }
                //if a stronghold chest, increase odds by 10%
                if(queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_CROSSING) || queriedLootTable.equals(LootTables.CHESTS_STRONGHOLD_LIBRARY)) {
                    rolledChance = MathHelper.clamp(rolledChance - 0.1f, 0, rolledChance);
                }
                //if some sort of bastion or a nether fortress bride, increase odds by 15%
                if(queriedLootTable.equals(LootTables.BASTION_BRIDGE) || queriedLootTable.equals(LootTables.BASTION_OTHER) || queriedLootTable.equals(LootTables.BASTION_TREASURE) || queriedLootTable.equals(LootTables.BASTION_HOGLIN_STABLE) || queriedLootTable.equals(LootTables.CHESTS_NETHER_BRIDGE)) {
                    rolledChance = MathHelper.clamp(rolledChance - 0.15f, 0, rolledChance);
                }
                //if end city, increase odds by 25%
                if(queriedLootTable.equals(LootTables.CHESTS_END_CITY_TREASURE)) {
                    rolledChance = MathHelper.clamp(rolledChance - 0.25f, 0, rolledChance);
                }
                
                //we do it like this since otherwise you would just generate multiple of the same curio by changing the count
                //also lets you tune how often you would find more curios
                //85% chance to get 1
                if(rolledChance < 0.85) {
                    //gets a random curio from the passed in list 'addition'
                    generatedLoot.add(new ItemStack(addition.get(context.getRandom().nextInt(curios.length)), 1));
                }
                //50% chance to get 2
                if(rolledChance < 0.5) {
                    generatedLoot.add(new ItemStack(addition.get(context.getRandom().nextInt(curios.length)), 1));
                }
                //30% chance to get 3
                if(rolledChance < 0.3) {
                    generatedLoot.add(new ItemStack(addition.get(context.getRandom().nextInt(curios.length)), 1));
                }
            }
        }
        //return the modified list of loot
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
                json.addProperty(curios[i], ForgeRegistries.ITEMS.getKey(instance.addition.get(i)).toString());
            }
            return json;
        }
    }
}
