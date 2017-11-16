package br.com.tk.mcs.Remote.Response;

import android.content.Context;

import java.util.Locale;

import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.R;

/**
 * Created by revolution on 16/02/16.
 */

public enum RemotePaymentResponse
{
   ResponseOK(0),
   ResponseERROR(1),
   ResponseNoAutoriz(2),
   ResponseCambioEstadoImp(3),
   ResponseSimulImp(4),
   ResponsePayInvalid(5),
   ResponseInvalidRemoteLaneType(6),
   ResponseNoVehicleInRemoteLane(7),
   ResponseTAGInvalid(10),
   ResponseTAGBlackList(11);

   private int value;
   //-----------------------------------------------------------------------------------------------------------------//
   private RemotePaymentResponse(int value)
   {
      this.value = value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   int value()
   {
      return value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static RemotePaymentResponse fromValue(int value)
   {
      for (RemotePaymentResponse my : RemotePaymentResponse.values())
      {
         if (my.value == value)
         {
            return my;
         }
      }
      return ResponseERROR;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static String getText(Context ctx, RemotePaymentResponse value)
   {

      switch (value)
      {
         case ResponseNoAutoriz:
            return ctx.getString(R.string.manager_assign_no_auth);
         case ResponsePayInvalid:
            return ctx.getString(R.string.manager_assign_blocked);
         case ResponseNoVehicleInRemoteLane:
            return ctx.getString(R.string.manager_assign_gone);
         case ResponseTAGInvalid:
            return ctx.getString(R.string.manager_assign_tagplate);
         case ResponseTAGBlackList:
            return ctx.getString(R.string.manager_tag_response_blacklist);
         case ResponseOK:
            return ctx.getString(R.string.manager_tag_process_ok);
      }
      return ctx.getString(R.string.manager_tag_response_error);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static String FormatMessage(Context context, RemotePaymentResponse value, String szData)
   {
      String fmt = "";

      switch (szData.length())
      {
         case 7:
            fmt = String.format(Locale.FRANCE, "Placa %s", szData);
            break;
         case 0xA:
            fmt = String.format(Locale.FRANCE, "Tag %s", szData);
            break;
         case 0xc:
            fmt = String.format(Locale.FRANCE, "ID %s", szData);
            break;
      }

      switch (value)
      {
         case ResponseNoAutoriz:
         case ResponsePayInvalid:
         case ResponseTAGInvalid:
         case ResponseOK:
         case ResponseTAGBlackList:
            return String.format(Locale.FRANCE, RemotePaymentResponse.getText(context, value), fmt);
         case ResponseNoVehicleInRemoteLane:
            return RemotePaymentResponse.getText(context, value);
      }
      return RemotePaymentResponse.getText(context, value);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static int FormatMessageID(RemotePaymentResponse value)
   {
      switch (value)
      {
         case ResponseNoAutoriz:
         case ResponsePayInvalid:
         case ResponseTAGInvalid:
         case ResponseTAGBlackList:
         case ResponseNoVehicleInRemoteLane:
            return MessageBox.IDWARNING;
         case ResponseOK:
            return MessageBox.IDOK;
      }
      return MessageBox.IDERROR;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}

