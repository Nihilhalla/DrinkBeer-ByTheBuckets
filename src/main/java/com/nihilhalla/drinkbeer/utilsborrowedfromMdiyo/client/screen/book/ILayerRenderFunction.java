package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.element.BookElement;

public interface ILayerRenderFunction {
  void draw(BookElement element, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, Font fontRenderer);
}
