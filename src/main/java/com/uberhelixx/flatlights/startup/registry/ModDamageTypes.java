package com.uberhelixx.flatlights.startup.registry;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDamageTypes {
    //yoinked format from IaF since damage types are so different from 1.16 damage sources
    public static final ResourceKey<DamageType> QUANTUM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlatLights.MODID + ":quantum"));
    public static final ResourceKey<DamageType> ENTANGLED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlatLights.MODID + ":entangled"));
    public static final ResourceKey<DamageType> PHYSICAL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlatLights.MODID + ":physical"));
    public static final ResourceKey<DamageType> BLEED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlatLights.MODID + ":bleed"));
    
    public static class CustomEntityDamageSource extends DamageSource {
        public CustomEntityDamageSource(Holder<DamageType> damageTypeIn, @Nullable Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
        }
    }
    
    public static class CustomIndirectEntityDamageSource extends DamageSource {
        public CustomIndirectEntityDamageSource(Holder<DamageType> damageTypeIn, Entity source, @Nullable Entity trueSource) {
            super(damageTypeIn, source, trueSource);
        }
    }
    
    /**
     * Cause direct quantum damage to an entity.
     * @param source The entity causing the damage
     * @return Quantum damage source
     */
    public static CustomEntityDamageSource causeQuantumDamage(@Nullable Entity source) {
        Holder<DamageType> holder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(QUANTUM);
        return new CustomEntityDamageSource(holder, source);
    }
    
    /**
     * Cause direct entangled damage to an entity.
     * @param source The entity causing the damage
     * @return Entangled damage source
     */
    public static CustomEntityDamageSource causeEntangledDamage(@Nullable Entity source) {
        Holder<DamageType> holder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ENTANGLED);
        return new CustomEntityDamageSource(holder, source);
    }
    
    /**
     * Cause direct physical damage to an entity.
     * @param source The entity causing the damage
     * @return Physical damage source
     */
    public static CustomEntityDamageSource causePhysicalDamage(@Nullable Entity source) {
        Holder<DamageType> holder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(PHYSICAL);
        return new CustomEntityDamageSource(holder, source);
    }
    
    /**
     * Cause direct bleed damage to an entity.
     * @param source The entity causing the damage
     * @return Bleed damage source
     */
    public static CustomEntityDamageSource causeBleedDamage(@Nullable Entity source) {
        Holder<DamageType> holder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLEED);
        return new CustomEntityDamageSource(holder, source);
    }
    
    /**
     * Cause indirect quantum damage to an entity.
     * NOTE: When source != trueSource, isIndirect() returns true so if source == trueSource simply use causeQuantumDamage instead.
     * @param source The direct source of the damage, such as a projectile
     * @param trueSource The real source of the damage, such as the shooter of a projectile
     * @return Indirect quantum damage source
     */
    public static CustomIndirectEntityDamageSource causeIndirectQuantumDamage(Entity source, @Nullable Entity trueSource) {
        Holder<DamageType> holder = trueSource.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(QUANTUM);
        return new CustomIndirectEntityDamageSource(holder, source, trueSource);
    }
    
    /**
     * Cause indirect entangled damage to an entity.
     * NOTE: When source != trueSource, isIndirect() returns true so if source == trueSource simply use causeEntangledDamage instead.
     * @param source The direct source of the damage, such as a projectile
     * @param trueSource The real source of the damage, such as the shooter of a projectile
     * @return Indirect entangled damage source
     */
    public static CustomIndirectEntityDamageSource causeIndirectEntangledDamage(Entity source, @Nullable Entity trueSource) {
        Holder<DamageType> holder = trueSource.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ENTANGLED);
        return new CustomIndirectEntityDamageSource(holder, source, trueSource);
    }
    
    /**
     * Cause indirect physical damage to an entity.
     * NOTE: When source != trueSource, isIndirect() returns true so if source == trueSource simply use causePhysicalDamage instead.
     * @param source The direct source of the damage, such as a projectile
     * @param trueSource The real source of the damage, such as the shooter of a projectile
     * @return Indirect physical damage source
     */
    public static CustomIndirectEntityDamageSource causeIndirectPhysicalDamage(Entity source, @Nullable Entity trueSource) {
        Holder<DamageType> holder = trueSource.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(PHYSICAL);
        return new CustomIndirectEntityDamageSource(holder, source, trueSource);
    }
    
    /**
     * Cause indirect bleed damage to an entity.
     * NOTE: When source != trueSource, isIndirect() returns true so if source == trueSource simply use causeBleedDamage instead.
     * @param source The direct source of the damage, such as a projectile
     * @param trueSource The real source of the damage, such as the shooter of a projectile
     * @return Indirect bleed damage source
     */
    public static CustomIndirectEntityDamageSource causeIndirectBleedDamage(Entity source, @Nullable Entity trueSource) {
        Holder<DamageType> holder = trueSource.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLEED);
        return new CustomIndirectEntityDamageSource(holder, source, trueSource);
    }
    
    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                // Tell generator to run only when server data are generating
                event.includeServer(),
                (DataProvider.Factory<ModDamageTypeTagsProvider>) output -> new ModDamageTypeTagsProvider(
                        event.getGenerator().getPackOutput(),
                        event.getLookupProvider(),
                        FlatLights.MODID,
                        event.getExistingFileHelper()
                )
        );
    }
    
    public static class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
        
        public ModDamageTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, modId, existingFileHelper);
        }
        
        //add tags to change the properties of individual damage types
        @Override
        public void addTags(HolderLookup.Provider pProvider) {
            this.tag(DamageTypeTags.BYPASSES_ARMOR).add(QUANTUM);
            this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ENTANGLED);
            this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ENTANGLED);
            this.tag(DamageTypeTags.BYPASSES_ARMOR).add(BLEED);
            this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(BLEED);
        }
    }
}
