package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network;

import com.nihilhalla.drinkbeer.DrinkBeer;

import net.minecraftforge.network.NetworkDirection;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.FluidContainerTransferPacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.DropLecternBookPacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.OpenLecternBookPacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.OpenNamedBookPacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.SwingArmPacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.UpdateHeldPagePacket;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.network.packet.UpdateLecternPagePacket;

public class MantleNetwork {
  /** Network instance */
  public static final NetworkWrapper INSTANCE = new NetworkWrapper(DrinkBeer.getResource("network"));

  /**
   * Registers packets into this network
   */
  public static void registerPackets() {
    INSTANCE.registerPacket(OpenLecternBookPacket.class, OpenLecternBookPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(UpdateHeldPagePacket.class, UpdateHeldPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(UpdateLecternPagePacket.class, UpdateLecternPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(DropLecternBookPacket.class, DropLecternBookPacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(SwingArmPacket.class, SwingArmPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(OpenNamedBookPacket.class, OpenNamedBookPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(FluidContainerTransferPacket.class, FluidContainerTransferPacket::new, NetworkDirection.PLAY_TO_CLIENT);
  }
}
