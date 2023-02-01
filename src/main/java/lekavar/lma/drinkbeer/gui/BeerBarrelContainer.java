package lekavar.lma.drinkbeer.gui;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ContainerTypeRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import javax.swing.plaf.synth.SynthViewportUI;

import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import slimeknights.mantle.util.sync.LambdaDataSlot;

public class BeerBarrelContainer extends AbstractContainerMenu {
    private static final int STATUS_CODE = 1;
    private static final int BREWING_REMAINING_TIME = 0;
    private static BlockEntity blockEntity;
    private Player playerEntity;
    private IItemHandler playerInventory;
    private final ContainerData syncData;
    static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<IFluidHandler>() {});
    
    public boolean getIsBrewing() {
        return syncData.get(STATUS_CODE) == 1;
    }

    public int getStandardBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    public int getRemainingBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    public BeerBarrelContainer(int windowId, BlockPos pos, Inventory pInventory, Player player, ContainerData syncData) {
        super(ContainerTypeRegistry.BEER_BARREL_CONTAINER.get(), windowId);
        blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(pInventory);
        this.syncData = syncData;
        // Prepare
        if (blockEntity != null) {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 28, 26));
                addSlot(new SlotItemHandler(h, 1, 46, 26));
                addSlot(new SlotItemHandler(h, 2, 28, 44));
                addSlot(new SlotItemHandler(h, 3, 46, 44));
                addSlot(new OutputSlot(h, 5, 128, 34, syncData));
                addSlot(new SlotItemHandler(h, 4, 73, 50));
            });
            //blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(j -> {
            //});
        };
    
        layoutPlayerInventorySlots(8, 84);
        

        // Player Inventory

        // Input Ingredients

        // Empty Cup

        // Output

        //Tracking Data
        addDataSlots(syncData);
    }

    public BeerBarrelContainer(int id, Inventory playerInventory, FriendlyByteBuf data, Player player) {
        this(id, playerInventory, data.readBlockPos(), player);
    }

    public BeerBarrelContainer(int windowId, Inventory playerInventory, BlockPos pos, Player player) {
        this(windowId, pos, playerInventory, player, ((BeerBarrelBlockEntity) player.level.getBlockEntity(pos)).syncData);
    }

    private final void trackFluids() {
        //This function will be our workhorse for both the initial brewing of the beer, and the subsequent pouring and handling of it.
        addDataSlot(new LambdaDataSlot(0, null, null));
    }
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Try quick-pickup output
            if (p_82846_2_ == 41) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Try quick-move item in player inv.
            else if (p_82846_2_ < 36) {
                // Try to fill cup slot first.
                if (this.isEmptyCup(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 40, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Try to fill ingredient slot.
                if (!this.moveItemStackTo(itemstack1, 36, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Try quick-move item to player inv.
            else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_82846_1_, itemstack1);
        }

        return itemstack;
    }

    public boolean isEmptyCup(ItemStack itemStack) {
        return itemStack.getItem() == ItemRegistry.EMPTY_BEER_MUG.get();
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockRegistry.BEER_BARREL.get());
    }



    @Override
    public void removed(Player player) {
        if (!player.level.isClientSide()) {
            // Play Closing Barrel Sound
            player.level.playSound(player, player.blockPosition(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1f, 1f);
        }
        super.removed(player);
    }

    static class OutputSlot extends SlotItemHandler {
        private ContainerData syncData;


        public OutputSlot(IItemHandler p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ContainerData syncData) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.syncData = syncData;

        }

        // After player picking up product, play pour sound effect
        // statusCode reset is handled by TileEntity#tick
        @Override
        public void onTake(Player player, ItemStack p_190901_2_) {
            if (p_190901_2_.getItem() == ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get()) {
                player.level.playSound((Player) null, blockEntity.getBlockPos(), SoundEventRegistry.POURING_CHRISTMAS.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                //p_190901_1_.level.playSound(p_190901_1_, p_190901_1_.blockPosition(), SoundEventRegistry.POURING_CHRISTMAS_VER.get(), SoundCategory.BLOCKS, 1f, 1f);

            } else {
                player.level.playSound((Player) null, blockEntity.getBlockPos(), SoundEventRegistry.POURING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                //p_190901_1_.level.playSound(p_190901_1_, p_190901_1_.blockPosition(), SoundEventRegistry.POURING.get(), SoundCategory.BLOCKS, 1f, 1f);
                //}
            }
        }

        // Placing item on output slot is prohibited.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return false;
        }

        // Only when the statusCode is 2 (waiting for pickup), pickup is allowed.
        @Override
        public boolean mayPickup(Player p_82869_1_) {
            return true;
        }
    }
}
