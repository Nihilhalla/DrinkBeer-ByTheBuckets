package com.nihilhalla.drinkbeer.registries;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.items.BeerBucket;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidBlockstateModelProvider;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidBucketModelProvider;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidTextureProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import net.minecraftforge.common.SoundActions;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;

public class FluidRegistry {
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(DrinkBeer.MOD_ID);

    // Beers
    public static final FlowingFluidObject<ForgeFlowingFluid> SELTZER = registerDrinkableFluid("seltzer", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> WITHER_STOUT = registerDrinkableFluid("wither_stout", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_STOUT = registerDrinkableFluid("blaze_stout", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_MILK_STOUT = registerDrinkableFluid("blaze_milk_stout", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> APPLE_LAMBIC = registerDrinkableFluid("apple_lambic", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> SWEET_BERRY_KRIEK = registerDrinkableFluid("sweet_berry_kriek", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> HAARS_ICY_PALE_LAGER = registerDrinkableFluid("haars_icy_pale_lager", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> PUMPKIN_KVASS = registerDrinkableFluid("pumpkin_kvass", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> NIGHT_HOWL_KVASS = registerDrinkableFluid("night_howl_kvass", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> FROTHY_PINK_EGGNOG = registerDrinkableFluid("frothy_pink_egg_nog", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> MINER_PALE_ALE = registerDrinkableFluid("miner_pale_ale", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> WISEMAN_BREW = registerDrinkableFluid("wiseman_brew", 800, 300);
    public static final FlowingFluidObject<ForgeFlowingFluid> HELLBREW = registerDrinkableFluid("hellbrew", 800, 2000);

    // Whiskies and spirits
    public static final FlowingFluidObject<ForgeFlowingFluid> WHISKY = FLUIDS.register("whisky")
        .type(alcohol("whisky").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_WHISKY = FLUIDS.register("blaze_whisky")
        .type(alcohol("blaze_whisky").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> WITHER_WHISKY = FLUIDS.register("wither_whisky")
        .type(alcohol("wither_whisky").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> NIGHT_HOWL_WHISKY = FLUIDS.register("night_howl_whisky")
        .type(alcohol("night_howl_whisky").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> SWEET_BERRY_WHISKY = FLUIDS.register("sweet_berry_whisky")
        .type(alcohol("sweet_berry_whisky").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> VODKA = FLUIDS.register("vodka")
        .type(alcohol("vodka").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> VODKA_MASH = FLUIDS.register("vodka_mash")
        .type(alcohol("vodka_mash").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> WINE = FLUIDS.register("wine")
        .type(alcohol("wine").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();
    public static final FlowingFluidObject<ForgeFlowingFluid> COGNAC = FLUIDS.register("cognac")
        .type(alcohol("cognac").density(800).viscosity(800).temperature(300))
        .block(MapColor.WATER, 0)
        .flowing();


    private static FluidType.Properties alcohol() {
        return FluidType.Properties.create()
                                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                                    .canDrown(true)
                                    .canConvertToSource(false);
    }
    private static FluidType.Properties alcohol(String name) {
            return alcohol().descriptionId(DrinkBeer.makeDescriptionId("fluid", name))
                                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }

    @SubscribeEvent
    void gatherData(final GatherDataEvent event) {
      DataGenerator datagenerator = event.getGenerator();
      PackOutput packOutput = datagenerator.getPackOutput();
      boolean client = event.includeClient();
      datagenerator.addProvider(client, new FluidTextureProvider(packOutput));
      datagenerator.addProvider(client, new FluidBucketModelProvider(packOutput, DrinkBeer.MOD_ID));
      datagenerator.addProvider(client, new FluidBlockstateModelProvider(packOutput, DrinkBeer.MOD_ID));
    }

    private static FlowingFluidObject<ForgeFlowingFluid> registerDrinkableFluid(String name, int viscosity, int temperature) {
        final FlowingFluidObject<ForgeFlowingFluid>[] ref = new FlowingFluidObject[1];

        // Create the bucket item separately so we can register it properly
        RegistryObject<BeerBucket> bucket = ItemRegistry.ITEMS.register(name + "_bucket", 
            () -> new BeerBucket(() -> ref[0].getStill(),
                new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)));

        // Now register the fluid and link the bucket
        FlowingFluidObject<ForgeFlowingFluid> fluid =
            FLUIDS.register(name)
                .type(alcohol(name).density(800).viscosity(viscosity).temperature(temperature))
                .block(MapColor.WATER, 0)
                .bucket(bucket) // <── register link
                .flowing();

        ref[0] = fluid;
        return fluid;
    }


    private static FlowingFluidObject<ForgeFlowingFluid> registerDrinkableFluid(String name) {
        return registerDrinkableFluid(name, 800, 300);
    }


}
