package br.com.tk.mcs.Drivers;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by wilsonsouza on 02/02/17.
 */

public class DetectDeviceType
{
   public static boolean IsTablet(Context context)
   {
      try
      {
         TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
         String deviceInfo = manager.getDeviceId(); // the IMEI

         Log.d(DetectDeviceType.class.getClass().getName(), "IMEI or unique ID is " + deviceInfo);
         Log.d(DetectDeviceType.class.getClass().getName(), "Device detected " + deviceInfo != null? "Tablet": "Phone");

         if (manager.getDeviceId() == null)
         {
            return true;
         }
      }
      catch (Exception e)
      {
         Log.e(DetectDeviceType.class.getClass().getName(), e.getMessage());
      }
      return false;
   }
}
