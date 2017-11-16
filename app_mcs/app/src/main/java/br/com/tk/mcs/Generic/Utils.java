/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;

import br.com.tk.mcs.BuildConfig;
import br.com.tk.mcs.Database.PersistenceControllerSquare;

/**
 * Created by revolution on 14/02/16.
 */
public class Utils
{
   private static final SimpleDateFormat m_SimpleDateFormat = new SimpleDateFormat("HH:mm");
   public static final String ROOT = "99999";
   public static final String LANE = "LANE";
   public static final String OPERATOR = "OPERATOR";
   public static final String USER = "Usuário: ";
   public static final String MSC_IMAGE = "MSC";
   public static final String DEBUG = "Enable_m_IsDebugMode_VerifyLaneState";
   public static final String COMPOSITELINK_GLOBAL = ":8181/mcs/GetImage.aspx?name=";
   public static final String COMPOSITELINK_CRO = "/mcs/GetImage.aspx?name=";
   public static final String BITMAPEXTENSION = "-F01.JPG";
   public static final String HTTP_PROTO = "http://";
   //-----------------------------------------------------------------------------------------------------------------//
   public static String getVersion(Context ctx)
   {
      return BuildConfig.VERSION_NAME;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static void hideSoftKeyboardOnCreate(Activity activity)
   {
      activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static void hideSoftKeyboard(Activity activity)
   {
      InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      View view = activity.getCurrentFocus();

      if(view != null)
      {
         inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static void showSoftKeyboard(Activity activity, View view)
   {
      activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
      InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(view, 0);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static boolean isSoftKeyboardShowing(Activity activity)
   {
      InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      return inputMethodManager.isActive();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static void getVibrate(Context ctx, int milliseconds)
   {
      Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
      v.vibrate(milliseconds);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static String MountImageLink(Context context, String imagelink)
   {
      PersistenceControllerSquare square = new PersistenceControllerSquare(context);
      String szLink = HTTP_PROTO + square.Get(); // + Utils.COMPOSITELINK;
      {
         if(Company.IsCRO)
         {
            szLink += Utils.COMPOSITELINK_CRO + imagelink + Utils.BITMAPEXTENSION;
         }
         else
         {
            szLink += Utils.COMPOSITELINK_GLOBAL + imagelink + Utils.BITMAPEXTENSION;
         }
         Log.e(Utils.class.getClass().getName(), szLink);
      }
      return szLink;
   }
}
