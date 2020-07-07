package com.signalfx.training;

import static java.lang.System.out;

public class DeadLockTest {

   static class Friend
   {
     
      private final String name;

      public Friend(final String newName) {
         this.name = newName;
      }
      
    
      public String getName() {
         return this.name;
      }

   
      public synchronized void bow(final Friend bower){
         out.format("%s: %s has bowed to me!%n", 
            this.name, bower.getName());
         bower.bowBack(this);
      }

   
      public synchronized void bowBack(final Friend bower){
         out.format("%s: %s  has bowed back to me!%n",
            this.name, bower.getName());
      }
   }

   public static void createDeadlock(){
      final Friend alphonse = new Friend("Alphonse");
      final Friend gaston = new Friend("Gaston");
      new Thread(new Runnable(){
         public void run() { alphonse.bow(gaston); }
      }, "Gaston Bowing").start();
      new Thread(new Runnable(){
         public void run() { gaston.bow(alphonse); }
      }, "Alphonse Bowing").start();
   }
}
