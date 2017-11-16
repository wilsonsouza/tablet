package br.com.tk.mcs.Lane.State;

/**
 * Created by revolution on 26/02/16.
 */

public enum BarreraState
{
   SensorUnknown(0), SensorON(2), SensorOFF(1);

   private int value;

   BarreraState(int value)
   {
      this.value = value;
   }

   int value()
   {
      return value;
   }

   public static BarreraState fromValue(int value)
   {
      for (BarreraState my : BarreraState.values())
      {
         if (my.value == value)
         {
            return my;
         }
      }
      return null;
   }
}
