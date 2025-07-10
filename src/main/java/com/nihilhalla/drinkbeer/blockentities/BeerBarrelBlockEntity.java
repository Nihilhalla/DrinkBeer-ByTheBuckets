package com.nihilhalla.drinkbeer.blockentities;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainer;
import com.nihilhalla.drinkbeer.handlers.BeerListHandler;
import com.nihilhalla.drinkbeer.recipes.BrewingRecipe;
import com.nihilhalla.drinkbeer.recipes.IBrewingInventory;
import com.nihilhalla.drinkbeer.registries.BlockEntityRegistry;
import com.nihilhalla.drinkbeer.registries.FluidRegistry;
import com.nihilhalla.drinkbeer.registries.ItemRegistry;
import com.nihilhalla.drinkbeer.registries.RecipeRegistry;
import com.nihilhalla.drinkbeer.utils.beer.Beers;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidTankAnimated;

import lombok.Getter;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.text.BreakIterator;
import java.util.List;

public class BeerBarrelBlockEntity extends InventoryBlockEntity implements IBrewingInventory {
    private NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    private NonNullList<FluidStack> fluids = NonNullList.withSize(1, FluidStack.EMPTY);
    // This int will not only indicate remainingBrewTime, but also represent Standard Brewing Time if valid in "waiting for ingredients" stage
    private int remainingBrewTime;
    private FluidStack outPour = FluidStack.EMPTY;
    private Player playerEntity;
    private CompoundTag waterTag = new CompoundTag();
    private CompoundTag fluidTag = new CompoundTag();
    private CompoundTag recipeTag = new CompoundTag();
    //private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new CompositeFluidHandler(waterTank, fluidTank));
    @Getter
    protected final FluidTankAnimated fluidTank = new FluidTankAnimated(5000, this);
    @Getter
    protected final FluidTankAnimated waterTank = new FluidTankAnimated(5000, this);

    private final LazyOptional<IFluidHandler> fluidTankHolder = LazyOptional.of(() -> fluidTank);
    private final LazyOptional<IFluidHandler> waterTankHolder = LazyOptional.of(() -> waterTank);
    //public static Player player;
    // 0 - waiting for ingredient, 1 - brewing, 2 - waiting for pickup product

