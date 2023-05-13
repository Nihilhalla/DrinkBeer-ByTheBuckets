package lekavar.lma.drinkbeer.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lekavar.lma.drinkbeer.items.Whisky.EmptyWhiskyGlassItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

@AllArgsConstructor
public class EmptyWhiskyGlassHandler implements IFluidHandlerItem, ICapabilityProvider {
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    //The entire code page is shamelessly borrowed from Tinker's Copper Can Item and modified to fit this mod.
    //Thanks go to mDiyo and the SlimeKnights Team.


    @Getter
    private ItemStack container;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(cap, holder);
    }


    /* Tank properties */

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        if (BeerListHandler.SpiritList().contains(stack.getFluid())) return true;
        return false;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 250;
    }

    /** Gets the contained fluid */
    private Fluid getFluid() {
        return EmptyWhiskyGlassItem.getFluid(container);
    }

    /** Gets the contained fluid */
    @Nullable
    private CompoundTag getFluidTag() {
        return EmptyWhiskyGlassItem.getFluidTag(container);
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(getFluid(), 250, getFluidTag());
    }


    /* Interaction */

    @Override
    public int fill(FluidStack resource, FluidAction action) {
    // must not be filled, must have enough
    if (getFluid() != Fluids.EMPTY || resource.getAmount() < 250 || !BeerListHandler.SpiritList().contains(resource.getFluid())) {
        return 0;
    }
    // update fluid and return
    if (action.execute() && BeerListHandler.SpiritList().contains(resource.getFluid())) {
        ItemStack newWhiskyGlass = new ItemStack(BeerListHandler.buildWhiskyGlassMap(resource.getFluid()));
        container = newWhiskyGlass;
    }
        return 250;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
    // must be draining at least a mug
    if (resource.isEmpty() || resource.getAmount() < 250) {
        return FluidStack.EMPTY;
    }
    // must have a fluid, must match what they are draining
    Fluid fluid = getFluid();
    if (fluid == Fluids.EMPTY || fluid != resource.getFluid()) {
        return FluidStack.EMPTY;
    }
    // output 1 mug
    FluidStack output = new FluidStack(fluid, 250, getFluidTag());
    if (action.execute()) {
        EmptyWhiskyGlassItem.setFluid(container, FluidStack.EMPTY);
    }
        return output;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
    // must be draining at least a mug
    if (maxDrain < 250) {
        return FluidStack.EMPTY;
    }
    // must have a fluid
    Fluid fluid = getFluid();
    if (fluid == Fluids.EMPTY) {
        return FluidStack.EMPTY;
    }
    // output 1 mug
    FluidStack output = new FluidStack(fluid, 250, getFluidTag());
    if (action.execute()) {
        EmptyWhiskyGlassItem.setFluid(container, FluidStack.EMPTY);
    }
        return output;
    }
}