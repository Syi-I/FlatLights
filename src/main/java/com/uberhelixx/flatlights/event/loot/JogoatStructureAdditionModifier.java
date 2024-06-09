package com.uberhelixx.flatlights.event.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class JogoatStructureAdditionModifier extends LootModifier {
    private final Item addition;

    //this is for structure chest loot not block drops
    protected JogoatStructureAdditionModifier(ILootCondition[] conditionsIn, Item addition) {
        super(conditionsIn);
        this.addition = addition;
    }

    /*
    could not get this part of the jogoat_structure_loot.json condition to work for some reason idk
    ,
    {
      "condition": "minecraft:entity_properties",
      "entity": "this",
      "predicate": {
        "type": "minecraft:chest_minecart"
      }
    }
     */
    
    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //generatedLoot is the loot that would be dropped before adding new items here
        //can add based on chance (some conditional if statement) or guarantee (no condition checks)
        //this should check the location from the LootContext to see if there is a tile entity at the origin position
        if(context.get(LootParameters.ORIGIN) != null) {
            //get BlockPos from context
            BlockPos chestPos = new BlockPos(context.get(LootParameters.ORIGIN));
            //get tile entity from chestPos
            TileEntity chest = context.getWorld().getTileEntity(chestPos);
            /*
            check if this tile entity is an instance of a LockableLootTileEntity but not a dispenser or hopper, if so then put in extra loot
            this gets all chests/barrels/shulkers/things that extend those classes but ignores dispensers and hoppers (dispensers are used in
            jungle temples, but we don't want to modify the loot for those since they're traps not loot inventories)
            
            also checks if the queried loot table is from mineshafts, because those use chest minecarts which are not tile entities,
            but we still want to fill those
            */
            
            if((chest instanceof LockableLootTileEntity && !(chest instanceof DispenserTileEntity || chest instanceof HopperTileEntity))
                    || context.getQueriedLootTableId().equals(LootTables.CHESTS_ABANDONED_MINESHAFT)) {
                //if(context.getRandom().nextFloat() > 0.15) {
                generatedLoot.add(new ItemStack(addition, 1));
                //}
            }
        }
        /*if(context.get(LootParameters.THIS_ENTITY) != null) {
            Entity minecart = context.get(LootParameters.THIS_ENTITY);
            if(minecart instanceof ChestMinecartEntity) {
                generatedLoot.add(new ItemStack(addition, 64));
            }
        }*/
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<JogoatStructureAdditionModifier> {

        @Override
        public JogoatStructureAdditionModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item addition = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(JSONUtils.getString(object, "addition")));
            return new JogoatStructureAdditionModifier(conditionsIn, addition);
        }

        @Override
        public JsonObject write(JogoatStructureAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            return json;
        }
    }
}
