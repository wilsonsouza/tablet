/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 12/02/17.
 */

public class Font extends Object
{
   public final static float DEFAULT_SIZE = ConfigDisplayMetrics.FontSize;
   public final static float CAPTION_SIZE = ConfigDisplayMetrics.CaptionSize;
   //-----------------------------------------------------------------------------------------------------------------//
   //
   private Font()
   {}
   //-----------------------------------------------------------------------------------------------------------------//
   public static <T> void SetSize(T view, float fSize, float fScale)
   {
      DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
      float fCurSize = 0;

      if (view instanceof TextView)
      {
         TextView p = ((TextView) view);

         if(fScale == DEFAULT_SIZE)
         {
            //fCurSize = TypedValue.applyDimension(ConfigDisplayMetrics.s_nUnits, fCurSize * fScale, dm);
            fCurSize = DEFAULT_SIZE;
            //p.setTextAppearance(ConfigDisplayMetrics.TextStyle);
         }
         else if(fScale == CAPTION_SIZE)
         {
            //fCurSize = TypedValue.applyDimension(ConfigDisplayMetrics.s_nUnits, fCurSize * fScale, dm);
            fCurSize = CAPTION_SIZE;
            //p.setTextAppearance(ConfigDisplayMetrics.CaptionStyle);
         }

         p.setTextSize ( TypedValue.COMPLEX_UNIT_PX, fCurSize );
         return;
      }

      if (view instanceof EditText)
      {
         EditText p = ((EditText)view);
         //p.setTextAppearance(ConfigDisplayMetrics.EditStyle);
         p.setTextSize( TypedValue.COMPLEX_UNIT_PX, DEFAULT_SIZE);
      }
   }
}
