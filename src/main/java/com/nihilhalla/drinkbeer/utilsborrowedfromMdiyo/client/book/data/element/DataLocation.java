package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.element;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.repository.BookRepository;

public class DataLocation implements IDataElement {

  public String file;
  public transient ResourceLocation location;

  @Override
  public void load(BookRepository source) {
    this.location = "$BLOCK_ATLAS".equals(this.file) ? InventoryMenu.BLOCK_ATLAS : source.getResourceLocation(this.file, true);
  }
}
