package br.com.tk.mcs.Remote.Response;

/**
 * Created by revolution on 05/02/16.
 */

public class PropertiesResponse
{
   private String vehicleSubclass;

   public PropertiesResponse()
   {
      setVehicleSubclass("");
   }

   public String getVehicleSubclass()
   {
      return vehicleSubclass;
   }

   public void setVehicleSubclass(String vehicleSubclass)
   {
      this.vehicleSubclass = vehicleSubclass;
   }
}