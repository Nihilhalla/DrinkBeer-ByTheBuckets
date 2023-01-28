package lekavar.lma.drinkbeer.blockentities;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.gui.BeerBarrelContainer;
import lekavar.lma.drinkbeer.handlers.BeerFluidHandler;
import lekavar.lma.drinkbeer.handlers.BeerListHandler;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.recipes.IBrewingInventory;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.registries.FluidRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.DynamicBucketModel.Loader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.fluid.transfer.FillFluidContainerTransfer;
import slimeknights.mantle.recipe.ingredient.FluidContainerIngredient;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.mantle.util.JsonHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import java.lang.reflect.Array;
import java.util.List;

public class BeerBarrelBlockEntity extends BaseContainerBlockEntity implements IBrewingInventory, EntityBlock, IFluidHandler {
    private NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    // This int will not only indicate remainingBrewTime, but also represent Standard Brewing Time if valid in "waiting for ingredients" stage
    private int remainingBrewTime;
    private FluidTank fluidTank = new FluidTank(FluidAttributes.BUCKET_VOLUME);
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

    public BeerBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BEER_BARREL_TILEENTITY.get(), pos, state);
    }

    public void tickServer() {
        // waiting for ingredient
        if (statusCode == 0) {
            // ingredient slots must have no empty slot
            if (!getIngredients().contains(ItemStack.EMPTY)) {
                // Try match Recipe
                BrewingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_BREWING.get(), this, this.level).orElse(null);
                if (canBrew(recipe)) {
                    // Show Standard Brewing Time & Result
                    showPreview(recipe);
                    // Check Weather have enough cup.
                    if (hasEnoughEmptyCap(recipe)) {
                        startBrewing(recipe);

                    }
                }
                // Time remainingBrewTime will be reset since it also represents Standard Brewing Time if valid in this stage
                else {
                    clearPreview();
                }
            } else {
                clearPreview();
            }
        }
        // brewing
        else if (statusCode == 1) {
            if (remainingBrewTime > 0) {
                remainingBrewTime--;
            }
            // Enter "waiting for pickup"
            else {
                // Prevent wired glitch such as remainingTime been set to one;
                remainingBrewTime = 0;
                // Enter Next Stage
                statusCode = 2;
            }
            setChanged();
        }
        // waiting for pickup
        else if (statusCode == 2) {
            // Reset Stage to 0 (waiting for ingredients) after pickup Item
            if (items.get(5).isEmpty()) {
                statusCode = 0;
                setChanged();
            }
        }
        // Error status reset
        else {
            remainingBrewTime = 0;
            statusCode = 0;
            setChanged();
        }
    }


    private boolean canBrew(@Nullable BrewingRecipe recipe) {
        if (recipe != null) {
            return recipe.matches(this, this.level);
        } else {
            return false;
        }
    }

    private boolean hasEnoughEmptyCap(BrewingRecipe recipe) {
        return recipe.isCupQualified(this);
    }



    private void startBrewing(BrewingRecipe recipe) {
        // Pre-set beer to output Slot
        // This Step must be done first
        items.set(5, recipe.assemble(this));
        // Consume Ingredient & Cup;
        for (int i = 0; i < 4; i++) {
            ItemStack ingred = items.get(i);
            if (isBucket(ingred) && ingred.is(Items.WATER_BUCKET)) {
                //Check the Fluid in the bucket, empty the bucket, and fill the tank.
                items.set(i, Items.BUCKET.getDefaultInstance());
            }
            else ingred.shrink(1);
        }
        items.get(4).shrink(recipe.getRequiredCupCount());
        // Set Remaining Time;
        remainingBrewTime = recipe.getBrewingTime();
        // Change Status Code to 1 (brewing)
        statusCode = 1;

        setChanged();
    }

    private boolean isBucket(ItemStack itemStack) {
        return itemStack.getItem() instanceof BucketItem || itemStack.getItem() instanceof MilkBucketItem;
    }

    private void clearPreview() {
        items.set(5, ItemStack.EMPTY);
        remainingBrewTime = 0;
        setChanged();
    }

    private void showPreview(BrewingRecipe recipe) {
        items.set(5, recipe.assemble(this));
        remainingBrewTime = recipe.getBrewingTime();
        setChanged();
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
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.remainingBrewTime = tag.getShort("RemainingBrewTime");
        this.statusCode = tag.getShort("statusCode");
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.drinkbeer.beer_barrel");
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.drinkbeer.beer_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BeerBarrelContainer(id, , inventory, player);
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return null;
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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, this.items);
        this.remainingBrewTime = tag.getShort("RemainingBrewTime");
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

    @Override
    public int getMaxStackSize() {
        return IBrewingInventory.super.getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player p_70300_1_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(p_70300_1_.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BeerBarrelBlockEntity(pPos, pState);
    }
    //Fluid Handling for the Barrel




    public static FluidStack waterBucketFill = new FluidStack(Fluids.WATER, 1000);
    public FluidStack boozeBucketFill (ItemStack stack) {
        if (stack.is(DrinkBeer.BOOZE_BUCKET)){
            return new FluidStack(BeerListHandler.BucketConverter(stack), 1000);
        }
        return null;
    }


    public boolean isFluidBeer(FluidStack fluid) {
        if (fluid.getFluid() != null) {                
            if (BeerListHandler.acceptedFluids.contains(fluid.getFluid())) {
                return true;
            }
            return false;
        } 
        return false;
    }
    
    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidTank.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        if (isFluidBeer(stack)){
            if(stack == getFluidInTank(tank) || getFluidInTank(tank) == FluidStack.EMPTY){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidTank.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return fluidTank.drain(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return fluidTank.drain(maxDrain, action);
    }
}