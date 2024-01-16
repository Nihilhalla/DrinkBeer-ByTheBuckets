package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo;

import lombok.Getter;
import lombok.Setter;
import com.nihilhalla.drinkbeer.blockentities.MantleBlockEntity;

public class FluidTankAnimated extends FluidTankBase<MantleBlockEntity> {
  @Getter @Setter
  private float renderOffset;

  public FluidTankAnimated(int capacity, MantleBlockEntity parent) {
    super(capacity, parent);
  }
}