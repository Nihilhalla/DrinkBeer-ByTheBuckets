package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.entity;

import net.minecraft.world.entity.LivingEntity;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry.IGenericLoader;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.AndJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.IJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.InvertedJsonPredicate;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.NestedJsonPredicateLoader;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.predicate.OrJsonPredicate;

import static com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.data.GenericLoaderRegistry.SingletonLoader.singleton;

/** Predicate matching an entity */
public interface LivingEntityPredicate extends IJsonPredicate<LivingEntity> {
  /** Predicate that matches all entities */
  LivingEntityPredicate ANY = singleton(loader -> new LivingEntityPredicate() {
    @Override
    public boolean matches(LivingEntity input) {
      return true;
    }

    @Override
    public IGenericLoader<? extends IJsonPredicate<LivingEntity>> getLoader() {
      return loader;
    }
  });

  /** Loader for block state predicates */
  GenericLoaderRegistry<IJsonPredicate<LivingEntity>> LOADER = new GenericLoaderRegistry<>(ANY, true);
  /** Loader for inverted conditions */
  InvertedJsonPredicate.Loader<LivingEntity> INVERTED = new InvertedJsonPredicate.Loader<>(LOADER);
  /** Loader for and conditions */
  NestedJsonPredicateLoader<LivingEntity,AndJsonPredicate<LivingEntity>> AND = AndJsonPredicate.createLoader(LOADER, INVERTED);
  /** Loader for or conditions */
  NestedJsonPredicateLoader<LivingEntity,OrJsonPredicate<LivingEntity>> OR = OrJsonPredicate.createLoader(LOADER, INVERTED);

  /** Gets an inverted condition */
  @Override
  default IJsonPredicate<LivingEntity> inverted() {
    return INVERTED.create(this);
  }

  /* Singletons */

  /** Predicate that matches water sensitive entities */
  LivingEntityPredicate WATER_SENSITIVE = singleton(loader -> new LivingEntityPredicate() {
    @Override
    public boolean matches(LivingEntity input) {
      return input.isSensitiveToWater();
    }

    @Override
    public IGenericLoader<? extends IJsonPredicate<LivingEntity>> getLoader() {
      return loader;
    }
  });

  /** Predicate that matches fire immune entities */
  LivingEntityPredicate FIRE_IMMUNE = singleton(loader -> new LivingEntityPredicate() {
    @Override
    public boolean matches(LivingEntity input) {
      return input.fireImmune();
    }

    @Override
    public IGenericLoader<? extends IJsonPredicate<LivingEntity>> getLoader() {
      return loader;
    }
  });

  /** Predicate that matches fire immune entities */
  LivingEntityPredicate ON_FIRE = singleton(loader -> new LivingEntityPredicate() {
    @Override
    public boolean matches(LivingEntity input) {
      return input.isOnFire();
    }

    @Override
    public IGenericLoader<? extends IJsonPredicate<LivingEntity>> getLoader() {
      return loader;
    }
  });
}
