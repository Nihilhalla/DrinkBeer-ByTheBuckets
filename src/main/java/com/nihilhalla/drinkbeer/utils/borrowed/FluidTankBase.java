package com.nihilhalla.drinkbeer.utils.borrowed;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class FluidTankBase<T extends BlockEntityType<BlockEntity>> extends FluidTank {

  protected T parent;

  public FluidTankBase(int capacity, T parent) {
    super(capacity);
    this.parent = parent;
  }

  @Override
  protected void onContentsChanged() {
    if (parent instanceof IFluidTankUpdater) {
      ((IFluidTankUpdater) parent).onTankContentsChanged();
    }

    parent.setChanged();
    Level level = parent.getLevel();
    if(level != null && !level.isClientSide) {
      DrinkerNetwork.getInstance().sendToClientsAround(new FluidUpdatePacket(parent.getBlockPos(), this.getFluid()), level, parent.getBlockPos());
    }
  }
}
