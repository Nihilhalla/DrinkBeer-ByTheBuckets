package lekavar.lma.drinkbeer.items;

import com.mojang.math.Vector3d;
import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.utils.ModCreativeTab;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import static java.lang.Math.pow;
import static net.minecraft.util.Mth.sqrt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class BeerBlockItem extends BlockItem implements ICapabilityProvider {
    protected final static float MAX_PLACE_DISTANCE = (float) 2;

    public BeerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static Capability<IFluidHandlerItem> FLUID_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
    ItemStack itemStack = new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get(), 1); // create a new ItemStack object with one item
    IFluidHandlerItem beerMugTank = itemStack.getCapability(FLUID_HANDLER).orElse(null); // get the fluid handler for the item

    private final LazyOptional<IFluidHandlerItem> beerMugHolder = LazyOptional.of(() -> beerMugTank);

    @SuppressWarnings("unchecked")
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            //DrinkBeer.LOG.atDebug().log(beerMugHolder.cast());
            return beerMugHolder.cast();
        }
        //DrinkBeer.LOG.atDebug().log(LazyOptional.empty().toString());
        return LazyOptional.empty();
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    public float getDistance(Vector3d p1, Vector3d p2) {
        return sqrt((float) (pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2) + pow(p1.z - p2.z, 2)));
    }

    public void giveEmptyMugBack(LivingEntity user) {
        if (!(user instanceof Player) || !((Player) user).isCreative()) {
            ItemStack emptyMugItemStack = new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get(), 1);
            if (user instanceof Player) {
                if (!((Player) user).addItem(emptyMugItemStack))
                    ((Player) user).drop(emptyMugItemStack, false);
            } else {
                user.spawnAtLocation(emptyMugItemStack);
            }
        }
    }
    
}
