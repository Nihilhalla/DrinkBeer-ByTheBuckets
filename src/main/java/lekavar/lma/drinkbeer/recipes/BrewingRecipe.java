package lekavar.lma.drinkbeer.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.handlers.BeerListHandler;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
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
import slimeknights.mantle.recipe.ICustomOutputRecipe;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BrewingRecipe implements ICustomOutputRecipe<IBrewingInventory> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> input;
    private final int brewingTime;
    private final FluidStack result;
    private final FluidStack fluid;


    public BrewingRecipe(ResourceLocation id, NonNullList<Ingredient> input, FluidStack fluid, int brewingTime, FluidStack result) {
        /*if(fluid == null) {
            DrinkBeer.LOG.atError().log("FluidIngredient is null in recipe: {}", id);
        }*/
        //DrinkBeer.LOG.atDebug().log(fluid.copy().getDisplayName().toString());
        this.id = id;
        this.input = input;
        this.brewingTime = brewingTime;
        this.result = result;
        this.fluid = fluid;
    }

    @Deprecated
    public NonNullList<Ingredient> getIngredient(){
        NonNullList<Ingredient> result = NonNullList.create();
        result.addAll(input);
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> result = NonNullList.create();
        result.addAll(input);
        //DrinkBeer.LOG.atDebug().log(result.toString());
        return result;
    }

    public NonNullList<ItemStack> getIngredientsItems(){
        NonNullList<ItemStack> result = NonNullList.create();
            for (Ingredient ingredient : input) {
                    result.add(ingredient.getItems()[0].copy());
                }
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
    List<Ingredient> recipeList = Lists.newArrayList(input);
    List<ItemStack> invItems = brewInv.getIngredients();
    List<ItemStack> iterableListItems = Lists.newArrayList(invItems);
    Consumer<ItemStack> removeEmpty = itemStack -> {
        if (itemStack == ItemStack.EMPTY) {
            iterableListItems.remove(itemStack);
        }
    };
    for (int i = iterableListItems.size() - 1; i >= 0; i--) {
        if (iterableListItems.get(i) == ItemStack.EMPTY) {
            iterableListItems.remove(i);
        }
    }
        //iterableListItems.forEach(removeEmpty);
    if (!iterableListItems.isEmpty()) {
        for (ItemStack itemStack : iterableListItems) {
            int j = getLatestMatched(recipeList, itemStack);
            if (j == -1) return false;
            else recipeList.remove(j);
            //DrinkBeer.LOG.atDebug().log();
        }
    }
    return recipeList.isEmpty();
}

private int getLatestMatched(List<Ingredient> recipeList, ItemStack invItem) {
    for (int i = 0; i < recipeList.size(); i++) {
        if (recipeList.get(i).test(invItem)) return i;
    }
    return -1;
}

    /**
     * Returns an Item that is the result of this recipe
     */

    public FluidStack getFluidIngredient() {
        return fluid.copy();
    }

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

    public ItemStack getCupResult() {
        FluidStack fluidResult = getResult();
        return new ItemStack(BeerListHandler.buildMugMap(fluidResult.getFluid())).copy();
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
        return RecipeRegistry.RECIPE_SERIALIZER_BREWING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.RECIPE_TYPE_BREWING.get();
    }
/*
    public int getRequiredCupCount() {
        return cup.getCount();
    }
*/
    public boolean isCupQualified(IBrewingInventory inventory) {
        return inventory.getCup().getItem() == ItemRegistry.EMPTY_BEER_MUG.get();
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BrewingRecipe> {

        
        @Override
        public BrewingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            FluidStack fluid2 =  fluidFromJson(jsonObject);
            int brewing_time = GsonHelper.getAsInt(jsonObject, "brewing_time");
            FluidStack result = fluidResultFromJson(jsonObject);
            return new BrewingRecipe(resourceLocation, ingredients, fluid2, brewing_time, result);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
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
        public BrewingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf packetBuffer) {
            int i = packetBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromNetwork(packetBuffer));
            //    DrinkBeer.LOG.atDebug().log("this was in the packet buffer for the network " + packetBuffer);
            }
            FluidStack fluid = packetBuffer.readFluidStack();
            int brewingTime = packetBuffer.readVarInt();
            FluidStack result = packetBuffer.readFluidStack();
            //DrinkBeer.LOG.atDebug().log("We found these values from the packets boss " + resourceLocation.toString(), ingredients.toString(), fluid.getFluid().toString(), brewingTime, result.getFluid().toString());
            return new BrewingRecipe(resourceLocation, ingredients, fluid, brewingTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf packetBuffer, BrewingRecipe brewingRecipe) {
            packetBuffer.writeVarInt(brewingRecipe.input.size());
            for (Ingredient ingredient : brewingRecipe.input) {
                ingredient.toNetwork(packetBuffer);
            }
            packetBuffer.writeFluidStack(brewingRecipe.fluid);
            packetBuffer.writeVarInt(brewingRecipe.brewingTime);
            packetBuffer.writeFluidStack(brewingRecipe.result);

        }
/* 
        private void RecipeException(String string) throws JsonException {
            throw new JsonException(string);
        }*/
    }


}