    private int statusCode;
    public FluidUtil fluidUtil;

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
            return 2;
        }
    };

    public BeerBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BEER_BARREL_TILEENTITY.get(), pos, state, Component.translatable("block.drinkbeer.beer_barrel"), false, 64);
    }
    
    public BeerBarrelContainer createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BeerBarrelContainer(pContainerId, pPlayerInventory, this, syncData);
    }


    public void tickServer() {
        //showPreview();
        //DrinkBeer.LOG.atDebug().log(statusCode);
        //DrinkBeer.LOG.atDebug().log("Fluid: " + fluidTank.getFluidAmount());
        //DrinkBeer.LOG.atDebug().log("Water: " + waterTank.getFluidAmount());
        if (items.get(4).getItem() == ItemRegistry.EMPTY_BEER_MUG.get() && fluidTank.getFluidAmount() >= 250&& fluidTank.getFluid().getFluid() != FluidRegistry.WINE.get()) {
            int amountServed = (fluidTank.getFluidAmount() / 250);
            //DrinkBeer.LOG.atDebug().log("There should be this many cups worth of fluid: " + amountServed);
            //DrinkBeer.LOG.atDebug().log("Fluid: " + fluidTank.getFluid().getFluid().getRegistryName().toString() + ", Amount: " + fluidTank.getFluidAmount());
            //DrinkBeer.LOG.atDebug().log("Water: " + waterTank.getFluidAmount());
            if (amountServed >= 1) {
                int amountPoured = Math.min(amountServed, items.get(4).getCount());
                //DrinkBeer.LOG.atDebug().log("We've made a beer!");
                if (items.get(5) != null) {
                    if (items.get(5) == ItemStack.EMPTY) {
                        items.set(5, new ItemStack(BeerListHandler.buildMugMap(fluidTank.getFluid().getFluid()), amountPoured));
                        //DrinkBeer.LOG.atDebug().log(items.get(5).toString());
                    } else if (BeerListHandler.MugList().contains(items.get(5).getItem())) {
                        items.get(5).grow(amountPoured);
                    } else {

                    }
                }
                items.get(4).shrink(amountPoured);
                fluidTank.drain(250 * amountPoured, FluidAction.EXECUTE);
                setChanged();
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            }
        }
        if (items.get(4).getItem() == ItemRegistry.EMPTY_WINE_GLASS.get() && fluidTank.getFluidAmount() >= 250 && fluidTank.getFluid().getFluid() == FluidRegistry.WINE.get()) {
            int amountServed = (fluidTank.getFluidAmount() / 250);
            //DrinkBeer.LOG.atDebug().log("There should be this many cups worth of fluid: " + amountServed);
            //DrinkBeer.LOG.atDebug().log("Fluid: " + fluidTank.getFluid().getFluid().getRegistryName().toString() + ", Amount: " + fluidTank.getFluidAmount());
            //DrinkBeer.LOG.atDebug().log("Water: " + waterTank.getFluidAmount());
            if (amountServed >= 1) {
                int amountPoured = Math.min(amountServed, items.get(4).getCount());
                //DrinkBeer.LOG.atDebug().log("We've made a beer!");
                if (items.get(5) != null) {
                    if (items.get(5) == ItemStack.EMPTY) {
                        items.set(5, new ItemStack(ItemRegistry.WINE_GLASS.get(), amountPoured));
                        //DrinkBeer.LOG.atDebug().log(items.get(5).toString());
                    } else if (items.get(5).getItem() == ItemRegistry.WINE_GLASS.get()) {
                        items.get(5).grow(amountPoured);
                    } else {

                    }
                }
                items.get(4).shrink(amountPoured);
                fluidTank.drain(250 * amountPoured, FluidAction.EXECUTE);
                setChanged();
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            }
        }
        // waiting for ingredient
        if (statusCode == 0) {
            getIngredients();
                //DrinkBeer.LOG.atDebug().log(getIngredients().toString());
                //do sweet fuck all because we actually just need this to do it's thing.
            // ingredient slots must have no empty slot
            for (int i = 0; i < 4; i++) {
                if (items.get(i).getItem() == Items.WATER_BUCKET && waterTank.getFluidAmount() < 5000 && waterTank.getFluid().getFluid() == Fluids.WATER) {
                    //DrinkBeer.LOG.atDebug().log("We see a bucket!");
                    items.set(i, Items.BUCKET.getDefaultInstance());
                    waterTank.fill(waterBucketFill, FluidAction.EXECUTE);
                    //DrinkBeer.LOG.atDebug().log("Water level is now: " + waterTank.getFluidAmount());
                    setChanged();
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
                }
                if (items.get(i).getItem() == Items.LAVA_BUCKET && waterTank.getFluidAmount() < 5000 && waterTank.getFluid().getFluid() == Fluids.LAVA) {
                    //DrinkBeer.LOG.atDebug().log("We see a bucket!");
                    items.set(i, Items.BUCKET.getDefaultInstance());
                    waterTank.fill(lavaBucketFill, FluidAction.EXECUTE);
                    //DrinkBeer.LOG.atDebug().log("Lava level is now: " + waterTank.getFluidAmount());
                    setChanged();
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
                }
                setChanged();
                level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            }
            // Try match Recipe
            BrewingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_BREWING.get(), this, this.level).orElse(null);
            if (recipe != null) {
                //DrinkBeer.LOG.atDebug().log("Recipe is: " + recipe.getResult().getFluid().getRegistryName().toString());
            }
            //DrinkBeer.LOG.atDebug().log(recipe.getResult().getFluid().getRegistryName().toString());
            if (canBrew(recipe) && (waterTank.getFluidAmount() >= recipe.getResult().getAmount()) && ((fluidTank.getFluid().getFluid() == recipe.getResult().getFluid()) || (fluidTank.getFluid().getFluid() == FluidStack.EMPTY.getFluid()))) {
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
                            fluidTank.fill(outPour, FluidAction.EXECUTE);
                            statusCode = 0;
                        }
                        // Prevent wired glitch such as remainingTime been set to one;
                        remainingBrewTime = 0;
                        // Enter Next Stage
                    } 
                    
                // Enter "waiting for pickup"
                    setChanged();
                    level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        } else if (statusCode == 2 && items.get(4).getItem() == ItemRegistry.EMPTY_BEER_MUG.get()) {
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
    private boolean canBrew(@Nullable BrewingRecipe recipe) {
        if (recipe != null) {
            ResourceLocation id = recipe.getId();
            if (id.equals(new ResourceLocation(DrinkBeer.MOD_ID, "beer_mug_hellbrew")) && this.level.dimension() != Level.NETHER) {
                return false;
            }
            DrinkBeer.LOG.atDebug().log("Recipe ID: {}", recipe.getId());
            return recipe.matches(this, this.level);
        } else {
            return false;
        }
    }

    private boolean hasEnoughEmptyCap(BrewingRecipe recipe) {
        return recipe.isCupQualified(this);
    }



    private void startBrewing(BrewingRecipe recipe) {
        //DrinkBeer.LOG.atDebug().log("Starting a Brew!");
        // Consume Ingredient & Cup;
        for (int i = 0; i < 4; i++) {
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
        waterTank.drain(recipe.getResult().getAmount(), FluidAction.EXECUTE);
        // Set Remaining Time;
        remainingBrewTime = recipe.getBrewingTime();


        
        outPour = recipe.getResult();
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
        if (items.get(4).getItem() == ItemStack.EMPTY.getItem() && fluidTank.getFluidAmount() >= 250) {

            items.set(5, new ItemStack(BeerListHandler.buildMugMap(fluidTank.getFluid().getFluid()), 1));
            setChanged();
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            statusCode = 2; 
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> getIngredients() {
        NonNullList<ItemStack> sample = NonNullList.withSize(4, ItemStack.EMPTY);
        for (int i = 0; i < 4; i++) {
            sample.set(i, items.get(i).copy());
        }
        return sample;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.UP) {
                return waterTankHolder.cast();
            } else if (side != Direction.UP) {
                return fluidTankHolder.cast();
            }
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.waterTankHolder.invalidate();
        this.fluidTankHolder.invalidate();

    }

    @Nonnull
    @Override
    public ItemStack getCup() {
        return items.get(4).copy();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("statusCode", (short) this.statusCode);
        CompoundTag waterTag = new CompoundTag();
        CompoundTag fluidTag = new CompoundTag();
        CompoundTag recipeTag = new CompoundTag();
        waterTank.writeToNBT(waterTag);
        tag.put("WaterLevel", waterTag);
        fluidTank.writeToNBT(fluidTag);
        tag.put("FluidLevel", fluidTag);
        outPour.writeToNBT(recipeTag);
        tag.put("Recipe", recipeTag);

    } 
    @Override
    public void load(@Nonnull CompoundTag tag) {
        if (tag != null) {
            super.load(tag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
            this.remainingBrewTime = tag.getShort("RemainingBrewTime");
            this.statusCode = tag.getShort("statusCode");
            CompoundTag waterTag = tag.getCompound("WaterLevel");
            CompoundTag fluidTag = tag.getCompound("FluidLevel");
            CompoundTag recipeTag = tag.getCompound("Recipe");
            outPour = FluidStack.loadFluidStackFromNBT(recipeTag);
            waterTank.readFromNBT(waterTag);
            fluidTank.readFromNBT(fluidTag);
        }
    }
    
    

    public Component getDisplayName() {
        return Component.translatable("block.drinkbeer.beer_barrel");
    }


    public Component getDefaultName() {
        return Component.translatable("block.drinkbeer.beer_barrel");
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
        CompoundTag waterTag = new CompoundTag();
        CompoundTag fluidTag = new CompoundTag();
        CompoundTag recipeTag = new CompoundTag();
        waterTank.writeToNBT(waterTag);
        tag.put("WaterLevel", waterTag);
        fluidTank.writeToNBT(fluidTag);
        tag.put("FluidLevel", fluidTag);
        outPour.writeToNBT(recipeTag);
        tag.put("Recipe", recipeTag);
    }
*/
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("statusCode", (short) this.statusCode);
        waterTank.writeToNBT(waterTag);
        tag.put("WaterLevel", waterTag);
        fluidTank.writeToNBT(fluidTag);
        tag.put("FluidLevel", fluidTag);
        outPour.writeToNBT(recipeTag);
        tag.put("Recipe", recipeTag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items);
            this.remainingBrewTime = tag.getShort("RemainingBrewTime");
            CompoundTag waterTag = tag.getCompound("WaterLevel");
            CompoundTag fluidTag = tag.getCompound("FluidLevel");
            CompoundTag recipeTag = tag.getCompound("Recipe");
            outPour = FluidStack.loadFluidStackFromNBT(recipeTag);
            waterTank.readFromNBT(waterTag);
            fluidTank.readFromNBT(fluidTag);
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
            return waterTank;
        } else if (index == 1) {
            return fluidTank;
        } else {
        return null;
        }
    }
*/

    public static FluidStack waterBucketFill = new FluidStack(Fluids.WATER, 1000);
    public static FluidStack lavaBucketFill = new FluidStack(Fluids.LAVA, 1000);
    public FluidStack boozeBucketFill (ItemStack stack) {
        if (stack.is(DrinkBeer.BOOZE_BUCKET)){
            return new FluidStack(BeerListHandler.BucketConverter(stack), 1000);
        }
        return null;
    }


    public boolean isFluidBeer(FluidStack fluid) {
        if (fluid.getFluid() != null) {                
            if (BeerListHandler.beers.contains(fluid.getFluid())) {
                return true;
            }
            return false;
        } 
        return false;
    }

    @Override
    public FluidStack getFluidIngredient() {
        return waterTank.getFluid();
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



