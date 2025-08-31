package com.nihilhalla.drinkbeer.client;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.managers.MixedBeerManager;
import com.nihilhalla.drinkbeer.registries.BlockEntityRegistry;
import com.nihilhalla.drinkbeer.client.renderers.MixedBeerEntityRenderer;
import com.nihilhalla.drinkbeer.registries.ItemRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DrinkBeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DrinkBeerClient {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(BlockEntityRegistry.MIXED_BEER_TILEENTITY.get(), MixedBeerEntityRenderer::new);
            ItemProperties.register(ItemRegistry.MIXED_BEER.get(), new ResourceLocation("beer_id"), (stack, level, living, id)
                            -> MixedBeerManager.getBeerId(stack) / 100.0f);
        });
    }
}