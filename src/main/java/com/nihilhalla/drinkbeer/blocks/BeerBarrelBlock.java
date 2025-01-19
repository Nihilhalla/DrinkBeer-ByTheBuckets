package com.nihilhalla.drinkbeer.blocks;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.blockentities.BeerBarrelBlockEntity;
import com.nihilhalla.drinkbeer.blockentities.TradeBoxBlockEntity;
import com.nihilhalla.drinkbeer.items.Beer.BeerBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.RenderShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import slimeknights.mantle.block.InventoryBlock;
import slimeknights.mantle.network.NetworkWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.fluid.transfer.*;

import javax.annotation.Nullable;

public class BeerBarrelBlock extends InventoryBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static NetworkWrapper netWrap;
    protected static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 15, 15);

    public BeerBarrelBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        boolean hitBucket;
            if (FluidTransferHelper.interactWithTank(world, pos, player, hand, hit) && world.getBlockEntity(pos) != null) {
                world.getBlockEntity(pos).setChanged();
                world.sendBlockUpdated(pos, state, state, 2);
                hitBucket = true;
            } else {
                hitBucket = false;
            }
        //DrinkBeer.LOG.atDebug().log(world.getBlockEntity(pos).getTileData().getAllKeys().toArray().toString());
        //boolean fluidHandler = world.getBlockEntity(pos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().isPresent();
        //DrinkBeer.LOG.atDebug().log(fluidHandler);
        if (!world.isClientSide && !hitBucket && player instanceof ServerPlayer serverPlayer) {
            world.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1f, 1f);

            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BeerBarrelBlockEntity barrel) {
            	NetworkHooks.openScreen((ServerPlayer) player, (BeerBarrelBlockEntity) blockentity, (FriendlyByteBuf packerBuffer) -> {
                    packerBuffer.writeBlockPos(blockentity.getBlockPos());
                });
                DrinkBeer.LOG.atDebug().log("Found BlockEntity");
                //openGui((ServerPlayer) player, world, pos);
                //DrinkBeer.LOG.atDebug().log(state.toString());
                //DrinkBeer.LOG.atDebug().log(world.toString());
                //DrinkBeer.LOG.atDebug().log(pos.toString());
                //DrinkBeer.LOG.atDebug().log(this.getMenuProvider(state, world, pos));
                //DrinkBeer.LOG.atDebug().log(openGui(player, world, pos));
            } else {
                DrinkBeer.LOG.atDebug().log("didn't work, no block entity.");
            }
            return InteractionResult.CONSUME;

        }
        return InteractionResult.SUCCESS;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BeerBarrelBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntity) {
        if (level != null && level.isClientSide()) {
            return null;
        } else {
            return (theLevel, pos, state, tile) -> {
                if (tile instanceof BeerBarrelBlockEntity beerBarrelBlockEntity) {
                    beerBarrelBlockEntity.tickServer();
                }
            };
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

}
