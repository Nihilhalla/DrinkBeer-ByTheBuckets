package com.nihilhalla.drinkbeer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class BartendingTableContainerScreen extends AbstractContainerScreen<BartendingTableContainer> {
    private final ResourceLocation BARTENDING_TABLE_CONTAINER_RESOURCE = new ResourceLocation("drinkbeer", "textures/gui/container/bartending_table.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Inventory inventory;
    Font font = Minecraft.getInstance().font;

    public BartendingTableContainerScreen(BartendingTableContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;

        this.inventory = inv;
    }
    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }
    @Override
    protected void renderBg(GuiGraphics stack, float partialTicks, int mouseX, int mouseY) {
        renderBackground(stack);
        RenderSystem.setShaderTexture(0, BARTENDING_TABLE_CONTAINER_RESOURCE);
        int i = (this.width - this.getXSize()) / 2;
        int j = (this.height - this.getYSize()) / 2;
        stack.blit(BARTENDING_TABLE_CONTAINER_RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
    }
    @Override
    protected void renderLabels(GuiGraphics stack, int x, int y) {
        stack.drawCenteredString(font, this.title, (int) this.textureWidth / 2, (int) this.titleLabelY, 16250871);
        stack.drawString(font, this.inventory.getDisplayName(),  this.inventoryLabelX,  this.inventoryLabelY, 16250871);
    }
}
