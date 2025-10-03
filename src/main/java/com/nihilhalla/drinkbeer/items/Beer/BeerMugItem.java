package com.nihilhalla.drinkbeer.items.Beer;

import com.nihilhalla.drinkbeer.effects.DrunkStatusEffect;
import com.nihilhalla.drinkbeer.effects.HellbrewEffect;
import com.nihilhalla.drinkbeer.effects.NightHowlStatusEffect;
import com.nihilhalla.drinkbeer.effects.WitherStoutEffect;
import com.nihilhalla.drinkbeer.registries.ItemRegistry;
import com.nihilhalla.drinkbeer.registries.MobEffectRegistry;
//import com.nihilhalla.drinkbeer.utils.ModCreativeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

public class BeerMugItem extends BeerBlockItem {
    private final static double MAX_PLACE_DISTANCE = 2.0D;
    private final boolean hasExtraTooltip;
    
    public BeerMugItem(Block block, int nutrition, boolean hasExtraTooltip) {
        super(block,new Item.Properties().stacksTo(16)
                .food(new FoodProperties.Builder().nutrition(nutrition).alwaysEat().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, @Nullable MobEffectInstance statusEffectInstance, int nutrition, boolean hasExtraTooltip) {
        super(block,new Item.Properties().stacksTo(16)
                .food(statusEffectInstance != null
                        ? new FoodProperties.Builder().nutrition(nutrition).effect(statusEffectInstance, 1).alwaysEat().build()
                        : new FoodProperties.Builder().nutrition(nutrition).alwaysEat().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, Supplier<MobEffectInstance> statusEffectInstance, int nutrition, boolean hasExtraTooltip) {
        super(block,new Item.Properties().stacksTo(16)
                .food(statusEffectInstance != null
                        ? new FoodProperties.Builder().nutrition(nutrition).effect(statusEffectInstance, 1).alwaysEat().build()
                        : new FoodProperties.Builder().nutrition(nutrition).alwaysEat().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext p_195944_1_, BlockState p_195944_2_) {
        if ((p_195944_1_.getClickLocation().distanceTo(p_195944_1_.getPlayer().position()) > MAX_PLACE_DISTANCE))
            return false;
        else {
            return super.canPlace(p_195944_1_, p_195944_2_);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        String name = this.asItem().toString();
        if (hasEffectNoticeTooltip() && world != null && world.isClientSide()) {
            tooltip.add(Component.translatable("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)));
        }
        //String hunger = String.valueOf(stack.getItem().getFoodProperties().getNutrition());
        //tooltip.add(Component.translatable("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)).append(hunger));
    }

    private boolean hasEffectNoticeTooltip() {
        return this.hasExtraTooltip;
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        //Give Drunk status effect unless we drank Seltzer
        if (stack.getItem() != ItemRegistry.BEER_MUG_SELTZER.get()) {
            DrunkStatusEffect.addStatusEffect(user);
        }
        //Give Night Vision status effect if drank Night Howl Kvass
        NightHowlStatusEffect.addStatusEffect(stack,world,user);
        //Give Wither Resistance if drank Wither Stout
        /*if (stack.getItem() == ItemRegistry.BEER_MUG_WITHER_STOUT.get()) {
            WitherStoutEffect.addStatusEffect(user);
        }*/
        if (stack.getItem() == ItemRegistry.BEER_MUG_WISEMAN_BREW.get() && user instanceof ServerPlayer serverplayer) {
            serverplayer.giveExperiencePoints(ThreadLocalRandom.current().nextInt(50,160));
        }
        /* 
        if (stack.getItem() == ItemRegistry.BEER_MUG_HELLBREW.get() && user instanceof ServerPlayer serverplayer) {
            HellbrewEffect.addStatusEffect(user);
        }
        */

        //reduce Drunk level if we drank seltzer
        if (stack.getItem() == ItemRegistry.BEER_MUG_SELTZER.get()) {
            user.removeEffect(MobEffectRegistry.DRUNK.get());
            if(user.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                user.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            }
            if(user.hasEffect(MobEffects.CONFUSION)){
                user.removeEffect(MobEffects.CONFUSION);
            }
        }
        //Give empty mug back
        giveEmptyMugBack(user);

        return super.finishUsingItem(stack, world, user);
    }
}
