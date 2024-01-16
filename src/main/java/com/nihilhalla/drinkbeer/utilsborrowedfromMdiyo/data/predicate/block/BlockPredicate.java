package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.block;

import net.minecraft.world.level.block.state.BlockState;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry.IGenericLoader;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.AndJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.IJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.InvertedJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.NestedJsonPredicateLoader;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.OrJsonPredicate;

import static com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry.SingletonLoader.singleton;

/**
 * Simple serializable block predicate
 */
public interface BlockPredicate extends IJsonPredicate<BlockState> {
  /** Loader for block state predicates */
  GenericLoaderRegistry<IJsonPredicate<BlockState>> LOADER = new GenericLoaderRegistry<>(true);
  /** Loader for inverted conditions */
  InvertedJsonPredicate.Loader<BlockState> INVERTED = new InvertedJsonPredicate.Loader<>(LOADER);
  /** Loader for and conditions */
  NestedJsonPredicateLoader<BlockState,AndJsonPredicate<BlockState>> AND = AndJsonPredicate.createLoader(LOADER, INVERTED);
  /** Loader for or conditions */
  NestedJsonPredicateLoader<BlockState,OrJsonPredicate<BlockState>> OR = OrJsonPredicate.createLoader(LOADER, INVERTED);

  /** Gets an inverted condition */
  @Override
  default IJsonPredicate<BlockState> inverted() {
    return INVERTED.create(this);
  }


  /* Singleton */

  /** Predicate that matches blocks with no harvest tool */
  BlockPredicate REQUIRES_TOOL = singleton(loader -> new BlockPredicate() {
    @Override
    public boolean matches(BlockState input) {
      return input.requiresCorrectToolForDrops();
    }

    @Override
    public IGenericLoader<? extends IJsonPredicate<BlockState>> getLoader() {
      return loader;
    }
  });
}
