package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.nihilhalla.drinkbeer.DrinkBeer;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = DrinkBeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MantleShaders {

  private static ShaderInstance blockFullBrightShader;

  @SubscribeEvent
  static void registerShaders(RegisterShadersEvent event) throws IOException {
    event.registerShader(
      new ShaderInstance(event.getResourceManager(), DrinkBeer.getResource("block_fullbright"), DefaultVertexFormat.BLOCK),
      shader -> blockFullBrightShader = shader
    );
  }

  public static ShaderInstance getBlockFullBrightShader() {
    return blockFullBrightShader;
  }
}
