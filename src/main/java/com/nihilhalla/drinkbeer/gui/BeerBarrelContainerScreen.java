package com.nihilhalla.drinkbeer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.blockentities.BeerBarrelBlockEntity;
import com.nihilhalla.drinkbeer.utils.borrowed.GuiTankModule;
import com.nihilhalla.drinkbeer.utils.borrowed.GuiUtil;
import com.nihilhalla.drinkbeer.utils.borrowed.IScreenWithFluidTank;

import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.mantle.client.screen.ElementScreen;

import java.awt.Color;

import javax.annotation.Nullable;

public class BeerBarrelContainerScreen extends AbstractContainerScreen<BeerBarrelContainer> implements IScreenWithFluidTank {

    private final static ResourceLocation BEER_BARREL_CONTAINER_RESOURCE = DrinkBeer.getResource("textures/gui/container/beer_barrel.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Inventory inventory;
    //private final ElementScreen SCALA = new ElementScreen(134, 0, 52,52,176,166);
    private static final ElementScreen waterTank = new ElementScreen(BEER_BARREL_CONTAINER_RESOURCE, 134, 73, 16, 64, 176, 166);
    private static final ElementScreen fluidTank = new ElementScreen(BEER_BARREL_CONTAINER_RESOURCE, 152, 73, 16, 64, 176, 166);
    private final GuiTankModule water;
    private final GuiTankModule fluid;
    Font font = Minecraft.getInstance().font;

    public BeerBarrelContainerScreen(BeerBarrelContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        DrinkBeer.LOG.atDebug().log("Screen constructor was called.");
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        BeerBarrelBlockEntity tileEntity = screenContainer.getTile();
        this.inventory = inv;
        if (tileEntity != null) {
            water = new GuiTankModule(this, tileEntity.getWaterTank(), 134, 11, 16, 63, BeerBarrelContainer.TOOLTIP_FORMAT);
            fluid = new GuiTankModule(this, tileEntity.getFluidTank(), 152, 11, 16, 63, BeerBarrelContainer.TOOLTIP_FORMAT);

        } else {
            water = null;
            fluid = null;
        }
        //DrinkBeer.LOG.atDebug().log(tileEntity.getWaterTank().getFluidInTank(0).getAmount());
        //DrinkBeer.LOG.atDebug().log(tileEntity.getFluidTank().getFluidInTank(0).getAmount());
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
        //DrinkBeer.LOG.atDebug().log("Called render");
    }
	@Override
    protected void renderBg(GuiGraphics stack, float partialTicks, int mouseX, int mouseY) {
        GuiUtil.drawBackground(stack, this, BEER_BARREL_CONTAINER_RESOURCE);
        //RenderSystem.setShaderTexture(0, BEER_BARREL_CONTAINER_RESOURCE);
        //int i = (this.width - this.getXSize()) / 2;
        //int j = (this.height - this.getYSize()) / 2;
        //blit(stack, i, j, 0, 0, imageWidth, imageHeight);

        //waterTank.draw(stack, 8, 73);
        //fluidTank.draw(stack, 26, 73);

        if (water != null) water.draw(stack);
        //water.draw(stack);
        if (fluid != null) fluid.draw(stack);
        //DrinkBeer.LOG.atDebug().log("Called renderBg");



    }

    @Override
    protected void renderTooltip(GuiGraphics matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);


        if (water != null) water.renderTooltip(matrices, mouseX, mouseY);
        if (fluid != null) fluid.renderTooltip(matrices, mouseX, mouseY);

    }
    @Override
    protected void renderLabels(GuiGraphics stack, int x, int y) {
        stack.drawCenteredString(this.font, this.title, (int) this.textureWidth / 2, (int) this.titleLabelY, 16250871);
        stack.drawString(font, this.inventory.getDisplayName(),  this.inventoryLabelX,  this.inventoryLabelY, 16250871);
        String str = menu.getIsBrewing() ? convertTickToTime(menu.getRemainingBrewingTime()) : convertTickToTime(menu.getStandardBrewingTime());
        stack.drawString(font, str,  90,  72, new Color(247, 247, 247, 255).getRGB());
        int checkX = x - this.leftPos;
        int checkY = y - this.topPos;
        if (water != null) water.highlightHoveredFluid(stack, checkX, checkY);
        if (fluid != null) fluid.highlightHoveredFluid(stack, checkX, checkY);
        //RenderUtils.setup(BEER_BARREL_CONTAINER_RESOURCE);
        //SCALA.draw(stack, 90, 16);
    }

    @Nullable
    public Object getIngredientUnderMouse(double mouseX, double mouseY) {
        Object ingredient = null;
        int checkX = (int) mouseX - leftPos;
        int checkY = (int) mouseY - topPos; 
        if (water != null)
            ingredient = water.getFluidUnderMouse(checkX, checkY);
        if (fluid != null)
            ingredient = fluid.getFluidUnderMouse(checkX, checkY);


        return ingredient;
    }

    public String convertTickToTime(int tick) {
        String result;
        if (tick > 0) {
            String resultM;
            String resultS;
            double time = tick / 20;
            int m = (int) (time / 60);
            int s = (int) (time % 60);
            if (m < 10) resultM = "0" + m; else
                resultM = "" + m;
            if (s < 10) resultS = ":0" + s; else
                resultS = ":" + s;
            result = resultM + resultS;
        } else result = "";
        return result;
    }

	@Override
	public FluidLocation getFluidUnderMouse(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		return null;
	}
}
