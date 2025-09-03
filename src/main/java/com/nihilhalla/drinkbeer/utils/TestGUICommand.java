package com.nihilhalla.drinkbeer.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import com.nihilhalla.drinkbeer.blockentities.BeerBarrelBlockEntity;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainer;
import com.nihilhalla.drinkbeer.gui.BeerBarrelContainerScreen;

import net.minecraft.world.level.block.Blocks;

public class TestGUICommand {
    public static void register(com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher) {
        dispatcher.register(net.minecraft.commands.Commands.literal("testbeerbarrelgui").executes(context -> {
            Minecraft mc = Minecraft.getInstance();
            Inventory inventory = mc.player.getInventory();

            // Create a dummy syncData with defaults
            ContainerData dummySyncData = new ContainerData() {
                private final int[] data = new int[2];

                @Override
                public int get(int index) {
                    return data[index];
                }

                @Override
                public void set(int index, int value) {
                    data[index] = value;
                }

                @Override
                public int getCount() {
                    return 2;
                }
            };

            // Create a dummy block entity for testing
            BeerBarrelBlockEntity dummyBarrel = new BeerBarrelBlockEntity(
                BlockPos.ZERO, // Dummy position
                Blocks.BARREL.defaultBlockState() // Use a default barrel state
            );

            mc.setScreen(new BeerBarrelContainerScreen(
                new BeerBarrelContainer(0, inventory, dummyBarrel, dummySyncData),
                inventory,
                Component.literal("Debug Screen")
            ));

            return 1;
        }));
    }
}
