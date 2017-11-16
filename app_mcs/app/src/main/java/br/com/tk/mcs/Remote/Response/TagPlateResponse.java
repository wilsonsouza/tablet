package br.com.tk.mcs.Remote.Response;

import android.content.Context;

import java.util.Locale;

import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.R;

/**
 * Created by revolution on 04/02/16.
 */

public enum TagPlateResponse
{
   Paid(1), Exempt(2), BlackList(3), NoRegister(4), Error(5), SizeError(6);

   private int value;
   //-----------------------------------------------------------------------------------------------------------------//
   TagPlateResponse(int value)
   {
      this.value = value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public int value()
   {
      return value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static TagPlateResponse fromValue(int value)
   {
      for (TagPlateResponse my : TagPlateResponse.values())
      {
         if (my.value == value)
         {
            return my;
         }
      }
      return Error;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static String getText(Context ctx, TagPlateResponse value)
   {
      switch (value)
      {
         case Paid:
            return ctx.getString(R.string.manager_tag_response_paid);
         case Exempt:
            return ctx.getString(R.string.manager_tag_response_exempt);
         case BlackList:
            return ctx.getString(R.string.manager_tag_response_blacklist);
         case NoRegister:
            return ctx.getString(R.string.manager_tag_response_noregister);
         case SizeError:
            return ctx.getString(R.string.manager_tag_response_size_error);
      }
      return ctx.getString(R.string.manager_tag_response_error);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static String FormatMessage(Context context, TagPlateResponse value, String szData)
   {
      String fmt = "";

      switch (szData.length())
      {
         case 7:
            fmt = String.format(Locale.FRANCE, "com Placa %s", szData);
            break;
         case 0xA:
            fmt = String.format(Locale.FRANCE, "com Tag %s", szData);
            break;
      }

      switch (value)
      {
         case Paid:
         case Exempt:
         case BlackList:
         case NoRegister:
         case SizeError:
            return String.format(TagPlateResponse.getText(context, value), fmt);
      }
      return TagPlateResponse.getText(context, value);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static int FormatMessageID(TagPlateResponse value)
   {
      switch (value)
      {
         case Paid:
         case Exempt:
            return MessageBox.IDOK;
         case BlackList:
            return MessageBox.IDWARNING;
         case NoRegister:
            return MessageBox.IDWARNING;
         case SizeError:
            return MessageBox.IDWARNING;
      }
      return MessageBox.IDERROR;
   }
}
