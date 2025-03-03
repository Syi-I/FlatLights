package com.uberhelixx.flatlights.common.advancement;

import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.advancement.instance.BlockBreakInstance;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.resources.ResourceLocation;

public class StandProudTrigger extends CustomCriterionTrigger<BlockBreakInstance> {
    public StandProudTrigger(ResourceLocation id) {
        super(id);
    }

    @Override
    public BlockBreakInstance deserialize(JsonObject object, ConditionArrayParser conditions) {
        return null;
    }
}
