package com.uberhelixx.flatlights.event.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ChestCheckCondition implements ILootCondition {
   public static final LootConditionType GENERIC_STRUCTURE_CHEST = new LootConditionType(new ChestCheckCondition.Serializer());
   
   private final LocationPredicate predicate;
   private final BlockPos blockPos;

   private ChestCheckCondition(LocationPredicate locationPredicate, BlockPos blockPos) {
      this.predicate = locationPredicate;
      this.blockPos = blockPos;
   }

   public LootConditionType getConditionType() {
      return GENERIC_STRUCTURE_CHEST;
   }

   public boolean test(LootContext lootContext) {
      Vector3d vector3d = lootContext.get(LootParameters.ORIGIN);
      return vector3d != null && this.predicate.test(lootContext.getWorld(), vector3d.getX() + (double)this.blockPos.getX(), vector3d.getY() + (double)this.blockPos.getY(), vector3d.getZ() + (double)this.blockPos.getZ());
   }

   public static IBuilder builder(LocationPredicate.Builder builder) {
      return () -> {
         return new ChestCheckCondition(builder.build(), BlockPos.ZERO);
      };
   }

   public static IBuilder func_241547_a_(LocationPredicate.Builder builder, BlockPos blockPos) {
      return () -> {
         return new ChestCheckCondition(builder.build(), blockPos);
      };
   }

   public static class Serializer implements ILootSerializer<ChestCheckCondition> {
      public void serialize(JsonObject jsonObject, ChestCheckCondition instance, JsonSerializationContext context) {
         jsonObject.add("predicate", instance.predicate.serialize());
         if (instance.blockPos.getX() != 0) {
            jsonObject.addProperty("offsetX", instance.blockPos.getX());
         }

         if (instance.blockPos.getY() != 0) {
            jsonObject.addProperty("offsetY", instance.blockPos.getY());
         }

         if (instance.blockPos.getZ() != 0) {
            jsonObject.addProperty("offsetZ", instance.blockPos.getZ());
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