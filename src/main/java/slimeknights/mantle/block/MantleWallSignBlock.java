package slimeknights.mantle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import slimeknights.mantle.block.entity.MantleSignBlockEntity;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MantleWallSignBlock extends WallSignBlock {
  public MantleWallSignBlock(Properties props, WoodType type) {
    super(props, type);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new MantleSignBlockEntity(pPos, pState);
  }
}
