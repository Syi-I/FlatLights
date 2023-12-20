package com.uberhelixx.flatlights.event.loot;

import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class JogoatAdditionModifier extends LootModifier {
    private final Item addition;

    //this is for block drops
    protected JogoatAdditionModifier(ILootCondition[] conditionsIn, Item addition) {
        super(conditionsIn);
        this.addition = addition;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        double DROP_CHANCE = 0.01;
        if(FlatLightsCommonConfig.jogoatDropChance.get() != null) {
            DROP_CHANCE = FlatLightsCommonConfig.jogoatDropChance.get();
        }
        if(Math.random() <= DROP_CHANCE) {
            generatedLoot.add(new ItemStack(addition, 1));
        }

        //returns the new modified loot with the additional item(s)
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<JogoatAdditionModifier> {

        @Override
        public JogoatAdditionModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item addition = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(JSONUtils.getString(object, "addition")));
            return new JogoatAdditionModifier(conditionsIn, addition);
        }

        @Override
        public JsonObject write(JogoatAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            return json;
        }
    }
}
