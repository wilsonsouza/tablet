/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import br.com.tk.mcs.Drivers.DetectDeviceType;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 17/01/17.
 */
@TargetApi(22)
public class ConfigDisplayMetrics
{
   private static final String TAG = ConfigDisplayMetrics.class.getName();
   public static int s_nUnits = TypedValue.COMPLEX_UNIT_DIP;
   public static double s_dDeviceInch = 0;
   public static int Wallpaper = R.drawable.ic_company_web;
   public static Point WallpaperOffset = new Point(ToDP(300), ToDP(200));

   public static int ButtonStyle = 0;
   public static int TextStyle = 0;
   public static int EditStyle = 0;
   public static int CaptionStyle = 0;
   public static int ImageStyle = 0;
   public static int DialogStyle = 0;
   public static int DialogMessageStyle = 0;
   public static int RadioButtonStyle = 0;
   public static int CheckButtonStyle = 0;
   public static int SpinnerStyle = 0;

   public static Point LaneIcon = new Point(48, 48);
   public static Point ButtonImageIcon = new Point(36, 36);
   public static Point DialogIcon = new Point(36, 36);
   public static Point ToolbarIcon = new Point(36, 36);
   public static Point HomeIndicator = new Point(56, 56);
   public static Point ScannerPoint = new Point(ToDP(440), ToDP(440));
   public static Point VehicleImage = new Point(ToDP(640), ToDP(480));

   public static float FontSize = 17f;
   public static float CaptionSize = 24f;
   public static Rect EditRect = new Rect ( 8, 8, 8, 8 );

   @SuppressWarnings("deprecation")
   public static void Builder(Context pWnd)
   {
      Configuration cf = Resources.getSystem().getConfiguration();
      {
         Log.i(cf.getClass().getName(), cf.toString());
         cf = ConfigDisplayMetrics.CheckScreenSizeConfig(cf);
         cf.setTo(cf);
      }
      DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      {
         Log.i(dm.getClass().getName(), dm.toString());
         dm = ConfigDisplayMetrics.CheckScreenSizeDisplay(dm);
         dm.setTo(dm);
      }

      /* define company logo */
      if(Company.IsArteris)
      {
         Wallpaper = R.drawable.logo_arteris;
         WallpaperOffset = new Point(ToDP(300), ToDP(125));
      }
      else if(Company.IsTamoios)
      {
         Wallpaper = R.drawable.logotamoios03;
         WallpaperOffset = new Point(ToDP(200), ToDP(100));
      }
      else if(Company.IsCRO)
      {
         Wallpaper = R.drawable.cro_logo_1;
         WallpaperOffset = new Point(ToDP(200), ToDP(100));
      }
      else if(Company.IsViaRio)
      {
         Wallpaper = R.drawable.vrio;
         WallpaperOffset = new Point(ToDP(200), ToDP(100));
      }
      else if(Company.IsTecsidel)
      {
         Wallpaper = R.drawable.ic_company_web;
         WallpaperOffset = new Point(ToDP(300), ToDP(128));
      }
      else if(Company.Is040)
      {
         Wallpaper = R.drawable.logo_via040;
         WallpaperOffset = new Point(ToDP ( 373 - 173 ), ToDP ( 373 - 200  ));
      }

      if(dm.heightPixels < 0x300)
      {
         VehicleImage = new Point(VehicleImage.x - 0x1e0, VehicleImage.y - 0x1e0);
      }

      /* set private and static variable with device inch */
      ConfigDisplayMetrics.s_dDeviceInch = ConfigDisplayMetrics.GetDeviceInch();

      /* set toolbar icon */
      int nTH = ConfigDisplayMetrics.GetAttrSizeH ( pWnd, android.R.attr.actionBarSize );
      {
         LaneIcon = new Point ( nTH, nTH );
         HomeIndicator = new Point(nTH, nTH);
         ButtonImageIcon = new Point ( nTH /2, nTH /2 );
         DialogIcon = new Point ( nTH, nTH );
         ToolbarIcon = new Point(nTH - 8, nTH - 8);
      }
      /* define icon offset */
      ButtonStyle = 0; //R.drawable.button_selector;
      TextStyle = R.style.label;
      CaptionStyle = R.style.caption;
      ImageStyle = android.R.attr.imageWellStyle;
      EditStyle = android.R.style.TextAppearance_Widget_EditText;
      DialogStyle = 0;
      DialogMessageStyle = 0; //android.R.style.Theme_DeviceDefault_Light_Dialog;

      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
      {
         DialogStyle = android.R.style.Theme_Dialog;
         DialogMessageStyle = android.R.style.Theme_Dialog;
      }

      if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
      {
         DialogStyle = android.R.style.Theme_Dialog;
         DialogMessageStyle = android.R.style.Theme_Dialog;
         RadioButtonStyle = android.R.attr.radioButtonStyle;
         CheckButtonStyle = android.R.attr.checkboxStyle;
         SpinnerStyle = android.R.attr.spinnerItemStyle|android.R.attr.dropDownSpinnerStyle;
      }

      if(GetDeviceInch() <= 7.0)
      {
         ScannerPoint = new Point(ToDP(320), ToDP(320));
         VehicleImage = new Point(ToDP(440), ToDP(280));
         FontSize = 16f;
         CaptionSize = 22f;
         EditRect = new Rect ( 4, 4, 4, 4 );
      }

      if( !DetectDeviceType.IsTablet ( pWnd ))
      {
         s_nUnits = TypedValue.COMPLEX_UNIT_PX;
         FontSize = 32f;
         CaptionSize = 48f;
      }
      /* assign changes */
      Resources.getSystem().updateConfiguration(cf, dm);

      /* register density */
      if(Company.IsDebug)
      {
         Log.i ( TAG, String.format ( "Density %f", dm.density ) );
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static Configuration CheckScreenSizeConfig(Configuration cf)
   {
      //cf.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
      return cf;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static DisplayMetrics CheckScreenSizeDisplay(DisplayMetrics dm)
   {
      //dm.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
      return dm;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static double GetDeviceInch()
   {
      DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      double dWidthDP = Math.pow((dm.widthPixels / dm.densityDpi), 2);
      double dHeightDP = Math.pow((dm.heightPixels/ dm.densityDpi), 2);
      double dInch = Math.sqrt(dWidthDP + dHeightDP);
      Log.i(TAG, String.format("Device inch %f", dInch));
      return dInch;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static int ToDP(int nPixels)
   {
      DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      return (int)TypedValue.applyDimension(ConfigDisplayMetrics.s_nUnits, nPixels / dm.density, dm);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static int GetAttrSizeH(Context pWnd, int attr)
   {
      TypedValue t = new TypedValue ();

      pWnd.getTheme ().resolveAttribute ( attr, t, true );
      int n = Resources.getSystem ().getDimensionPixelSize ( t.resourceId );
      return n;
   }

}
