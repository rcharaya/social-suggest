package com.yahoo.hack.server.util.facebook;

/**
 * Created by IntelliJ IDEA.
 * User: vikashk
 * Date: 10/9/11
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.List;

public class HomeFeed {

   //Collection of entries
   private List<HomeFeedEntry> data;

   //No Arg constructor
   public HomeFeed() {}

   public List<HomeFeedEntry> getData() {
      return data;
   }

   public void setData(List<HomeFeedEntry> data) {
      this.data = data;
   }
}
