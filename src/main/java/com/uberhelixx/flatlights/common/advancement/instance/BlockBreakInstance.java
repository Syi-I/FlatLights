package com.uberhelixx.flatlights.advancement.instance;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class BlockBreakInstance extends CriterionInstance {
    public BlockBreakInstance(ResourceLocation criterion, EntityPredicate.AndPredicate playerCondition) {
        super(criterion, playerCondition);
    }

    private BlockBreakInstance(ResourceLocation criterionIn) {
        super(criterionIn, EntityPredicate.AndPredicate.ANY_AND);
    }

    @Override
    public JsonObject serialize(ConditionArraySerializer conditions) {
        JsonObject out = super.serialize(conditions);
        out.addProperty("levelNeeded", String.valueOf(this));
        return out;
    }

    public static BlockBreakInstance deserialize(ResourceLocation id, JsonObject json) {
        BlockBreakInstance instance = new BlockBreakInstance(id);
        return instance;
    }

    public boolean test(ServerPlayerEntity player) {
        return false;
    }
}
