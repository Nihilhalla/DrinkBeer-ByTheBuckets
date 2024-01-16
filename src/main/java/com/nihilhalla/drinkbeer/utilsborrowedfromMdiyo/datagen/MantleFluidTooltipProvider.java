package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fluids.FluidAttributes;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.fluid.tooltip.AbstractFluidTooltipProvider;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.fluid.tooltip.FluidTooltipHandler;

/** Mantle datagen for fluid tooltips. For mods, don't use this, use {@link AbstractFluidTooltipProvider} */
public class MantleFluidTooltipProvider extends AbstractFluidTooltipProvider {
  public MantleFluidTooltipProvider(DataGenerator generator) {
    super(generator, DrinkBeer.MOD_ID);
  }

  @Override
  protected void addFluids() {
    add("buckets")
      .addUnit("kilobucket", FluidAttributes.BUCKET_VOLUME * 1000)
      .addUnit("bucket", FluidAttributes.BUCKET_VOLUME);
    addRedirect(FluidTooltipHandler.DEFAULT_ID, id("buckets"));
  }

  @Override
  public String getName() {
    return "Mantle Fluid Tooltip Provider";
  }
}
