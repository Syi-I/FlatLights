package com.uberhelixx.flatlights.advancement;

import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.advancement.instance.BlockBreakInstance;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class StandProudTrigger extends CustomCriterionTrigger<BlockBreakInstance> {
    public StandProudTrigger(ResourceLocation id) {
        super(id);
    }

    @Override
    public BlockBreakInstance deserialize(JsonObject object, ConditionArrayParser conditions) {
        return null;
    }
}
