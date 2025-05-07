package com.uberhelixx.flatlights.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JogoatAdditionModifier extends LootModifier {
    private final Item addition;
    
    public JogoatAdditionModifier(LootItemCondition[] conditionsIn, Item addition) {
        super(conditionsIn);
        this.addition = addition;
    }
    
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        double DROP_CHANCE = 0.01;
        if(FlatLightsCommonConfig.jogoatDropChance.get() != null) {
            DROP_CHANCE = FlatLightsCommonConfig.jogoatDropChance.get();
        }
        if(Math.random() <= DROP_CHANCE) {
            objectArrayList.add(new ItemStack(addition, 1));
        }
        
        //returns the new modified loot with the additional item(s)
        return objectArrayList;
    }
    
    public static final Supplier<Codec<JogoatAdditionModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("addition").forGetter(m -> m.addition)).apply(inst, JogoatAdditionModifier::new)));
    
    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
