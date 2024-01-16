package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.fluid.transfer;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.recipe.helper.ItemOutput;

/** Fluid transfer info that empties a fluid from an item, copying the fluid's NBT to the stack */
public class EmptyFluidWithNBTTransfer extends EmptyFluidContainerTransfer {
  public static final ResourceLocation ID = DrinkBeer.getResource("empty_nbt");
  public EmptyFluidWithNBTTransfer(Ingredient input, ItemOutput filled, FluidStack fluid) {
    super(input, filled, fluid);
  }

  @Override
  protected FluidStack getFluid(ItemStack stack) {
    return new FluidStack(fluid.getFluid(), fluid.getAmount(), stack.getTag());
  }

  @Override
  public JsonObject serialize(JsonSerializationContext context) {
    JsonObject json = super.serialize(context);
    json.addProperty("type", ID.toString());
    return json;
  }

  /** Unique loader instance */
  public static final JsonDeserializer<EmptyFluidContainerTransfer> DESERIALIZER = new Deserializer<>(EmptyFluidWithNBTTransfer::new);
}
