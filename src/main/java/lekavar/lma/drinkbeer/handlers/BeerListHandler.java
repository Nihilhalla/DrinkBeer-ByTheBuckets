package lekavar.lma.drinkbeer.handlers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.commons.StaticInitMerger;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.registries.FluidRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.TagsProvider.TagAppender;

public class BeerListHandler {

        //Making my own friggin lists, mumble grumble
        public static List<Item> buckets;
        public static Map<Fluid, Item> pouredMug = new HashMap<>();
        public static List<Fluid> beers = BeerList();
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
}
