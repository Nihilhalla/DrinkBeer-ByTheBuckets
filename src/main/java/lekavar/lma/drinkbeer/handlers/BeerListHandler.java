package lekavar.lma.drinkbeer.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import lekavar.lma.drinkbeer.registries.FluidRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class BeerListHandler {

        //Making my own friggin lists, mumble grumble
        public static List<Item> buckets;
        public static Map<Fluid, Item> pouredMug = new HashMap<>();
        public static Map<Fluid, Item> pouredSpiritGlass = new HashMap<>();
        public static Map<Fluid, Item> pouredWineGlass = new HashMap<>();
        public static List<Fluid> beers = BeerList();
        public static List<Fluid> spirits = SpiritList();
        public static List<Fluid> wines = WineList();
        public static List<Fluid> acceptedFluids = AcceptedFluidList();
        public static List<Fluid> bucketToFluid;

        public static List<Fluid> AcceptedFluidList () {
            return List.of(FluidRegistry.APPLE_LAMBIC.get(), FluidRegistry.BLAZE_MILK_STOUT.get(), FluidRegistry.BLAZE_STOUT.get()
                , FluidRegistry.FROTHY_PINK_EGGNOG.get(), FluidRegistry.HAARS_ICY_PALE_LAGER.get(), FluidRegistry.MINER_PALE_ALE.get()
                , FluidRegistry.NIGHT_HOWL_KVASS.get(), FluidRegistry.PUMPKIN_KVASS.get(), FluidRegistry.SELTZER.get()
                , FluidRegistry.SWEET_BERRY_KRIEK.get(), FluidRegistry.WITHER_STOUT.get(), Fluids.WATER);
        }

        public static List<Fluid> BeerList () {
            return List.of(FluidRegistry.APPLE_LAMBIC.get(), FluidRegistry.BLAZE_MILK_STOUT.get(), FluidRegistry.BLAZE_STOUT.get()
                , FluidRegistry.FROTHY_PINK_EGGNOG.get(), FluidRegistry.HAARS_ICY_PALE_LAGER.get(), FluidRegistry.MINER_PALE_ALE.get()
                , FluidRegistry.NIGHT_HOWL_KVASS.get(), FluidRegistry.PUMPKIN_KVASS.get(), FluidRegistry.SELTZER.get()
                , FluidRegistry.SWEET_BERRY_KRIEK.get(), FluidRegistry.WITHER_STOUT.get());
        }
        public static List<Fluid> SpiritBaseList () {
            return List.of(FluidRegistry.WINE.get(), FluidRegistry.BLAZE_STOUT.get(), FluidRegistry.MINER_PALE_ALE.get()
                , FluidRegistry.WITHER_STOUT.get(), FluidRegistry.NIGHT_HOWL_KVASS.get(), FluidRegistry.SWEET_BERRY_KRIEK.get()
                , FluidRegistry.VODKA_MASH.get());
        }
        public static List<Fluid> SpiritList () {
            return List.of(FluidRegistry.BLAZE_WHISKY.get(), FluidRegistry.WHISKY.get(), FluidRegistry.WITHER_WHISKY.get()
                , FluidRegistry.NIGHT_HOWL_WHISKY.get(), FluidRegistry.SWEET_BERRY_WHISKY.get(), FluidRegistry.VODKA.get());
        }
        public static List<Fluid> WineList () {
            return List.of(FluidRegistry.WINE.get(), FluidRegistry.COGNAC.get());
        }
        public static List<Item> WineGlassList () {
            return List.of(ItemRegistry.WINE_GLASS.get(), ItemRegistry.COGNAC_GLASS.get());
        }
        public static List<Item> WhiskyGlassList () {
            return List.of(ItemRegistry.BLAZE_WHISKY_GLASS.get(), ItemRegistry.WHISKY_GLASS.get(), ItemRegistry.WITHER_WHISKY_GLASS.get()
                , ItemRegistry.NIGHT_HOWL_WHISKY_GLASS.get(), ItemRegistry.SWEET_BERRY_WHISKY_GLASS.get(), ItemRegistry.VODKA_GLASS.get());
        }
        public static List<Item> StillOutputList () {
            return List.of(ItemRegistry.BLAZE_WHISKY_GLASS.get(), ItemRegistry.WHISKY_GLASS.get(), ItemRegistry.WITHER_WHISKY_GLASS.get()
                , ItemRegistry.NIGHT_HOWL_WHISKY_GLASS.get(), ItemRegistry.SWEET_BERRY_WHISKY_GLASS.get(), ItemRegistry.VODKA_GLASS.get()
                , ItemRegistry.COGNAC_GLASS.get());
        }
        public static List<Item> MugList () {
            return List.of(ItemRegistry.BEER_MUG_APPLE_LAMBIC.get(), ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get(), ItemRegistry.BEER_MUG_BLAZE_STOUT.get()
                , ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get(), ItemRegistry.BEER_MUG_HAARS_ICY_PALE_LAGER.get(), ItemRegistry.BEER_MUG.get()
                , ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get(), ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get(), ItemRegistry.BEER_MUG_SELTZER.get()
                , ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get(), ItemRegistry.BEER_MUG_WITHER_STOUT.get());
        }
        //This is here in case we need it, but we probably never will.
        public void BeerBucketList() {
            for (Fluid fluid : beers) {
                buckets.add(fluid.getBucket().asItem());
            }
        }
        public static Item buildWhiskyGlassMap(Fluid fluidChecked) {
            for (int i = 0; i < spirits.size(); i++) {
                pouredSpiritGlass.put(spirits.get(i), WhiskyGlassList().get(i));
            }
            return pouredSpiritGlass.get(fluidChecked);
        }
        public static Item buildWineGlassMap(Fluid fluidChecked) {
            for (int i = 0; i < wines.size(); i++) {
                pouredWineGlass.put(wines.get(i), WineGlassList().get(i));
            }
            return pouredWineGlass.get(fluidChecked);
        }
        public static Item buildMugMap(Fluid fluidChecked) {
            for (int i = 0; i < beers.size(); i++) {
                pouredMug.put(beers.get(i), MugList().get(i));
            }
            return pouredMug.get(fluidChecked);
        }
        public static Fluid BucketConverter(ItemStack testBucket) {
                for (Fluid fluid : beers) {
                    if (fluid.getBucket().asItem().equals(testBucket)) {
                        return fluid;
                    }
                    return null;
                }
            return null;
        }
        public static final Map<ForgeFlowingFluid, MobEffectInstance> FLUID_EFFECTS_MAP = ImmutableMap.<ForgeFlowingFluid, MobEffectInstance>builder()
            .put(FluidRegistry.APPLE_LAMBIC.get(), new MobEffectInstance(MobEffects.REGENERATION, 1200, 4))
            .put(FluidRegistry.BLAZE_MILK_STOUT.get(), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400, 4))
            .put(FluidRegistry.BLAZE_STOUT.get(),  new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800, 4))
            .put(FluidRegistry.FROTHY_PINK_EGGNOG.get(), new MobEffectInstance(MobEffects.ABSORPTION, 2400, 4))
            .put(FluidRegistry.HAARS_ICY_PALE_LAGER.get(), new MobEffectInstance(MobEffectRegistry.DRUNK_FROST_WALKER.get(), 1200, 4))
            .put(FluidRegistry.MINER_PALE_ALE.get(),  new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 4))
            .put(FluidRegistry.NIGHT_HOWL_KVASS.get(), new MobEffectInstance(MobEffects.NIGHT_VISION, 9600, 4))
            .put(FluidRegistry.PUMPKIN_KVASS.get(), new MobEffectInstance(MobEffects.SATURATION, 1200))
            .put(FluidRegistry.SELTZER.get(), new MobEffectInstance(MobEffects.HUNGER, 5, 20))
            .put(FluidRegistry.SWEET_BERRY_KRIEK.get(), new MobEffectInstance( MobEffects.REGENERATION, 400, 4))
            .put(FluidRegistry.WITHER_STOUT.get(), new MobEffectInstance(MobEffects.BLINDNESS, 10, 4))
            .build();
}
