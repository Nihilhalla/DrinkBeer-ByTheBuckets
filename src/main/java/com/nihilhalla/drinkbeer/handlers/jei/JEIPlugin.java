package com.nihilhalla.drinkbeer.handlers.jei;

import java.util.List;
import java.util.Optional;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainer;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainerScreen;
import com.nihilhalla.drinkbeer.recipes.BrewingRecipe;
import com.nihilhalla.drinkbeer.registries.BlockRegistry;
import com.nihilhalla.drinkbeer.registries.ContainerTypeRegistry;
import com.nihilhalla.drinkbeer.registries.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.recipe.helper.RecipeHelper;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final RecipeType<BrewingRecipe> BREWING = type("brewing", BrewingRecipe.class);
    


    @Override
    public ResourceLocation getPluginUid() {
        return null;
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIBrewingRecipe(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        net.minecraft.world.item.crafting.RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<BrewingRecipe> brewingRecipes = RecipeHelper.getJEIRecipes(manager, RecipeRegistry.RECIPE_TYPE_BREWING.get(), BrewingRecipe.class);

        registration.addRecipes(BREWING, brewingRecipes);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BeerBarrelRecipeTransferInfo());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.BEER_BARREL.get()), BREWING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(BeerBarrelContainerScreen.class, 96, 31, 22, 18, BREWING);
    }
    private static <T> RecipeType<T> type(String name, Class<T> clazz) {
        return RecipeType.create(DrinkBeer.MOD_ID, name, clazz);
    }


}
