package com.nihilhalla.drinkbeer.registries;



import com.nihilhalla.drinkbeer.DrinkBeer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.registration.FluidBuilder;

public class FluidRegistry {
    // Beers
        public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(DrinkBeer.MOD_ID);
        public static final FlowingFluidObject<ForgeFlowingFluid> SELTZER = FLUIDS.register("seltzer").type(fluidBuilder("seltzer").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> WITHER_STOUT = FLUIDS.register("wither_stout").type(fluidBuilder("wither_stout").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> BLAZE_STOUT = FLUIDS.register("blaze_stout").type(fluidBuilder("blaze_stout").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> BLAZE_MILK_STOUT = FLUIDS.register("blaze_milk_stout").type(fluidBuilder("blaze_milk_stout").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> APPLE_LAMBIC = FLUIDS.register("apple_lambic").type(fluidBuilder("apple_lambic").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> SWEET_BERRY_KRIEK = FLUIDS.register("sweet_berry_kriek").type(fluidBuilder("sweet_berry_kriek").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> HAARS_ICY_PALE_LAGER = FLUIDS.register("haars_icy_pale_lager").type(fluidBuilder("haars_icy_pale_lager").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> PUMPKIN_KVASS = FLUIDS.register("pumpkin_kvass").type(fluidBuilder("pumpkin_kvass").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> NIGHT_HOWL_KVASS = FLUIDS.register("night_howl_kvass").type(fluidBuilder("night_howl_kvass").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> FROTHY_PINK_EGGNOG = FLUIDS.register("frothy_pink_egg_nog").type(fluidBuilder("frothy_pink_egg_nog").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> MINER_PALE_ALE = FLUIDS.register("miner_pale_ale").type(fluidBuilder("miner_pale_ale").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();

    // Whiskies and spirits
        public static final FluidObject<ForgeFlowingFluid> WHISKY = FLUIDS.register("whisky").type(fluidBuilder("whisky").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> BLAZE_WHISKY = FLUIDS.register("blaze_whisky").type(fluidBuilder("blaze_whisky").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> WITHER_WHISKY = FLUIDS.register("wither_whisky").type(fluidBuilder("wither_whisky").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> NIGHT_HOWL_WHISKY = FLUIDS.register("night_howl_whisky").type(fluidBuilder("night_howl_whisky").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> SWEET_BERRY_WHISKY = FLUIDS.register("sweet_berry_whisky").type(fluidBuilder("sweet_berry_whisky").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> VODKA = FLUIDS.register("vodka").type(fluidBuilder("vodka").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> VODKA_MASH = FLUIDS.register("vodka_mash").type(fluidBuilder("vodka_mash").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> WINE = FLUIDS.register("wine").type(fluidBuilder("wine").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();
        public static final FluidObject<ForgeFlowingFluid> COGNAC = FLUIDS.register("cognac").type(fluidBuilder("cognac").density(800).viscosity(800).temperature(300)).bucket().block(Material.WATER).flowing();

    /* 
    public static void registerDynamicBuckets() {
            // Loop through fluids
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            if (fluid.getRegistryName() != null && fluid.getRegistryName().getNamespace().equals(DrinkBeer.MOD_ID)) {
               // Create a Supplier<Fluid> for the fluid
                Supplier<Fluid> fluidSupplier = () -> fluid;

                // Register a drinkable dynamic bucket for the fluid
                RegistryObject<Item> BEER_BUCKET = ItemRegistry.ITEMS.register(
                        fluid.getRegistryName().getPath() + "_bucket",
                        () -> new BeerBucket(fluidSupplier, new Item.Properties().stacksTo(1).tab(ModCreativeTab.BEER))
                );
            }

        }
    }
*/
    private static FluidType.Properties fluidBuilder() {
        return FluidType.Properties.create()
                                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }
    private static FluidType.Properties fluidBuilder(String name) {
            return fluidBuilder().descriptionId(Mantle.makeDescriptionId("fluid", name))
                                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }

        


    //All of the code below this comment was written before I decided to make the beer fluid an independent fluid that can be registered in a group calling.
        /*public static final RegistryObject<UnplaceableFluid> BEER_BASE = FLUIDS.register("beer_base", () -> new UnplaceableFluid(ItemRegistry.BEER_BUCKET, FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
        .density(15).luminosity(2).viscosity(5).sound(Items.WATER_BUCKET.getEquipSound()).overlay(WATER_OVERLAY_RL)
        .color(0xbffcba03)));*/
        
    //This was the pre-Mantle way.
    //public static final RegistryObject<FlowingFluid> BEERFLOWING = FLUIDS.register("beer_flowing", () -> new ForgeFlowingFluid.Flowing(FluidHandler.BEERProps));
    //public static final RegistryObject<FlowingFluid> BEERSOURCE = FLUIDS.register("beer_source", () -> new ForgeFlowingFluid.Source(FluidHandler.BEERProps));

        /*public static final ForgeFlowingFluid.Properties BEERProps = new ForgeFlowingFluid.Properties(() -> BEERSOURCE.get(), () -> BEERFLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
        .density(15).luminosity(2).viscosity(5).sound(Items.WATER_BUCKET.getEquipSound()).overlay(WATER_OVERLAY_RL)
        .color(0xbffcba03)).slopeFindDistance(2).levelDecreasePerBlock(2)
        .block(() -> BlockRegistry.BEERBLOCK.get()).bucket(() -> ItemRegistry.BEER_BUCKET.get());

*/

}