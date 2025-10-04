package com.nihilhalla.drinkbeer.handlers;

import java.util.List;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.effects.DrunkStatusEffect;
import com.nihilhalla.drinkbeer.registries.DamageRegistry;
import com.nihilhalla.drinkbeer.registries.FluidRegistry;
import com.nihilhalla.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DrinkBeer.MOD_ID)
public class EventHandler {
    public static ItemStack newStack;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDamage(LivingHurtEvent event) {
        if(event.getEntity() != null) {
            //DrinkBeer.LOG.atDebug().log(event.getEntity().toString() + " has been hit!");
            if (event.getEntity().hasEffect(MobEffectRegistry.WITHER_RESIST.get()) && event.getSource().is(DamageTypes.WITHER)) {
                    @SuppressWarnings("null")
                    float newAmount = event.getAmount() / (2F * (event.getEntity().getEffect(MobEffectRegistry.WITHER_RESIST.get()).getAmplifier() + 1F));

                        //DrinkBeer.LOG.atDebug().log("Mitigating Damage to " + newAmount);
                        event.setAmount(newAmount);


            }
            if (event.getEntity().hasEffect(MobEffectRegistry.HELLBREW.get()) && (event.getSource().is(DamageTypes.LAVA) || event.getSource().is(DamageTypes.IN_FIRE) || event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.FIREBALL))) {
                @SuppressWarnings("null")    
                float newAmount = event.getAmount() / (2F * (event.getEntity().getEffect(MobEffectRegistry.HELLBREW.get()).getAmplifier() + 1F));
                        //DrinkBeer.LOG.atDebug().log("Mitigating Damage to " + newAmount);
                        event.setAmount(newAmount);

            }
        }
    }
    @SuppressWarnings("null")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityAttacked(LivingAttackEvent event) {
        if(event.getEntity() != null) {
            //DrinkBeer.LOG.atDebug().log(event.getEntity().toString() + " has been hit!");
            if (event.getEntity().hasEffect(MobEffectRegistry.WITHER_RESIST.get()) && event.getEntity().getEffect(MobEffectRegistry.WITHER_RESIST.get()).getAmplifier() >= 4 && event.getSource().is(DamageTypes.WITHER)) {
                        event.setCanceled(true);
                        //DrinkBeer.LOG.atDebug().log("Canceled damage from Wither Effect");

            }
            if (event.getEntity().hasEffect(MobEffectRegistry.HELLBREW.get()) && event.getEntity().getEffect(MobEffectRegistry.HELLBREW.get()).getAmplifier() >= 4 && (event.getSource().is(DamageTypes.LAVA) || event.getSource().is(DamageTypes.IN_FIRE) || event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.FIREBALL))) {
                        event.setCanceled(true);
                        //DrinkBeer.LOG.atDebug().log("Canceled damage from Lava and/or Fire");

            }
        }
    }

    /*
    @SuppressWarnings("null")
    @SubscribeEvent
    public static void onBucketRightClick(PlayerInteractEvent.RightClickItem event) {
        LivingEntity player = event.getEntity();
        net.minecraft.world.level.Level level = event.getLevel();
        ItemStack stack = event.getItemStack();

        // Only intercept BucketItems
        if (!(stack.getItem() instanceof BucketItem bucketItem)) return;

        Fluid fluid = bucketItem.getFluid();

        // Only accepted fluids
        if (!BeerListHandler.acceptedFluids.contains(fluid)) return;

        // Ray trace using player's reach (modded reach supported)
        Vec3 eyePos = player.getEyePosition(0.0F);
        Vec3 lookVec = player.getLookAngle();
        double reach = player.getAttribute(ForgeMod.BLOCK_REACH.get()) != null
            ? player.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue()
            : 5.0D;
        Vec3 endVec = eyePos.add(lookVec.scale(reach));

        BlockHitResult hit = level.clip(new ClipContext(
            eyePos,
            endVec,
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.SOURCE_ONLY,
            player
        ));
        // If targeting a block, let vanilla bucket placement happen
        if (hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) return;

        // Otherwise, drink it
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (!level.isClientSide) {
            // Get base effect from FLUID_EFFECTS_MAP
            if (BeerListHandler.FLUID_EFFECTS_MAP.containsKey(fluid)) {
                var base = BeerListHandler.FLUID_EFFECTS_MAP.get(fluid);

                // Apply amplified effect (×4)
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        base.getEffect(),
                        base.getDuration(),
                        base.getAmplifier()
                ));
                if (ConfigHandler.ENABLE_DRUNK_EFFECT.get()) {
                    DrunkStatusEffect.addStatusEffect(player, 4);;
                }
 
            //    if (fluid == FluidRegistry.WITHER_STOUT.get()){
            //        for (int i = 0; i < 4; i++) {
            //            player.addEffect(new MobEffectInstance(MobEffectRegistry.WITHER_RESIST.get()));
            //        }
            //    }
            //    if (fluid == FluidRegistry.HELLBREW.get()){
            //        for (int i = 0; i < 4; i++) {
            //            player.addEffect(new MobEffectInstance(MobEffectRegistry.HELLBREW.get()));
            //        }
            //    }
                
            }

            // Replace bucket with empty bucket
            player.setItemInHand(event.getHand(), new ItemStack(Items.BUCKET));
        } else {
            // Play drinking sound client-side
            level.playSound(player, player.blockPosition(),
                    net.minecraft.sounds.SoundEvents.GENERIC_DRINK,
                    net.minecraft.sounds.SoundSource.PLAYERS,
                    1.0F, 1.0F);
        }
    }
    */
    //Checking for a collision
    public boolean isColliding(List<AABB> blockBox, AABB playerBox) {
        for (AABB aabb : blockBox) { 
            if (!aabb.intersects(playerBox)) {
                //DrinkBeer.LOG.atDebug().log("We've hit something");
                return false;
            }
        }
        return true;
    }
    //Some Variables that need to be outside the tick.
    public int i = 0;
    public int ampMax = 5;
    public int ampCurrent = 0;
    //Check if we're in alcohol
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
    //Setting up some variables for collision
    LivingEntity playerEntity = event.player;
    MobEffectInstance drunkEffect = playerEntity.getEffect(MobEffectRegistry.DRUNK.get());
    Vec3 testPos = playerEntity.getPosition(1);
    Vec3i playerPos = new Vec3i(playerEntity.getBlockX(), playerEntity.getBlockY() , playerEntity.getBlockZ());
    BlockPos playerOn = new BlockPos(playerPos);
    BlockState blockState = playerEntity.level().getBlockState(playerOn);
    @Nullable FluidState fluidState = playerEntity.level().getFluidState(playerOn);
    AABB playerBox = playerEntity.getBoundingBox();
    List<AABB> blockBox = blockState.getShape(playerEntity.level(), playerOn).toAabbs();
    if (!playerEntity.level().isClientSide 
        && ConfigHandler.ENABLE_ALCOHOL_DAMAGE.get() 
        && playerEntity.hasEffect(MobEffectRegistry.DRUNK.get())
        && playerEntity.getEffect(MobEffectRegistry.DRUNK.get()).getAmplifier() >= 4) {
            int tickCount = playerEntity.getPersistentData().getInt("drinkbeer.alcoholTick");

        if (tickCount >= 20) { // every 20 ticks = once per second
            float damage = .25F * drunkEffect.getAmplifier();
            playerEntity.hurt(DamageRegistry.alcohol((ServerLevel) playerEntity.level()), damage);
            playerEntity.getPersistentData().putInt("drinkbeer.alcoholTick", 0); // reset
        } else {
            playerEntity.getPersistentData().putInt("drinkbeer.alcoholTick", tickCount + 1); // increment
        }
    }
    }
}
