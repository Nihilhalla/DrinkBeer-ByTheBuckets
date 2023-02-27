package lekavar.lma.drinkbeer.handlers.jei;

import java.util.ArrayList;
import java.util.List;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JEIBrewingRecipe implements IRecipeCategory<BrewingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(DrinkBeer.MOD_ID, "brewing");
    private static final String DRINK_BEER_YELLOW = "#F4D223";
    private static final String NIGHT_HOWL_CUP_HEX_COLOR = "#C69B82";
    private static final String PUMPKIN_DRINK_CUP_HEX_COLOR = "#AC6132";
    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;


    public JEIBrewingRecipe(IGuiHelper helper) {
        guiHelper = helper;
        background = helper.createDrawable(new ResourceLocation(DrinkBeer.MOD_ID, "textures/gui/container/beer_barrel.png"),
                0, 0, 176, 66);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemRegistry.BEER_MUG.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends BrewingRecipe> getRecipeClass() {
        return BrewingRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("drinkbeer.jei.title.brewing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return JEIPlugin.BREWING;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 25).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredientsItems().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 46, 25).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredientsItems().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 43).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredientsItems().get(2));
        if (recipe.getIngredientsItems().size() > 3 ) {
            builder.addSlot(RecipeIngredientRole.INPUT, 46, 43).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredientsItems().get(3));
        }
/*
        DrinkBeer.LOG.atDebug().log(recipe.getIngredientsItems().get(0));
        DrinkBeer.LOG.atDebug().log(recipe.getIngredientsItems().get(1));
        DrinkBeer.LOG.atDebug().log(recipe.getIngredientsItems().get(2));
        DrinkBeer.LOG.atDebug().log(recipe.getIngredientsItems().get(3));
*/
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 11)
            .setFluidRenderer(1000, true, 16, 55)
            .addIngredient(ForgeTypes.FLUID_STACK, recipe.getFluidIngredient());
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 152, 11)
            .setFluidRenderer(1000, true, 16, 55)
            .addIngredient(ForgeTypes.FLUID_STACK, recipe.getResult());
        
        builder.moveRecipeTransferButton(180, 50);

    }

    @Override
    public List<Component> getTooltipStrings(BrewingRecipe recipe, double mouseX, double mouseY) {
        List<Component> tooltips = new ArrayList<>();
        if (!inTransferBottomRange(mouseX, mouseY)){
            if (inCupSlotRange(mouseX, mouseY)) {
                tooltips.add(new TranslatableComponent("drinkbeer.jei.tooltip.cup_slot")
                        .setStyle(Style.EMPTY.withColor(TextColor.parseColor(DRINK_BEER_YELLOW))));
            } else {
                int brewingTimeMin = (recipe.getBrewingTime() / 20) / 60;
                int brewingTimeSec = recipe.getBrewingTime() / 20 - brewingTimeMin * 60;
                tooltips.add(new TranslatableComponent("drinkbeer.jei.tooltip.brewing")
                        .setStyle(Style.EMPTY.withColor(TextColor.parseColor(PUMPKIN_DRINK_CUP_HEX_COLOR)))
                        .append(new TextComponent(brewingTimeMin + ":" + (brewingTimeSec < 10 ? "0" + brewingTimeSec : brewingTimeSec))
                                .withStyle(Style.EMPTY.withBold(true).withColor(TextColor.parseColor(DRINK_BEER_YELLOW)))));
            }
        }
        return tooltips;
    }

    private boolean inCupSlotRange(double mouseX, double mouseY) {
        return mouseX >= 72 && mouseX < 90 && mouseY >= 49 && mouseY <= 67;
    }

    private boolean inTransferBottomRange(double mouseX, double mouseY) {
        return mouseX >= 180 && mouseX < 193 && mouseY >= 50 && mouseY < 63;
    }
}