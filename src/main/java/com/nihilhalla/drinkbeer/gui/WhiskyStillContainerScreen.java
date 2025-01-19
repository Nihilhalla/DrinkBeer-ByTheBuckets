package com.nihilhalla.drinkbeer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.blockentities.WhiskyStillBlockEntity;
import com.nihilhalla.drinkbeer.utils.borrowed.GuiTankModule;
import com.nihilhalla.drinkbeer.utils.borrowed.GuiUtil;
import com.nihilhalla.drinkbeer.utils.borrowed.IScreenWithFluidTank;

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

public class WhiskyStillContainerScreen extends AbstractContainerScreen<WhiskyStillContainer> implements IScreenWithFluidTank {

    private final static ResourceLocation WHISKY_STILL_CONTAINER_RESOURCE = DrinkBeer.getResource("textures/gui/container/whisky_still.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Inventory inventory;
    //private final ElementScreen SCALA = new ElementScreen(134, 0, 52,52,176,166);
    private static final ElementScreen inputTank = new ElementScreen(WHISKY_STILL_CONTAINER_RESOURCE, 8, 73, 16, 64, 176, 166);
    private static final ElementScreen outputTank = new ElementScreen(WHISKY_STILL_CONTAINER_RESOURCE, 152, 73, 16, 64, 176, 166);
    private final GuiTankModule input;
    private final GuiTankModule output;
    Font font = Minecraft.getInstance().font;

    public WhiskyStillContainerScreen(WhiskyStillContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        WhiskyStillBlockEntity tileEntity = screenContainer.getTile();
        this.inventory = inv;
        if (tileEntity != null) {
            input = new GuiTankModule(this, tileEntity.getInFluidTank(), 8, 11, 16, 63, WhiskyStillContainer.TOOLTIP_FORMAT);
            output = new GuiTankModule(this, tileEntity.getOutFluidTank(), 152, 11, 16, 63, WhiskyStillContainer.TOOLTIP_FORMAT);

        } else {
            input = null;
            output = null;
        }
        //DrinkBeer.LOG.atDebug().log(tileEntity.getWaterTank().getFluidInTank(0).getAmount());
        //DrinkBeer.LOG.atDebug().log(tileEntity.getFluidTank().getFluidInTank(0).getAmount());
    }
    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);

    }
    @Override
    protected void renderBg(GuiGraphics stack, float partialTicks, int mouseX, int mouseY) {
        GuiUtil.drawBackground(stack, this, WHISKY_STILL_CONTAINER_RESOURCE);
        /*RenderSystem.setShaderTexture(0, BEER_BARREL_CONTAINER_RESOURCE);*/
        //int i = (this.width - this.getXSize()) / 2;
        //int j = (this.height - this.getYSize()) / 2;
        //blit(stack, i, j, 0, 0, imageWidth, imageHeight);

        //inputTank.draw(stack, 8, 73);
        //outputTank.draw(stack, 152, 73);

        if (input != null) input.draw(stack);
        //water.draw(stack);
        if (output != null) output.draw(stack);
        




    }
    @Override
    protected void renderTooltip(GuiGraphics matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);


        if (input != null) input.renderTooltip(matrices, mouseX, mouseY);
        if (output != null) output.renderTooltip(matrices, mouseX, mouseY);

    }
    @Override
    protected void renderLabels(GuiGraphics stack, int x, int y) {
        stack.drawCenteredString(font, this.title, (int) this.textureWidth / 2, (int) this.titleLabelY, 4210752);
        stack.drawString(font, this.inventory.getDisplayName(),  this.inventoryLabelX, this.inventoryLabelY, 4210752);
        String str = menu.getIsBrewing() ? convertTickToTime(menu.getRemainingBrewingTime()) : convertTickToTime(menu.getStandardBrewingTime());
        stack.drawString(font, str,  76,  16, new Color(64, 64, 64, 255).getRGB());
        int checkX = x - this.leftPos;
        int checkY = y - this.topPos;
        if (input != null) input.highlightHoveredFluid(stack, checkX, checkY);
        if (output != null) output.highlightHoveredFluid(stack, checkX, checkY);
        //RenderUtils.setup(BEER_BARREL_CONTAINER_RESOURCE);
        //SCALA.draw(stack, 90, 16);
    }

    @Nullable
    public Object getIngredientUnderMouse(double mouseX, double mouseY) {
        Object ingredient = null;
        int checkX = (int) mouseX - leftPos;
        int checkY = (int) mouseY - topPos; 
        if (input != null)
            ingredient = input.getFluidUnderMouse(checkX, checkY);
        if (output != null)
            ingredient = output.getFluidUnderMouse(checkX, checkY);


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
