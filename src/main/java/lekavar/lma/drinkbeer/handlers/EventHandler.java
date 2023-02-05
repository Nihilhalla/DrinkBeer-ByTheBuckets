package lekavar.lma.drinkbeer.handlers;

import java.util.List;
import javax.annotation.Nullable;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.effects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.entities.damages.AlcoholDamage;
import lekavar.lma.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DrinkBeer.MOD_ID)
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDamage(LivingHurtEvent event) {
        if(event.getEntityLiving() != null) {
            //DrinkBeer.LOG.atDebug().log(event.getEntity().toString() + " has been hit!");
            if (event.getEntityLiving().hasEffect(MobEffectRegistry.WITHER_RESIST.get()) && event.getSource() == DamageSource.WITHER) {
                    float newAmount = event.getAmount() / (2F * (event.getEntityLiving().getEffect(MobEffectRegistry.WITHER_RESIST.get()).getAmplifier() + 1F));

                    if (newAmount < 0.124){
                        event.setCanceled(true);
                    } else {
                        //DrinkBeer.LOG.atDebug().log("Mitigating Damage to " + newAmount);
                        event.setAmount(newAmount);
                    }

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
    Vec3 testPos = playerEntity.getPosition(1);
    Vec3 playerPos = new Vec3(playerEntity.getX(), playerEntity.getY() , playerEntity.getZ());
    BlockPos playerOn = new BlockPos(playerPos);
    BlockState blockState = playerEntity.level.getBlockState(playerOn);
    @Nullable FluidState fluidState = playerEntity.level.getFluidState(playerOn);
    AABB playerBox = playerEntity.getBoundingBox();
    List<AABB> blockBox = blockState.getShape(playerEntity.level, playerOn).toAabbs();
        if ((!fluidState.getTags().toList().contains(DrinkBeer.ALCOHOLS))) {
            i = 0;
        }
        if (isColliding(blockBox, playerBox) && fluidState.getTags().toList().contains(DrinkBeer.ALCOHOLS) && i <= 1200) {
            i += 1;
            if (i == 1200) {
                DrunkStatusEffect.addStatusEffect(playerEntity);
                i = 0;
            }
            /* //This part doesn't work yet, not sure if I want it to yet either.
            if (i == 200 && playerEntity.getActiveEffectsMap().get(MobEffectRegistry.DRUNK.get()).getAmplifier() >= 4){
                playerEntity.hurt(AlcoholDamage.ALCOHOL_DAMAGE, 1f);
            }*/
        }
    }
}
