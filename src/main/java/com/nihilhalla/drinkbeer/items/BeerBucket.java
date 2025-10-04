package com.nihilhalla.drinkbeer.items;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import com.nihilhalla.drinkbeer.effects.DrunkStatusEffect;
import com.nihilhalla.drinkbeer.effects.WitherStoutEffect;
import com.nihilhalla.drinkbeer.handlers.BeerListHandler;
import com.nihilhalla.drinkbeer.registries.FluidRegistry;
import com.nihilhalla.drinkbeer.registries.MobEffectRegistry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.common.ForgeMod;

public class BeerBucket extends BucketItem {

    private final Supplier<? extends Fluid> fluidSupplier;

    public BeerBucket(Supplier<? extends Fluid> fluidSupplier, Properties props) {
        super(fluidSupplier, props);
        this.fluidSupplier = fluidSupplier;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 64;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Fluid fluid = fluidSupplier.get();

        Vec3 eyePos = player.getEyePosition(0f);
        Vec3 lookVec = player.getLookAngle();
        double reach = player.getAttribute(ForgeMod.BLOCK_REACH.get()) != null
                ? player.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue()
                : 5.0D;
        Vec3 reachVec = eyePos.add(lookVec.scale(reach));

        BlockHitResult hitResult = level.clip(new ClipContext(
                eyePos, reachVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.SOURCE_ONLY,
                player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return super.use(level, player, hand);
        }

        player.startUsingItem(hand);
        level.playSound(null, player.blockPosition(),
                SoundEvents.GENERIC_DRINK,
                SoundSource.PLAYERS,
                0.5F, 1.0F);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity entity) {
        if (!(entity instanceof Player player)) return stack;

        Fluid fluid = fluidSupplier.get();

        if (!level.isClientSide) {
            if (BeerListHandler.FLUID_EFFECTS_MAP.containsKey(fluid)) {
                var base = BeerListHandler.FLUID_EFFECTS_MAP.get(fluid);
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        base.getEffect(),
                        base.getDuration(),
                        base.getAmplifier()
                ));

                if (fluid != FluidRegistry.SELTZER.get()) {
                    DrunkStatusEffect.addStatusEffect(player, 4);
                }
                if (fluid == FluidRegistry.WITHER_STOUT.get()) {
                    WitherStoutEffect.addStatusEffect(player, 4);
                }
                if (fluid == FluidRegistry.SELTZER.get() && player.hasEffect(MobEffectRegistry.DRUNK.get())) {
                    player.removeEffect(MobEffectRegistry.DRUNK.get());
                }
                if (fluid == FluidRegistry.WISEMAN_BREW.get()) {
                    player.giveExperiencePoints(ThreadLocalRandom.current().nextInt(200,640));

                }
            }

            if (!player.isCreative()) {
                return new ItemStack(Items.BUCKET);
            }
        }

        return stack;
    }
}
