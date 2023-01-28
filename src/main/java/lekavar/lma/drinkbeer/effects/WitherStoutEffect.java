package lekavar.lma.drinkbeer.effects;

import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class WitherStoutEffect extends MobEffect {
    private final static int BASE_DURATION = 1200;
    public static int AMPLITUDE = 0;
    private final static int MAX_AMPLITUDE = 4;

    public WitherStoutEffect() {
        super(MobEffectCategory.BENEFICIAL, new Color(30, 30, 30, 255).getRGB());
        
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public static void addStatusEffect( LivingEntity user, int value) {
                MobEffectInstance statusEffectInstance = user.getEffect(MobEffectRegistry.WITHER_RESIST.get());
                int currentAmplifier = statusEffectInstance == null ? -1 : statusEffectInstance.getAmplifier();
                int newAmplifier = currentAmplifier + value;
                newAmplifier = Math.min(newAmplifier, MAX_AMPLITUDE);
                user.addEffect(new MobEffectInstance(MobEffectRegistry.WITHER_RESIST.get(), (BASE_DURATION * (newAmplifier + 1)), newAmplifier));
    }

    public static void addStatusEffect(LivingEntity user) {
        addStatusEffect(user,1);
    }

    /*
    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        int remainingTime = entity.getEffect(MobEffectRegistry.DRUNK_FROST_WALKER.get()).getDuration();
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, remainingTime));
        FrostWalkerEnchantment.onEntityMoved(entity, entity.level, new BlockPos(entity.position()), 1);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
     */
}