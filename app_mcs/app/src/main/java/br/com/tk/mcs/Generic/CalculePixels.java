/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by wilsonsouza on 16/01/17.
 */

public class CalculePixels
{
   public static int ToDP(int nValue)
   {
      //DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      //return (int)(nValue * dm.density); //(int)(nValue * (dm.densityDpi / 160f));
      //return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)nValue, dm);
      return nValue; //ConfigDisplayMetrics.ToDP(nValue);
   }
   public static int ToPixels(int nValue)
   {
      DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      return (int)(nValue / dm.density);//(int)(nValue / (dm.densityDpi / 160f));
   }
}
