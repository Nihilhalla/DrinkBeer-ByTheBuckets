package com.nihilhalla.drinkbeer.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.handlers.BeerListHandler;
import com.nihilhalla.drinkbeer.registries.ItemRegistry;
import com.nihilhalla.drinkbeer.registries.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.recipe.ICustomOutputRecipe;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.recipe.ingredient.FluidIngredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DistillationRecipe implements ICustomOutputRecipe<IBrewingInventory> {
    private final ResourceLocation id;
    private final FluidStack input;
    private final int brewingTime;
    private final FluidStack result;


    public DistillationRecipe (ResourceLocation id, FluidStack input, int brewingTime, FluidStack result) {
        /*if(fluid == null) {
            DrinkBeer.LOG.atError().log("FluidIngredient is null in recipe: {}", id);
        }*/
        //DrinkBeer.LOG.atDebug().log(fluid.copy().getDisplayName().toString());
        this.id = id;
        this.input = input;
        this.brewingTime = brewingTime;
        this.result = result;
    }

    @Deprecated
    public NonNullList<FluidStack> getIngredient(){
        NonNullList<FluidStack> result = NonNullList.create();
        result.add(input);
        return result;
    }

    public NonNullList<FluidStack> getFluidIngredients(){
        NonNullList<FluidStack> result = NonNullList.create();
        result.add(input);
        //DrinkBeer.LOG.atDebug().log(result.toString());
        return result;
    }


/*
    @Deprecated
    public ItemStack geBeerCup(){
        return cup.copy();
    }

    public FluidIngredient getFluidIsIngredient(){
        return fluid.of(result);
    }
 */
@Override
public boolean matches(IBrewingInventory brewInv, Level level) {
    FluidStack recipeList = input;
    FluidStack inputFluid = brewInv.getFluidIngredient();
    return inputFluid.equals(recipeList);
}

private int getLatestMatched(List<FluidStack> recipeList, FluidStack invItem) {
    for (int i = 0; i < recipeList.size(); i++) {
        if (recipeList.get(i).getFluid().isSame(invItem.getFluid())) return i;
    }
    return -1;
}

    /**
     * Returns an Item that is the result of this recipe
     */

    // Can Craft at any dimension
    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }


    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book).
     * If your recipe has more than one possible result (e.g. it's dynamic and depends on its inputs),
     * then return an empty stack.
     */

    public FluidStack getResult() {
        //For Safety, I use #copy
        return result.copy();
    }

    public FluidStack getInput() {
        //For Safety, I use #copy
        return input.copy();
    }

    public ItemStack getCupResult() {
        FluidStack fluidResult = getResult();
        return new ItemStack(BeerListHandler.buildWhiskyGlassMap(fluidResult.getFluid())).copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RECIPE_SERIALIZER_DISTILLATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.RECIPE_TYPE_DISTILLATION.get();
    }
/*
    public int getRequiredCupCount() {
        return cup.getCount();
    }
*/
    public boolean isCupQualified(IBrewingInventory inventory) {
        return inventory.getCup().getItem() == ItemRegistry.EMPTY_WHISKY_GLASS.get() || inventory.getCup().getItem() == ItemRegistry.EMPTY_WINE_GLASS.get();
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DistillationRecipe> {

        @Override
        public DistillationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidStack input = FluidIngredient.deserialize(json, "input").getFluids().get(0);
            FluidStack result = FluidIngredient.deserialize(json, "result").getFluids().get(0);
            int brewingTime = GsonHelper.getAsInt(json, "brewing_time");
/*
            DrinkBeer.LOG.atDebug().log("Boss, Input is: " + input.getFluid().getRegistryName().toString());
            DrinkBeer.LOG.atDebug().log("Boss, Result is: " + result.getFluid().getRegistryName().toString());
            DrinkBeer.LOG.atDebug().log("Boss, BrewingTime is: " + brewingTime);
*/

            return new DistillationRecipe(recipeId, input, brewingTime, result);
        }

        @Override
        public DistillationRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidStack input = buffer.readFluidStack();
            FluidStack result = buffer.readFluidStack();
            int brewingTime = buffer.readInt();

            return new DistillationRecipe(recipeId, input, brewingTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DistillationRecipe recipe) {
            buffer.writeFluidStack(recipe.getInput());
            buffer.writeFluidStack(recipe.getResult());
            buffer.writeInt(recipe.getBrewingTime());
        }
    }
}
/*
        @Override
        public DistillationRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            FluidStack ingredients = fluidIngredientFromJson(jsonObject);
            FluidStack fluid2 =  fluidFromJson(jsonObject);
            int brewing_time = GsonHelper.getAsInt(jsonObject, "brewing_time");
            FluidStack result = fluidResultFromJson(jsonObject);
            return new DistillationRecipe(resourceLocation, ingredients, fluid2, brewing_time, result);
        }

        private static NonNullList<FluidIngredient> fluidsFromJson(JsonArray jsonArray) {
            NonNullList<FluidIngredient> ingredients = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                FluidIngredient ingredient = 
                ingredients.add(ingredient);
            }
            return ingredients;
        }

        private static FluidStack fluidFromJson(JsonObject jsonObj) {
            FluidIngredient fluid1 = FluidIngredient.deserialize(jsonObj, "fluid");
            if (fluid1.getFluids().isEmpty()) {
                return FluidStack.EMPTY;
            }
            return fluid1.getFluids().get(0);
        }
        private static FluidStack fluidIngredientFromJson(JsonObject jsonObj) {
            FluidIngredient fluid1 = FluidIngredient.deserialize(jsonObj, "ingredients");
            if (fluid1.getFluids().isEmpty()) {
                return FluidStack.EMPTY;
            }
            return fluid1.getFluids().get(0);
        }
        private static FluidStack fluidResultFromJson(JsonObject jsonObj) {
            FluidIngredient fluidlist = FluidIngredient.deserialize(jsonObj, "result");
            //DrinkBeer.LOG.atDebug().log(fluidlist.getFluids().get(0).toString());
            if (fluidlist.getFluids().isEmpty()) {
                return FluidStack.EMPTY;
            }
            return fluidlist.getFluids().get(0);
        }

        @Nullable
        @Override
        public DistillationRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf packetBuffer) {
            int i = packetBuffer.readVarInt();
            FluidStack ingredients = packetBuffer.readFluidStack();
            //    DrinkBeer.LOG.atDebug().log("this was in the packet buffer for the network " + packetBuffer);
            FluidStack fluid = packetBuffer.readFluidStack();
            int brewingTime = packetBuffer.readVarInt();
            FluidStack result = packetBuffer.readFluidStack();
            //DrinkBeer.LOG.atDebug().log("We found these values from the packets boss " + resourceLocation.toString(), ingredients.toString(), fluid.getFluid().toString(), brewingTime, result.getFluid().toString());
            return new DistillationRecipe(resourceLocation, ingredients, fluid, brewingTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf packetBuffer, DistillationRecipe distillationRecipe) {
            packetBuffer.writeFluidStack(distillationRecipe.input);
            packetBuffer.writeFluidStack(distillationRecipe.fluid);
            packetBuffer.writeVarInt(distillationRecipe.brewingTime);
            packetBuffer.writeFluidStack(distillationRecipe.result);

        } */
/* 
        private void RecipeException(String string) throws JsonException {
            throw new JsonException(string);
        }
    }


}
*/