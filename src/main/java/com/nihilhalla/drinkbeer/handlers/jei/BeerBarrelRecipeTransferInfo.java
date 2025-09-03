package com.nihilhalla.drinkbeer.handlers.jei;

import java.util.List;
import java.util.Optional;

import com.nihilhalla.drinkbeer.gui.BeerBarrelContainer;
import com.nihilhalla.drinkbeer.recipes.BrewingRecipe;
import com.nihilhalla.drinkbeer.registries.ContainerTypeRegistry;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;


public class BeerBarrelRecipeTransferInfo implements IRecipeTransferInfo<BeerBarrelContainer, BrewingRecipe> {

    @Override
    public Class<BeerBarrelContainer> getContainerClass() {
        return BeerBarrelContainer.class;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return JEIPlugin.BREWING;
    }
    
    @Override
    public boolean canHandle(BeerBarrelContainer container, BrewingRecipe recipeType) {
        return true; // Add additional checks if necessary
    }
    
    @Override
    public Optional<MenuType<BeerBarrelContainer>> getMenuType() {
        return Optional.of(ContainerTypeRegistry.BEER_BARREL_CONTAINER.get());
    }

    @Override
    public List<Slot> getRecipeSlots(BeerBarrelContainer container, BrewingRecipe recipe) {
        return container.slots.subList(0, 4);
    }

    @Override
    public List<Slot> getInventorySlots(BeerBarrelContainer container, BrewingRecipe recipe) {
        int inventoryStartIndex = 5;
        return container.slots.subList(inventoryStartIndex, container.slots.size());
    }
}
