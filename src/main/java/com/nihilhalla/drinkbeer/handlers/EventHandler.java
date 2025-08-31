package com.nihilhalla.drinkbeer.handlers;

import java.util.List;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.effects.DrunkStatusEffect;
import com.nihilhalla.drinkbeer.registries.DamageRegistry;
import com.nihilhalla.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DrinkBeer.MOD_ID)
public class EventHandler {
    public static ItemStack newStack;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDamage(LivingHurtEvent event) {
        if(event.getEntity() != null) {
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
/*      //This bit of code was the old laggy stupid way of changing out the beers, I found a better way.
        if (event.player.getInventory().contains(DrinkBeer.EMPTY_BEER)) {
            event.player.getInventory();
            for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
                ItemStack itemStack = event.player.getInventory().getItem(i);
                //DrinkBeer.LOG.atDebug().log(EmptyBeerMugItem.getFluid(itemStack) != null);
                if (itemStack != ItemStack.EMPTY && itemStack.getItem() == ItemRegistry.EMPTY_BEER_MUG.get() && BeerListHandler.BeerList().contains(EmptyBeerMugItem.getFluid(itemStack))) {
                    int iCount = itemStack.getCount();
                    newStack = new ItemStack(BeerListHandler.buildMugMap(EmptyBeerMugItem.getFluid(itemStack)), iCount);
                    event.player.getInventory().removeItem(itemStack);
                    event.player.getInventory().add(newStack);
                    //DrinkBeer.LOG.atDebug().log("Updated Mug to: " + newStack.getItem().getRegistryName());
                    event.player.getInventory().setChanged();
                }
            }    
        }*/
    }
/*     @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    ItemStack itemStack = event.getItemStack();
    Player player = event.getPlayer();
    Level world = event.getWorld();

       // Check if the player is holding a bucket
        if (itemStack.getItem() instanceof BeerBucket) {
            BeerBucket bucket = (BeerBucket) itemStack.getItem();
            Fluid fluid = bucket.getFluid();

            if (BeerListHandler.beers.contains(fluid)) {
                // Cancel the event to prevent further processing
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    } */
}
