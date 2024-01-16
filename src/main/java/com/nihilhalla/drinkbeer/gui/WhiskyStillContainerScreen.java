package com.nihilhalla.drinkbeer.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.blockentities.WhiskyStillBlockEntity;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.GuiTankModule;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.GuiUtil;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.IScreenWithFluidTank;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.ElementScreen;

import java.awt.*;

import javax.annotation.Nullable;

public class WhiskyStillContainerScreen extends AbstractContainerScreen<WhiskyStillContainer> implements IScreenWithFluidTank {

    private final ResourceLocation WHISKY_STILL_CONTAINER_RESOURCE = new ResourceLocation(DrinkBeer.MOD_ID, "textures/gui/container/whisky_still.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Inventory inventory;
    //private final ElementScreen SCALA = new ElementScreen(134, 0, 52,52,176,166);
    private static final ElementScreen inputTank = new ElementScreen(8, 73, 16, 64, 176, 166);
    private static final ElementScreen outputTank = new ElementScreen(152, 73, 16, 64, 176, 166);
    private final GuiTankModule input;
    private final GuiTankModule output;

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
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);

    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
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
    protected void renderTooltip(PoseStack matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);


        if (input != null) input.renderTooltip(matrices, mouseX, mouseY);
        if (output != null) output.renderTooltip(matrices, mouseX, mouseY);

    }

    @Override
    protected void renderLabels(PoseStack stack, int x, int y) {
        drawCenteredString(stack, this.font, this.title, (int) this.textureWidth / 2, (int) this.titleLabelY, 4210752);
        //this.font.draw(stack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        String str = menu.getIsBrewing() ? convertTickToTime(menu.getRemainingBrewingTime()) : convertTickToTime(menu.getStandardBrewingTime());
        this.font.draw(stack, str, (float) 76, (float) 16, new Color(64, 64, 64, 255).getRGB());
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
            ingredient = input.getIngreientUnderMouse(checkX, checkY);
        if (output != null)
            ingredient = output.getIngreientUnderMouse(checkX, checkY);


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
}
