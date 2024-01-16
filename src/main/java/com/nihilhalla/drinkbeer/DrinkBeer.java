package com.nihilhalla.drinkbeer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.google.common.graph.Network;

import com.nihilhalla.drinkbeer.client.DrinkBeerClient;
import com.nihilhalla.drinkbeer.effects.WitherStoutEffect;
import com.nihilhalla.drinkbeer.handlers.BeerListHandler;
import com.nihilhalla.drinkbeer.handlers.EventHandler;
import com.nihilhalla.drinkbeer.networking.NetWorking;
import com.nihilhalla.drinkbeer.registries.*;

import net.minecraft.Util;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.Tag.Builder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.datagen.MantleFluidTagProvider;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.datagen.MantleFluidTooltipProvider;

// The value here should match an entry in the META-INF/mods.toml file

@Mod("drinkbeer")

public class DrinkBeer {
    public static EventHandler eventHandler;
    public static BeerListHandler bucketListHandler;
    // We don't need this logger now since there is no need at all.
    // Directly reference a log4j logger.
    // private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "drinkbeer";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public static final TagKey<Item> BOOZE_BUCKET = ItemTags.create(new ResourceLocation(MOD_ID, "beer_buckets"));
    public static final TagKey<Item> EMPTY_BEER = ItemTags.create(new ResourceLocation(MOD_ID, "empty_mug"));
    public static final TagKey<Fluid> ALCOHOLS = FluidTags.create(new ResourceLocation(MOD_ID, "alcohols"));

    public DrinkBeer() {

        ForgeMod.enableMilkFluid();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        eventHandler = new EventHandler();

        MinecraftForge.EVENT_BUS.register(eventHandler);

        MobEffectRegistry.STATUS_EFFECTS.register(bus);
        ItemRegistry.ITEMS.register(bus);
        BlockRegistry.BLOCKS.register(bus);
        BlockEntityRegistry.BLOKC_ENTITIES.register(bus);
        FluidRegistry.FLUIDS.register(bus);
        //FluidRegistry.registerDynamicBuckets();
        SoundEventRegistry.SOUNDS.register(bus);
        ContainerTypeRegistry.CONTAINERS.register(bus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(bus);
        RecipeRegistry.RECIPE_TYPES.register(bus);
        ParticleRegistry.PARTICLES.register(bus);

        bus.addListener(DrinkBeerClient::onInitializeClient);
        bus.addListener(NetWorking::init);
        // We just don't need these part now
        // Register the setup method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        
    }
    // Next code segments shamelessly stolen from Mantle
      private void gatherData(final GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    if (event.includeServer()) {
      generator.addProvider(new MantleFluidTagProvider(generator, event.getExistingFileHelper()));
    }
    if (event.includeClient()) {
      generator.addProvider(new MantleFluidTooltipProvider(generator));
    }
  }

  /**
   * Gets a resource location for Mantle
   * @param name  Name
   * @return  Resource location instance
   */
  public static ResourceLocation getResource(String name) {
    return new ResourceLocation(MOD_ID, name);
  }

  /**
   * Makes a translation key for the given name
   * @param base  Base name, such as "block" or "gui"
   * @param name  Object name
   * @return  Translation key
   */
  public static String makeDescriptionId(String base, String name) {
    return Util.makeDescriptionId(base, getResource(name));
  }

  /**
   * Makes a translation text component for the given name
   * @param base  Base name, such as "block" or "gui"
   * @param name  Object name
   * @return  Translation key
   */
  public static MutableComponent makeComponent(String base, String name) {
    return new TranslatableComponent(makeDescriptionId(base, name));
  }
    public static final ResourceKey<Fluid> beerKey = ResourceKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(MOD_ID));
    //Tagging stuff
    



    //Get that client stuff done!
    private void clientSetup(final FMLClientSetupEvent event) {

    //    ItemBlockRenderTypes.setRenderLayer(FluidHandler.BEERSOURCE.get(), RenderType.translucent());
    //    ItemBlockRenderTypes.setRenderLayer(FluidHandler.BEERFLOWING.get(), RenderType.translucent());
    //    ItemBlockRenderTypes.setRenderLayer(BlockRegistry.BEERBLOCK.get(), RenderType.translucent());
    }
    // We just don't need these part now
    /*private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ScreenManager.register(ContainerTypeRegistry.beerBarrelContainer.get(), BeerBarrelContainerScreen::new));
    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }


    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }*/
}
