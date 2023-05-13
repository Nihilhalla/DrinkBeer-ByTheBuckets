/*package lekavar.lma.drinkbeer.items;

import java.util.function.Supplier;

import lekavar.lma.drinkbeer.effects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.effects.WitherStoutEffect;
import lekavar.lma.drinkbeer.handlers.BeerListHandler;
import lekavar.lma.drinkbeer.registries.FluidRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

public class BeerBucket extends MilkBucketItem {
    public BeerBucket(Supplier<? extends Fluid> fluidSupplier, Properties builder) {
        super(builder);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (world.isClientSide) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            if (itemStack.getItem() instanceof BeerBucket) {
                BeerBucket bucket = (BeerBucket) itemStack.getItem();
                Fluid fluid = bucket.getFluid();

                player.addEffect(BeerListHandler.FLUID_EFFECTS_MAP.get(fluid), player);
                if (fluid != FluidRegistry.SELTZER.get()) {
                    DrunkStatusEffect.addStatusEffect(player, 4);
                }
                if (fluid == FluidRegistry.WITHER_STOUT.get()) {
                    WitherStoutEffect.addStatusEffect(player, 4);
                }
                // Consume the bucket and give the player an empty bucket
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                }
            }
            return InteractionResultHolder.success(itemStack);
        }
    }
}
*/