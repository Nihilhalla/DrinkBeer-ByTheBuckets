package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import com.nihilhalla.drinkbeer.blockentities.MantleSignBlockEntity;

import static com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.registration.RegistrationHelper.injected;

import com.nihilhalla.drinkbeer.DrinkBeer;

/**
 * Various objects registered under Mantle
 */
@ObjectHolder(DrinkBeer.MOD_ID)
public class MantleRegistrations {
  private MantleRegistrations() {}

  public static final BlockEntityType<MantleSignBlockEntity> SIGN = injected();
}
