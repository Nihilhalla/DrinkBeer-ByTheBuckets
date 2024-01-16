package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.content;

import net.minecraft.resources.ResourceLocation;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.BookData;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.element.BookElement;

import java.util.ArrayList;

public class ContentBlank extends PageContent {
  public static final ResourceLocation ID = DrinkBeer.getResource("blank");

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
  }
}
