package com.uberhelixx.flatlights.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class ChestCheckCondition implements ILootCondition {
   public static final LootConditionType GENERIC_STRUCTURE_CHEST = new LootConditionType(new ChestCheckCondition.Serializer());
   
   private final LocationPredicate predicate;
   private final BlockPos offset;
   
   /**
    * Functionally similar to the vanilla {@link net.minecraft.loot.conditions.LocationCheck} condition in that it takes the same arguments.
    * This simply adds another layer to the check which confirms if the location is a chest or not, to function as a generic chest filling condition
    * instead of needing to add individually to every structure's loot table, while also allowing for spawning in other mods' structures if they use
    * chests as loot storage.
    */
   private ChestCheckCondition(LocationPredicate locationPredicate, BlockPos blockPos) {
      this.predicate = locationPredicate;
      this.offset = blockPos;
   }

   public LootConditionType getConditionType() {
      return GENERIC_STRUCTURE_CHEST;
   }

   public boolean test(LootContext lootContext) {
      Vector3d vector3d = lootContext.get(LootParameters.ORIGIN);
      if(vector3d == null) {
         return false;
      }
      
      if(lootContext.getQueriedLootTableId().equals(LootTables.CHESTS_ABANDONED_MINESHAFT)) {
         //LOGGER.info("[Chest Loot Condition] We are spamming the mineshaft carts trying to put loot in. Instead, we are making duplicate item entries in all the chest minecarts. God why.");
         return false;
      }
      
      //get BlockPos from context
      BlockPos chestLocation = new BlockPos(vector3d);
      //get tile entity from chestPos
      TileEntity chestTile = lootContext.getWorld().getTileEntity(chestLocation);
      
      /*
      check that the tile entity is a lockable storage entity of some sort (e.g. chest or barrel), but not a dispenser or hopper
      things like jungle pyramids use dispensers for traps so we don't want to override the loot table for those
      also would not want to put stuff in a hopper if another mod uses that as part of a structure, in case of messing up a puzzle or redstone mechanism
       */
      boolean isChest = chestTile instanceof LockableLootTileEntity && !(chestTile instanceof DispenserTileEntity || chestTile instanceof HopperTileEntity);
      
      //returns if the tile entity is specifically a chest, and if it's in the right location
      return isChest && this.predicate.test(lootContext.getWorld(), vector3d.getX() + (double)this.offset.getX(), vector3d.getY() + (double)this.offset.getY(), vector3d.getZ() + (double)this.offset.getZ());
   }

   //json without specified block offset
   public static IBuilder builder(LocationPredicate.Builder builder) {
      return () -> {
         return new ChestCheckCondition(builder.build(), BlockPos.ZERO);
      };
   }

   //if the json has a specified block offset
   public static IBuilder offsetBuilder(LocationPredicate.Builder builder, BlockPos blockPos) {
      return () -> {
         return new ChestCheckCondition(builder.build(), blockPos);
      };
   }

   public static class Serializer implements ILootSerializer<ChestCheckCondition> {
      public void serialize(JsonObject jsonObject, ChestCheckCondition instance, JsonSerializationContext context) {
         jsonObject.add("predicate", instance.predicate.serialize());
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
         LocationPredicate locationpredicate = LocationPredicate.deserialize(jsonObject.get("predicate"));
         int i = JSONUtils.getInt(jsonObject, "offsetX", 0);
         int j = JSONUtils.getInt(jsonObject, "offsetY", 0);
         int k = JSONUtils.getInt(jsonObject, "offsetZ", 0);
         return new ChestCheckCondition(locationpredicate, new BlockPos(i, j, k));
      }
   }
}