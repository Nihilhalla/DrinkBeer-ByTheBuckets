package com.nihilhalla.drinkbeer.registries;

import com.nihilhalla.drinkbeer.recipes.BrewingRecipe;
import com.nihilhalla.drinkbeer.recipes.DistillationRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, "drinkbeer");
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "drinkbeer");
    public static final RegistryObject<RecipeSerializer<BrewingRecipe>> RECIPE_SERIALIZER_BREWING = RECIPE_SERIALIZERS.register("brewing", BrewingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<DistillationRecipe>> RECIPE_SERIALIZER_DISTILLATION = RECIPE_SERIALIZERS.register("distillation", DistillationRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<BrewingRecipe>> RECIPE_TYPE_BREWING = RECIPE_TYPES.register("brewing", () -> new RecipeType<BrewingRecipe>() {
        public String toString() {
            return "brewing";
        }
    });
    public static final RegistryObject<RecipeType<DistillationRecipe>> RECIPE_TYPE_DISTILLATION = RECIPE_TYPES.register("distillation", () -> new RecipeType<DistillationRecipe>() {
        public String toString() {
            return "distillation";
        }
    });




}
