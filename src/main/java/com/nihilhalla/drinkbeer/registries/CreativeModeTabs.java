package com.nihilhalla.drinkbeer.registries;


import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utils.borrowed.CustomTabBehavior;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DrinkBeer.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register(DrinkBeer.MOD_ID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + DrinkBeer.MOD_ID + ".general"))
            .withTabsBefore(net.minecraft.world.item.CreativeModeTabs.SPAWN_EGGS)
            .displayItems((enabledFeatures, output) -> {
                for(RegistryObject<Item> item : ItemRegistry.ITEMS.getEntries()){
                    if(item.get() instanceof CustomTabBehavior customTabBehavior){
                        customTabBehavior.fillItemCategory(output);
                    }else{
                        output.accept(item.get());
                    }
                }
            })
            .icon(() -> new ItemStack(ItemRegistry.BEER_MUG.get()))
            .build());
}
