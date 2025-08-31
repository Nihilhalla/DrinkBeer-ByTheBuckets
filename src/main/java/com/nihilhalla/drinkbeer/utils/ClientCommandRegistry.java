package com.nihilhalla.drinkbeer.utils;

import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "drinkbeer", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientCommandRegistry {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterClientCommandsEvent event) {
        TestGUICommand.register(event.getDispatcher());
    }
}