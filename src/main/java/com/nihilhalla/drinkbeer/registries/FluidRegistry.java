package com.nihilhalla.drinkbeer.registries;



import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidBlockstateModelProvider;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidBucketModelProvider;
import com.nihilhalla.drinkbeer.utils.borrowed.FluidTextureProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.LiquidBlock;
//import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.fluid.tooltip.AbstractFluidTooltipProvider;
import slimeknights.mantle.registration.FluidBuilder;

public class FluidRegistry {
    // Beers
        public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(DrinkBeer.MOD_ID);
        public static final FlowingFluidObject<ForgeFlowingFluid> SELTZER = FLUIDS.register("seltzer").type(alcohol("seltzer").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> WITHER_STOUT = FLUIDS.register("wither_stout").type(alcohol("wither_stout").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_STOUT = FLUIDS.register("blaze_stout").type(alcohol("blaze_stout").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_MILK_STOUT = FLUIDS.register("blaze_milk_stout").type(alcohol("blaze_milk_stout").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> APPLE_LAMBIC = FLUIDS.register("apple_lambic").type(alcohol("apple_lambic").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> SWEET_BERRY_KRIEK = FLUIDS.register("sweet_berry_kriek").type(alcohol("sweet_berry_kriek").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> HAARS_ICY_PALE_LAGER = FLUIDS.register("haars_icy_pale_lager").type(alcohol("haars_icy_pale_lager").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> PUMPKIN_KVASS = FLUIDS.register("pumpkin_kvass").type(alcohol("pumpkin_kvass").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> NIGHT_HOWL_KVASS = FLUIDS.register("night_howl_kvass").type(alcohol("night_howl_kvass").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> FROTHY_PINK_EGGNOG = FLUIDS.register("frothy_pink_egg_nog").type(alcohol("frothy_pink_egg_nog").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> MINER_PALE_ALE = FLUIDS.register("miner_pale_ale").type(alcohol("miner_pale_ale").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> WISEMAN_BREW = FLUIDS.register("wiseman_brew").type(alcohol("wiseman_brew").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> HELLBREW = FLUIDS.register("hellbrew").type(alcohol("hellbrew").density(800).viscosity(800).temperature(2000)).bucket().block(MapColor.WATER, 0).flowing();

    // Whiskies and spirits
        public static final FlowingFluidObject<ForgeFlowingFluid> WHISKY = FLUIDS.register("whisky").type(alcohol("whisky").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> BLAZE_WHISKY = FLUIDS.register("blaze_whisky").type(alcohol("blaze_whisky").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> WITHER_WHISKY = FLUIDS.register("wither_whisky").type(alcohol("wither_whisky").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> NIGHT_HOWL_WHISKY = FLUIDS.register("night_howl_whisky").type(alcohol("night_howl_whisky").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> SWEET_BERRY_WHISKY = FLUIDS.register("sweet_berry_whisky").type(alcohol("sweet_berry_whisky").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> VODKA = FLUIDS.register("vodka").type(alcohol("vodka").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> VODKA_MASH = FLUIDS.register("vodka_mash").type(alcohol("vodka_mash").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> WINE = FLUIDS.register("wine").type(alcohol("wine").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();
        public static final FlowingFluidObject<ForgeFlowingFluid> COGNAC = FLUIDS.register("cognac").type(alcohol("cognac").density(800).viscosity(800).temperature(300)).bucket().block(MapColor.WATER, 0).flowing();


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

}