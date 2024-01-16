package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.recipe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;


import static com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.registration.RegistrationHelper.injected;

import com.nihilhalla.drinkbeer.DrinkBeer;

/**
 * All recipe serializers registered under Mantles name
 */
@ObjectHolder(DrinkBeer.MOD_ID)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MantleRecipeSerializers {
  public static final RecipeSerializer<?> CRAFTING_SHAPED_FALLBACK = injected();
  public static final RecipeSerializer<?> CRAFTING_SHAPED_RETEXTURED = injected();
}
