package com.nihilhalla.drinkbeer.blockentities;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.blocks.WhiskyStillBlock;
import com.nihilhalla.drinkbeer.gui.WhiskyStillContainer;
import com.nihilhalla.drinkbeer.handlers.BeerListHandler;
import com.nihilhalla.drinkbeer.recipes.BrewingRecipe;
import com.nihilhalla.drinkbeer.recipes.DistillationRecipe;
import com.nihilhalla.drinkbeer.recipes.IBrewingInventory;
import com.nihilhalla.drinkbeer.registries.BlockEntityRegistry;
import com.nihilhalla.drinkbeer.registries.FluidRegistry;
import com.nihilhalla.drinkbeer.registries.ItemRegistry;
import com.nihilhalla.drinkbeer.registries.RecipeRegistry;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidTankAnimated;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import com.nihilhalla.drinkbeer.blockentities.InventoryBlockEntity;
import slimeknights.mantle.fluid.FluidTransferHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WhiskyStillBlockEntity extends InventoryBlockEntity implements IBrewingInventory {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private NonNullList<FluidStack> fluids = NonNullList.withSize(1, FluidStack.EMPTY);
    // This int will not only indicate remainingBrewTime, but also represent Standard Brewing Time if valid in "waiting for ingredients" stage
    private int remainingBrewTime;
    private FluidStack outSpiritPour = FluidStack.EMPTY;
    private Player playerEntity;
    private CompoundTag inFluidTag = new CompoundTag();
    private CompoundTag outFluidTag = new CompoundTag();
    private CompoundTag spiritRecipeTag = new CompoundTag();
    //private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new CompositeFluidHandler(inFluidTank, outFluidTank));
    @Getter
    protected final FluidTankAnimated outFluidTank = new FluidTankAnimated(1000, this);
    @Getter
    protected final FluidTankAnimated inFluidTank = new FluidTankAnimated(2000, this);

    private final LazyOptional<IFluidHandler> outFluidTankHolder = LazyOptional.of(() -> outFluidTank);
    private final LazyOptional<IFluidHandler> inFluidTankHolder = LazyOptional.of(() -> inFluidTank);
    //public static Player player;
    // 0 - waiting for ingredient, 1 - brewing, 2 - waiting for pickup product

    private int statusCode;
    public FluidTransferHelper fluidUtil;

    public final ContainerData syncData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return remainingBrewTime;
                case 1:
                    return statusCode;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    remainingBrewTime = value;
                    break;
                case 1:
                    statusCode = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public WhiskyStillBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WHISKY_STILL_TILEENTITY.get(), pos, state, Component.translatable("block.drinkbeer.whisky_still"), false, 64);
    }
    
    @Override
    public WhiskyStillContainer createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WhiskyStillContainer(pContainerId, pPlayerInventory, this);
    }


    public void tickServer() {
        // waiting for ingredient
        if ((items.get(0).getItem() == ItemRegistry.EMPTY_WHISKY_GLASS.get() || items.get(0).getItem() == ItemRegistry.EMPTY_WINE_GLASS.get()) && outFluidTank.getFluidAmount() >= 250) {
            int amountServed = (outFluidTank.getFluidAmount() / 250);
            //DrinkBeer.LOG.atDebug().log("There should be this many cups worth of fluid: " + amountServed);
            //DrinkBeer.LOG.atDebug().log("Fluid: " + fluidTank.getFluid().getFluid().getRegistryName().toString() + ", Amount: " + fluidTank.getFluidAmount());
            //DrinkBeer.LOG.atDebug().log("Water: " + waterTank.getFluidAmount());
            if (amountServed >= 1) {
                int amountPoured = Math.min(amountServed, items.get(0).getCount());
                //DrinkBeer.LOG.atDebug().log("We've made a beer!");
                if (items.get(0) != null) {
                    if (items.get(1) == ItemStack.EMPTY && items.get(0).getItem() == ItemRegistry.EMPTY_WHISKY_GLASS.get()
                && BeerListHandler.SpiritList().contains(outFluidTank.getFluid().getFluid())) {
                        items.set(1, new ItemStack(BeerListHandler.buildWhiskyGlassMap(outFluidTank.getFluid().getFluid()), amountPoured));
                        updateFluids(amountPoured);
                        //DrinkBeer.LOG.atDebug().log(items.get(5).toString());
                    } else if (items.get(1) == ItemStack.EMPTY && outFluidTank.getFluid().getFluid() == FluidRegistry.COGNAC.get()
                    && items.get(0).getItem() == ItemRegistry.EMPTY_WINE_GLASS.get()) {
                        items.set(1, new ItemStack(ItemRegistry.COGNAC_GLASS.get(), amountPoured));
                        updateFluids(amountPoured);
                    } else {
                        //Do nothing because we don't want to fill when there's not a valid item.
                    }
                }
            }
        }
        if (statusCode == 0) {
                //DrinkBeer.LOG.atDebug().log(recipe.getIngredients().toString());
                //do sweet fuck all because we actually just need this to do it's thing.
            // ingredient slots must have no empty slot
            // Try match Recipe
            //DrinkBeer.LOG.atDebug().log(inFluidTank.getFluid().getFluid().getRegistryName().toString());
            //DrinkBeer.LOG.atDebug().log(inFluidTank.getFluid().getAmount());
            DistillationRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_DISTILLATION.get(), this, this.level).orElse(null);
            if (recipe != null) {
                //DrinkBeer.LOG.atDebug().log("Recipe is: " + recipe.getResult().getFluid().getRegistryName().toString());
            } else if (recipe == null) {
                //DrinkBeer.LOG.atDebug().log("Recipe is: null");
            }
            //DrinkBeer.LOG.atDebug().log(level.getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_DISTILLATION.get(), this, this.level));
            if (canBrew(recipe) && (inFluidTank.getFluidAmount() >= recipe.getInput().getAmount()) && ((outFluidTank.getFluid().getFluid() == recipe.getResult().getFluid()) || (outFluidTank.getFluid().getFluid() == FluidStack.EMPTY.getFluid()))) {
            // Show Standard Brewing Time & Result
                startBrewing(recipe);
                setChanged();
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
                
                // Check Weather have enough cup.
            } 
        } else if (statusCode == 1) {
            // brewing
                    if (remainingBrewTime > 0) {
                        remainingBrewTime--;
                        //DrinkBeer.LOG.atDebug().log("We are timing a brew! Present time remaining is: " + remainingBrewTime + " and OutPour is: " + outPour.getFluid().getRegistryName().toString());
                    } else {
                        if (remainingBrewTime == 0) {
                            outFluidTank.fill(outSpiritPour, FluidAction.EXECUTE);
                            statusCode = 0;
                        }
                        // Prevent wired glitch such as remainingTime been set to one;
                        remainingBrewTime = 0;
                        // Enter Next Stage
                    } 
                    
                // Enter "waiting for pickup"
                    setChanged();
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        } else if (statusCode == 2 && items.get(0).getItem() == ItemRegistry.EMPTY_WHISKY_GLASS.get() ||
            items.get(0).getItem() == ItemRegistry.EMPTY_WINE_GLASS.get()) {
                    statusCode = 0;
                }
                // Error status reset
                else {
                    remainingBrewTime = 0;
                    statusCode = 0;
                    setChanged();
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
                }
                
    }
    private boolean canBrew(@Nullable DistillationRecipe recipe) {
        if (recipe != null) {
            //DrinkBeer.LOG.atDebug().log("Recipe is " + recipe);
            return recipe.matches(this, this.level);

        } else {
            return false;
        }
    }

    private boolean hasEnoughEmptyCap(BrewingRecipe recipe) {
        return recipe.isCupQualified(this);
    }

    private void updateFluids(int amountPoured) {
        items.get(0).shrink(amountPoured);
        outFluidTank.drain(250 * amountPoured, FluidAction.EXECUTE);
        setChanged();
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    private void startBrewing(DistillationRecipe recipe) {
        //DrinkBeer.LOG.atDebug().log("Starting a Brew!");
        // Consume Ingredient & Cup;
        for (int i = 0; i < 1; i++) {
            if (items.get(i) == null || items.get(i) == ItemStack.EMPTY) {
                //Do Fuck All, because we don't have an item in this slot.
            } else if (isBucket(items.get(i))) {
                items.set(i, Items.BUCKET.getDefaultInstance());
            } else {

                ItemStack ingred = items.get(i);
                ingred.shrink(1);

            }
            statusCode = 1;
        }
        inFluidTank.drain(recipe.getInput().getAmount(), FluidAction.EXECUTE);
        // Set Remaining Time;
        remainingBrewTime = recipe.getBrewingTime();


        
        outSpiritPour = recipe.getResult();
        setChanged();
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    private boolean isBucket(ItemStack itemStack) {
        return itemStack.getItem() instanceof MilkBucketItem;
    }

    private void clearPreview() {
        items.set(5, ItemStack.EMPTY);
        remainingBrewTime = 0;
        setChanged();
    }

    private void showPreview() {
        if (items.get(4).getItem() == ItemStack.EMPTY.getItem() && outFluidTank.getFluidAmount() >= 250) {

            items.set(5, new ItemStack(BeerListHandler.buildMugMap(outFluidTank.getFluid().getFluid()), 1));
            setChanged();
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            statusCode = 2; 
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> getIngredients() {
        NonNullList<ItemStack> sample = NonNullList.withSize(1, ItemStack.EMPTY);
        for (int i = 0; i < 1; i++) {
            sample.set(i, items.get(i).copy());
        }
        return sample;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        // Get the block's facing direction
        Direction facing = getBlockState().getValue(WhiskyStillBlock.FACING);

        // Determine the relative side to expose the capability
        Direction leftSide = facing.getClockWise(Axis.Y); // Change this to get the desired relative side'
        Direction rightSide = facing.getCounterClockWise(Axis.Y);

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (side == rightSide) {
                return inFluidTankHolder.cast();
            } else if (side == leftSide) {
                return outFluidTankHolder.cast();
            } else {
                return inFluidTankHolder.empty();
            }
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inFluidTankHolder.invalidate();
        this.outFluidTankHolder.invalidate();

    }

    @Nonnull
    @Override
    public ItemStack getCup() {
        return items.get(0).copy();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("statusCode", (short) this.statusCode);
        CompoundTag inputTag = new CompoundTag();
        CompoundTag outputTag = new CompoundTag();
        CompoundTag spiritRecipeTag = new CompoundTag();
        inFluidTank.writeToNBT(inputTag);
        tag.put("InputLevel", inputTag);
        outFluidTank.writeToNBT(outputTag);
        tag.put("OutputLevel", outputTag);
        outSpiritPour.writeToNBT(spiritRecipeTag);
        tag.put("Recipe", spiritRecipeTag);

    } 
    @Override
    public void load(@Nonnull CompoundTag tag) {
        if (tag != null) {
            super.load(tag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
            this.remainingBrewTime = tag.getShort("RemainingBrewTime");
            this.statusCode = tag.getShort("statusCode");
            CompoundTag inputTag = tag.getCompound("InputLevel");
            CompoundTag outputTag = tag.getCompound("OutputLevel");
            CompoundTag spiritRecipeTag = tag.getCompound("Recipe");
            outSpiritPour = FluidStack.loadFluidStackFromNBT(spiritRecipeTag);
            inFluidTank.readFromNBT(inputTag);
            outFluidTank.readFromNBT(outputTag);
        }
    }
    
    

    public Component getDisplayName() {
        return Component.translatable("block.drinkbeer.whisky_still");
    }


    public Component getDefaultName() {
        return Component.translatable("block.drinkbeer.whisky_still");
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
/*
    @Override
    protected boolean shouldSyncOnUpdate() {
        return true;
    }

    @Override
    public void saveSynced(CompoundTag tag) {
        super.saveSynced(tag);

        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("statusCode", (short) this.statusCode);
        CompoundTag inputTag = new CompoundTag();
        CompoundTag outputTag = new CompoundTag();
        CompoundTag spiritRecipeTag = new CompoundTag();
        inFluidTank.writeToNBT(inputTag);
        tag.put("InputLevel", inputTag);
        outFluidTank.writeToNBT(outputTag);
        tag.put("OutputLevel", outputTag);
        outPour.writeToNBT(spiritRecipeTag);
        tag.put("Recipe", spiritRecipeTag);
    }
*/
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("statusCode", (short) this.statusCode);
        inFluidTank.writeToNBT(inFluidTag);
        tag.put("InputLevel", inFluidTag);
        outFluidTank.writeToNBT(outFluidTag);
        tag.put("OutputLevel", outFluidTag);
        outSpiritPour.writeToNBT(spiritRecipeTag);
        tag.put("Recipe", spiritRecipeTag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
            this.remainingBrewTime = tag.getShort("RemainingBrewTime");
            CompoundTag inFluidTag = tag.getCompound("InputLevel");
            CompoundTag outFluidTag = tag.getCompound("OutputLevel");
            CompoundTag spiritRecipeTag = tag.getCompound("Recipe");
            outSpiritPour = FluidStack.loadFluidStackFromNBT(spiritRecipeTag);
            inFluidTank.readFromNBT(inFluidTag);
            outFluidTank.readFromNBT(outFluidTag);
        }
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return p_70301_1_ >= 0 && p_70301_1_ < this.items.size() ? this.items.get(p_70301_1_) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return ContainerHelper.removeItem(this.items, p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ContainerHelper.takeItem(this.items, p_70304_1_);
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ >= 0 && p_70299_1_ < this.items.size()) {
            this.items.set(p_70299_1_, p_70299_2_);
        }
    }

/*
    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }
 */
    @Override
    public void clearContent() {
        this.items.clear();
    }

    //Fluid Handling for the Barrel
/*
    public static FluidTank getTank(int index) {
        if (index == 0) {
            return inFluidTank;
        } else if (index == 1) {
            return outFluidTank;
        } else {
        return null;
        }
    }
*/

    public boolean isFluidSpirit(FluidStack fluid) {
        if (fluid.getFluid() != null) {                
            if (BeerListHandler.spirits.contains(fluid.getFluid())) {
                return true;
            }
            return false;
        } 
        return false;
    }

    @Override
    public FluidStack getFluidIngredient() {
        return inFluidTank.getFluid();
    }

    @Override
    public FluidStack assemble() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }


}



