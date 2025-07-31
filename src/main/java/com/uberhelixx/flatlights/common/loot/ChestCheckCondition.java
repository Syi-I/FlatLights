package com.uberhelixx.flatlights.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class ChestCheckCondition implements LootItemCondition {
    
    public static final LootItemConditionType GENERIC_STRUCTURE_CHEST = new LootItemConditionType(new ChestCheckCondition.Serializer());
    private final LocationPredicate predicate;
    private final BlockPos offset;
    
    /**
     * Functionally similar to the vanilla {@link LocationCheck} condition in that it takes the same arguments.
     * This simply adds another layer to the check which confirms if the location is a chest or not, to function as a generic chest filling condition
     * instead of needing to add individually to every structure's loot table, while also allowing for spawning in other mods' structures if they use
     * chests as loot storage.
     */
    private ChestCheckCondition(LocationPredicate locationPredicate, BlockPos blockPos) {
        this.predicate = locationPredicate;
        this.offset = blockPos;
    }
    
    @Override
    public LootItemConditionType getType() {
        return GENERIC_STRUCTURE_CHEST;
    }
    
    public boolean test(LootContext pContext) {
        Vec3 vector3d = pContext.getParamOrNull(LootContextParams.ORIGIN);
        if (vector3d == null) {
            return false;
        }
        
        if (pContext.getQueriedLootTableId().equals(BuiltInLootTables.ABANDONED_MINESHAFT)) {
            //MiscUtils.infoLog("[Chest Loot Condition] We are spamming the mineshaft carts trying to put loot in. Instead, we are making duplicate item entries in all the chest minecarts. God why.");
            return false;
        }
        
        BlockPos chestPos = new BlockPos((int) vector3d.x, (int) vector3d.y, (int) vector3d.z);
        //get tile entity from chestPos
        BlockEntity chestTile = pContext.getLevel().getBlockEntity(chestPos);
        if(chestTile != null) {
            //MiscUtils.infoLog("[Chest Loot Condition] Tile Entity is " + chestTile.toString());
        }
        else {
            //MiscUtils.infoLog("[Chest Loot Condition] Chest is null???");
        }
        
      
      /*
      check that the tile entity is a lockable storage entity of some sort (e.g. chest or barrel), but not a dispenser or hopper
      things like jungle pyramids use dispensers for traps so we don't want to override the loot table for those
      also would not want to put stuff in a hopper if another mod uses that as part of a structure, in case of messing up a puzzle or redstone mechanism
       */
        boolean isChest = chestTile instanceof RandomizableContainerBlockEntity && !(chestTile instanceof DispenserBlockEntity || chestTile instanceof HopperBlockEntity);
        //MiscUtils.infoLog("[Chest Loot Condition] isChest = " + isChest);
        
        //returns if the tile entity is specifically a chest, and if it's in the right location
        return isChest && this.predicate.matches(pContext.getLevel(), vector3d.x() + (double) this.offset.getX(), vector3d.y() + (double) this.offset.getY(), vector3d.z() + (double) this.offset.getZ());
    }
    
    //json without specified block offset
    public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder pLocationPredicateBuilder) {
        return () -> {
            return new ChestCheckCondition(pLocationPredicateBuilder.build(), BlockPos.ZERO);
        };
    }
    
    //if the json has a specified block offset
    public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder pLocationPredicateBuilder, BlockPos pOffset) {
        return () -> {
            return new ChestCheckCondition(pLocationPredicateBuilder.build(), pOffset);
        };
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ChestCheckCondition> {
        public void serialize(JsonObject jsonObject, ChestCheckCondition instance, JsonSerializationContext context) {
            jsonObject.add("predicate", instance.predicate.serializeToJson());
            if (instance.offset.getX() != 0) {
                jsonObject.addProperty("offsetX", instance.offset.getX());
            }
            
            if (instance.offset.getY() != 0) {
                jsonObject.addProperty("offsetY", instance.offset.getY());
            }
            
            if (instance.offset.getZ() != 0) {
                jsonObject.addProperty("offsetZ", instance.offset.getZ());
            }
            
        }
        
        public ChestCheckCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            LocationPredicate locationpredicate = LocationPredicate.fromJson(jsonObject.get("predicate"));
            int i = GsonHelper.getAsInt(jsonObject, "offsetX", 0);
            int j = GsonHelper.getAsInt(jsonObject, "offsetY", 0);
            int k = GsonHelper.getAsInt(jsonObject, "offsetZ", 0);
            return new ChestCheckCondition(locationpredicate, new BlockPos(i, j, k));
        }
    }
}
