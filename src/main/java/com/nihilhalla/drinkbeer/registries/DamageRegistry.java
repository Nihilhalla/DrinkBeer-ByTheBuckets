// DamageRegistry.java
package com.nihilhalla.drinkbeer.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.nihilhalla.drinkbeer.DrinkBeer;

public class DamageRegistry {
    public static final ResourceKey<DamageType> ALCOHOL_DAMAGE =
        ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(DrinkBeer.MOD_ID, "alcohol_damage"));

    public static DamageSource alcohol(ServerLevel level) {
        return new DamageSource(level.registryAccess()
            .registryOrThrow(Registries.DAMAGE_TYPE)
            .getHolderOrThrow(ALCOHOL_DAMAGE));
    }
}