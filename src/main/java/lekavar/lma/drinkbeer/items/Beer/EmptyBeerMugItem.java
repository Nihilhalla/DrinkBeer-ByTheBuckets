package lekavar.lma.drinkbeer.items.Beer;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blocks.BeerMugBlock;
import lekavar.lma.drinkbeer.handlers.BeerListHandler;
import lekavar.lma.drinkbeer.handlers.EmptyBeerMugHandler;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.utils.ModCreativeTab;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

public class EmptyBeerMugItem extends BlockItem {
    //The below code is shamelessly borrowed from Tinker's Copper Can Item and modified to fit this mod.
    //Thanks go to mDiyo and the SlimeKnights Team.
    
    private static final String TAG_FLUID = "fluid";
    private static final String TAG_FLUID_TAG = "fluid_tag";

    public EmptyBeerMugItem(Block block, Properties properties) {
        super(block, properties);
    }
    


    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EmptyBeerMugHandler(stack);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return getFluid(stack) != Fluids.EMPTY;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        Fluid fluid = getFluid(stack);
        if (fluid != Fluids.EMPTY) {
            return new ItemStack(this);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Fluid fluid = getFluid(stack);
        if (fluid != Fluids.EMPTY) {
            CompoundTag fluidTag = getFluidTag(stack);
            MutableComponent text;
            if (fluidTag != null) {
                FluidStack displayFluid = new FluidStack(fluid, 250, fluidTag);
                text = displayFluid.getDisplayName().plainCopy();
            } else {
                text = new TranslatableComponent(fluid.getAttributes().getTranslationKey());
            }
            tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".contents", text).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
        }
    }

    /** Sets the fluid on the given stack */
    public static ItemStack setFluid(ItemStack stack, FluidStack fluid) {
      // if empty, try to remove the NBT, helps with recipes
        if (fluid.isEmpty()) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                nbt.remove(TAG_FLUID);
                nbt.remove(TAG_FLUID_TAG);
                if (nbt.isEmpty()) {
                    stack.setTag(null);
                }
            }
        } else {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putString(TAG_FLUID, Objects.requireNonNull(fluid.getFluid().getRegistryName()).toString());
            CompoundTag fluidTag = fluid.getTag();
            if (fluidTag != null) {
                nbt.put(TAG_FLUID_TAG, fluidTag.copy());

            } else {
                nbt.remove(TAG_FLUID_TAG);
            }
            
        }
        return stack;
    }

    /** Gets the fluid from the given stack */
    public static Fluid getFluid(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
        ResourceLocation location = ResourceLocation.tryParse(nbt.getString(TAG_FLUID));
            if (location != null && ForgeRegistries.FLUIDS.containsKey(location)) {
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(location);
                if (fluid != null) {
                    return fluid;
                }
            }
        }
        return Fluids.EMPTY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack != null && stack.getItem() == ItemRegistry.EMPTY_BEER_MUG.get()) {
            ItemStack newStack = new ItemStack(BeerListHandler.buildMugMap(getFluid(stack)));
            stack = newStack;
        }
        return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide);
    }

    /** Gets the fluid NBT from the given stack */
    @Nullable
    public static CompoundTag getFluidTag(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains(TAG_FLUID_TAG, Tag.TAG_COMPOUND)) {
            return nbt.getCompound(TAG_FLUID_TAG);
        }
        return null;
    }
/* 
    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasTag() && BeerListHandler.BeerList().contains(getFluid(stack))) {
            ItemStack newStack = new ItemStack(BeerListHandler.buildMugMap(getFluid(stack)));
            return new TranslatableComponent(getDescriptionId(newStack));
        } else {
            return super.getName(stack);
        }
    }
*/
    /**
     * Gets a string variant name for the given stack
     * @param stack  Stack instance to check
     * @return  String variant name
     */
    public static String getSubtype(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            return nbt.getString(TAG_FLUID);
        }
        return "";
    }
}