package com.nihilhalla.drinkbeer.utils.borrowed;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
//import slimeknights.mantle.block.entity.MantleBlockEntity;

public class FluidTankAnimated extends FluidTankBase<BlockEntityType<BlockEntity>> {
  @Getter @Setter
  private float renderOffset;

  public FluidTankAnimated(int capacity, BlockEntityType parent) {
    super(capacity, parent);
  }
}
