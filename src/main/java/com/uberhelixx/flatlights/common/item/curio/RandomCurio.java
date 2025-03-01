package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RandomCurio extends Item {
    public RandomCurio(Properties properties) {
        super(properties);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //how to use curio when not rolled yet
        ITextComponent useTooltip = TextHelpers.genericBrackets("Right-click to receive a random curio.", TextFormatting.GRAY);
        tooltip.add(useTooltip);
        
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            List<Supplier<ItemStack>> CURIO_LIST = new ArrayList<>();
            //get all curios from the CURIOS item registry, and add to CURIO_LIST for pulling loot item to drop
            for(RegistryObject<Item> nextCurio : ModCurios.CURIOS.getEntries()) {
                CURIO_LIST.add(() -> new ItemStack(nextCurio.get(), 1));
            }
            
            ItemStack heldStack = player.getHeldItem(hand);
            ItemStack stackToDrop = null;
            
            //gets a random curio from our list of curios, based on the size of the list
            stackToDrop = CURIO_LIST.get(random.nextInt(CURIO_LIST.size())).get();
            
            //drop the random curio we picked
            if (stackToDrop != null) {
                player.dropItem(stackToDrop, false, false);
            }
            
            //make sure to shrink item stack of the random curio item for each use
            heldStack.shrink(1);
        }
        
        return super.onItemRightClick(world, player, hand);
    }
}
