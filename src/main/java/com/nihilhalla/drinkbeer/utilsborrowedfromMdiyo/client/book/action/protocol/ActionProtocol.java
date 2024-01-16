package com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.book.action.protocol;

import com.nihilhalla.drinkbeer.utilsborrowedfromMdiyo.client.screen.book.BookScreen;

public abstract class ActionProtocol {
  public abstract void processCommand(BookScreen book, String param);
}
