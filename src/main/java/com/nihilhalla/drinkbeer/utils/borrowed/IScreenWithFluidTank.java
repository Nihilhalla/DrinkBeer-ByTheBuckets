package com.nihilhalla.drinkbeer.utils.borrowed;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.Rect2i;
import net.minecraftforge.fluids.FluidStack;

/**
 * Interface for JEI support to determine the ingredient under the mouse
 */
public interface IScreenWithFluidTank {
  /**
   * Gets the ingredient under the mouse, typically a fluid
   * @param mouseX Mouse X
   * @param mouseY Mouse Y
   * @return Ingredient under mouse, or null if no ingredient. Does not need to handle item stacks
   */
	  @Nullable
	  FluidLocation getFluidUnderMouse(int mouseX, int mouseY);

	  /** Return from the fluid under mouse, maps to a JEI fluid */
	  record FluidLocation(FluidStack fluid, Rect2i location) {}
	}
