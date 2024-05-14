package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.network.PacketGenericPlayerNotification;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
public class CurioEvents {

    /**
     * Calculates progress value for Growth type curios and updates growth progress for appropriate curios
     * @param event LivingDeathEvent for when mobs are killed
     */
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void growthProgress(LivingDeathEvent event) {
        Entity mob = event.getEntity();
        Entity killer = event.getSource().getTrueSource();
        if(killer instanceof PlayerEntity && mob instanceof LivingEntity) {
            PlayerEntity player = (PlayerEntity) killer;
            World world = killer.getEntityWorld();

            List<ItemStack> curioList = CurioUtils.getWornCurios(player);

            //gained cores is equal to how many times more HP the mob had compared to the player's base 20 HP
            int gainedCores = Math.max((Math.round(((LivingEntity) mob).getMaxHealth() / 20)), 1);

            //kill text notification formatting stuff
            String coreGainText = " core.";
            if(gainedCores > 1) {
                coreGainText = " cores.";
            }
            ITextComponent killMessage = new StringTextComponent("You have slain a creature and gained " + (gainedCores) + coreGainText);
            if(FlatLightsClientConfig.coreNoti.get()) {
                if(!killer.getEntityWorld().isRemote()) {
                    PacketHandler.sendToPlayer((ServerPlayerEntity) killer, new PacketGenericPlayerNotification(killMessage.getString()));
                }
            }

            for(ItemStack curio : curioList) {
                CompoundNBT tag = curio.getTag();
                //ensure that the curio is a growth type curio before applying any updates to the curio's nbt data
                if (tag != null) {
                    if (tag.contains(CurioUtils.GROWTH_TRACKER) && tag.contains(CurioUtils.GROWTH_CAP) && CurioUtils.getCurioTier(curio) == CurioTier.GROWTH) {
                        //get growth progress and growth cap from curio nbt
                        int growthProgress = tag.getInt(CurioUtils.GROWTH_TRACKER);
                        int growthCap = tag.getInt(CurioUtils.GROWTH_CAP);
                        int newProgress = growthProgress + gainedCores;

                        //check for if new progress total is higher than growth cap, then cap out progress if true so you can't gain extra stats
                        if (newProgress > growthCap) {
                            //play sound if max progress on curio
                            world.playSound(null, killer.getPosX(), killer.getPosY(), killer.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.PLAYERS, 0.2f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
                            newProgress = growthCap;
                        }

                        //update growth tracker nbt data and set tag on this curio
                        tag.putInt(CurioUtils.GROWTH_TRACKER, newProgress);
                        curio.setTag(tag);

                        //send packet to server to indicate that the curio nbt is changed
                        PacketHandler.sendToServer(new PacketWriteNbt(tag, curio));
                    }
                }
            }
        }
    }
}
