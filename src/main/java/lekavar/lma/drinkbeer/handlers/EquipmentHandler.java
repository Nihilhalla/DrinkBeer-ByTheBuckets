package lekavar.lma.drinkbeer.handlers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;

public class EquipmentHandler {
      /** Gets the slot type from a hand */
    public static EquipmentSlot getSlotType(InteractionHand hand) {
    return hand == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
  }
    
}
