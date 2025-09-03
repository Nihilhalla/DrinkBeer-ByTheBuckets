/*package com.nihilhalla.drinkbeer.handlers;

import java.util.Arrays;
import java.util.List;

import com.nihilhalla.drinkbeer.blockentities.BeerBarrelBlockEntity;
import com.nihilhalla.drinkbeer.blocks.BeerBarrelBlock;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainer;
import com.nihilhalla.drinkbeer.registries.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import slimeknights.mantle.fluid.*;
import slimeknights.mantle.fluid.transfer.*;
import slimeknights.mantle.fluid.tooltip.*;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.fluid.tooltip.FluidUnitList;

public class CompositeFluidHandler implements IFluidHandler {
    private final List<FluidTank> tanks;

    public CompositeFluidHandler(FluidTank... tanks) {
        this.tanks = Arrays.asList(tanks);
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return tanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return tanks.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return tanks.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        for (FluidTank tank : tanks) {
            int filled = tank.fill(resource, action);
            if (filled > 0) {
                return filled;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for (FluidTank tank : tanks) {
            FluidStack drained = tank.drain(resource, action);
            if (drained != null) {
                return drained;
            }
            
        }
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        // TODO Auto-generated method stub
        return null;
    }
}*/