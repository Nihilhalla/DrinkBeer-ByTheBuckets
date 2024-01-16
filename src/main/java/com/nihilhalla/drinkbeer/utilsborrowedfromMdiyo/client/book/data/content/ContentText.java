package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.content;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import com.nihilhalla.drinkbeer.DrinkBeer;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.BookData;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.data.element.TextData;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.BookScreen;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.element.BookElement;
import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.element.TextElement;

import java.util.ArrayList;

public class ContentText extends PageContent {
  public static final ResourceLocation ID = DrinkBeer.getResource("text");

  @Getter
  public String title = null;
  public TextData[] text;

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
    int y;
    if (this.title == null || this.title.isEmpty()) {
      y = 0;
    } else {
      this.addTitle(list, this.title);
      y = getTitleHeight();
    }
    if (this.text != null && this.text.length > 0) {
      list.add(new TextElement(0, y, BookScreen.PAGE_WIDTH, BookScreen.PAGE_HEIGHT - y, this.text));
    }
  }
}
