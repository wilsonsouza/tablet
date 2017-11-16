package br.com.tk.mcs.Lane.State;

/**
 * Created by revolution on 26/02/16.
 */

public enum TrafficLightState
{
   LightOff(0), LightRed(2), LightGreen(1), LightUnkown(3);

   private int value;

   TrafficLightState(int value)
   {
      this.value = value;
   }

   int value()
   {
      return value;
   }

   public static TrafficLightState fromValue(int value)
   {
      for (TrafficLightState my : TrafficLightState.values())
      {
         if (my.value == value)
         {
            return my;
         }
      }
      return null;
   }
}
