package com.nihilhalla.drinkbeer.utils.borrowed;

import com.nihilhalla.drinkbeer.DrinkBeer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.mantle.registration.object.FlowingFluidObject;

@EventBusSubscriber(modid = DrinkBeer.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class FluidClientEvents extends ClientEventBase {

  @SubscribeEvent
  static void registerModelLoaders(RegisterGeometryLoaders event) {
    event.register("fluid_container", FluidContainerModel.LOADER);
  }

  private static void setTranslucent(FlowingFluidObject<?> fluid) {
    ItemBlockRenderTypes.setRenderLayer(fluid.getStill(), RenderType.translucent());
    ItemBlockRenderTypes.setRenderLayer(fluid.getFlowing(), RenderType.translucent());
  }
}
