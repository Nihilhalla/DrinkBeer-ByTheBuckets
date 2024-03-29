package lekavar.lma.drinkbeer.registries;



import java.util.function.Supplier;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.items.BeerBucket;
import lekavar.lma.drinkbeer.utils.ModCreativeTab;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.ModelFluidAttributes;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import net.minecraft.world.food.FoodProperties;

public class FluidRegistry {

        public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(DrinkBeer.MOD_ID);
        public static final FluidObject<ForgeFlowingFluid> SELTZER = FLUIDS.register("seltzer", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> WITHER_STOUT = FLUIDS.register("wither_stout", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> BLAZE_STOUT = FLUIDS.register("blaze_stout", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> BLAZE_MILK_STOUT = FLUIDS.register("blaze_milk_stout", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> APPLE_LAMBIC = FLUIDS.register("apple_lambic", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> SWEET_BERRY_KRIEK = FLUIDS.register("sweet_berry_kriek", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> HAARS_ICY_PALE_LAGER = FLUIDS.register("haars_icy_pale_lager", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> PUMPKIN_KVASS = FLUIDS.register("pumpkin_kvass", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> NIGHT_HOWL_KVASS = FLUIDS.register("night_howl_kvass", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> FROTHY_PINK_EGGNOG = FLUIDS.register("frothy_pink_egg_nog", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
        public static final FluidObject<ForgeFlowingFluid> MINER_PALE_ALE = FLUIDS.register("miner_pale_ale", fluidBuilder().density(800).viscosity(800).temperature(300), Material.WATER, 0);
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
    private static FluidAttributes.Builder fluidBuilder() {
        return ModelFluidAttributes.builder().sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY);
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