package com.uberhelixx.flatlights.common.item.curio;

import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RandomCurio extends Item {
    public RandomCurio(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            List<Supplier<ItemStack>> CURIO_LIST = new ArrayList<>();
            //get all curios from the CURIOS item registry, and add to CURIO_LIST for pulling loot item to drop
            for(RegistryObject<Item> nextCurio : ModCurios.CURIOS.getEntries()) {
                CURIO_LIST.add(() -> new ItemStack(nextCurio.get(), 1));
            }
            
            ItemStack heldStack = pPlayer.getItemInHand(pUsedHand);
            ItemStack stackToDrop = null;
            
            //gets a random curio from our list of curios, based on the size of the list
            stackToDrop = CURIO_LIST.get(pLevel.getRandom().nextInt(CURIO_LIST.size())).get();
            
            //drop the random curio we picked
            if (stackToDrop != null) {
                pPlayer.drop(stackToDrop, false, false);
            }
            
            //make sure to shrink item stack of the random curio item for each use
            heldStack.shrink(1);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        //how to use curio when not rolled yet
        TooltipHelper.genericBrackets(pTooltipComponents, "Right-click to receive a random curio.", Style.EMPTY.withColor(ChatFormatting.GRAY));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
