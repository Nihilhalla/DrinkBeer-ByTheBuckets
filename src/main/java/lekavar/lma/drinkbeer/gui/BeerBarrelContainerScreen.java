package lekavar.lma.drinkbeer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.gui.utilsborrowedfromMdiyo.GuiTankModule;
import lekavar.lma.drinkbeer.gui.utilsborrowedfromMdiyo.GuiUtil;
import lekavar.lma.drinkbeer.gui.utilsborrowedfromMdiyo.IScreenWithFluidTank;
import lekavar.lma.drinkbeer.gui.utilsborrowedfromMdiyo.RenderUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import slimeknights.mantle.client.render.FluidRenderer;
import slimeknights.mantle.client.render.MantleRenderTypes;
import slimeknights.mantle.client.screen.ElementScreen;

import java.awt.*;

import javax.annotation.Nullable;

public class BeerBarrelContainerScreen extends AbstractContainerScreen<BeerBarrelContainer> implements IScreenWithFluidTank {

    private final ResourceLocation BEER_BARREL_CONTAINER_RESOURCE = new ResourceLocation(DrinkBeer.MOD_ID, "textures/gui/container/beer_barrel.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Inventory inventory;
    //private final ElementScreen SCALA = new ElementScreen(134, 0, 52,52,176,166);
    private final ElementScreen waterTank = new ElementScreen(134, 73, 16, 64, 176, 166);
    private final ElementScreen fluidTank = new ElementScreen(152, 73, 16, 64, 176, 166);
    private final GuiTankModule water;
    private final GuiTankModule fluid;

    public BeerBarrelContainerScreen(BeerBarrelContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        BeerBarrelBlockEntity tileEntity = screenContainer.getTile();
        this.inventory = inv;
        water = new GuiTankModule(this, tileEntity.gettableWaterTank, 134, 11, 16, 63);
        fluid = new GuiTankModule(this, tileEntity.gettableFluidTank, 152, 11, 16, 63);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);

        

    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        GuiUtil.drawBackground(stack, this, BEER_BARREL_CONTAINER_RESOURCE);
        /*RenderSystem.setShaderTexture(0, BEER_BARREL_CONTAINER_RESOURCE);*/
        //int i = (this.width - this.getXSize()) / 2;
        //int j = (this.height - this.getYSize()) / 2;
        //blit(stack, i, j, 0, 0, imageWidth, imageHeight);
        waterTank.draw(stack, 134, 73);
        fluidTank.draw(stack, 152, 73);
        
        water.draw(stack);
        fluid.draw(stack);
    }

    @Override
    protected void renderTooltip(PoseStack matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int x, int y) {
        drawCenteredString(stack, this.font, this.title, (int) this.textureWidth / 2, (int) this.titleLabelY, 4210752);
        this.font.draw(stack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        String str = menu.getIsBrewing() ? convertTickToTime(menu.getRemainingBrewingTime()) : convertTickToTime(menu.getStandardBrewingTime());
        this.font.draw(stack, str, (float) 90, (float) 72, new Color(64, 64, 64, 255).getRGB());
        int checkX = x - this.leftPos;
        int checkY = y - this.topPos;
        water.highlightHoveredFluid(stack, checkX, checkY);
        fluid.highlightHoveredFluid(stack, checkX, checkY);
        //RenderUtils.setup(BEER_BARREL_CONTAINER_RESOURCE);
        //SCALA.draw(stack, 90, 16);
    }

    @Nullable
    public Object getIngredientUnderMouse(double mouseX, double mouseY) {
        Object ingredient = null;
        int checkX = (int) mouseX - leftPos;
        int checkY = (int) mouseY - topPos;
        ingredient = water.getIngreientUnderMouse(checkX, checkY);
        ingredient = fluid.getIngreientUnderMouse(checkX, checkY);


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
