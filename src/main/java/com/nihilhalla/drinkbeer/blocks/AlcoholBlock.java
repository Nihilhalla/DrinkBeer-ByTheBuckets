package com.nihilhalla.drinkbeer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class AlcoholBlock extends LiquidBlock {
	public AlcoholBlock(Supplier<? extends FlowingFluid> supplier, Properties props) {
		super(supplier, props);
	}
	public static Function<Supplier<? extends FlowingFluid>, LiquidBlock> createBurning(MapColor color, int lightLevel) {
		return fluid -> new AlcoholBlock(fluid, FluidDeferredRegister.createProperties(color, lightLevel));
		}
}
