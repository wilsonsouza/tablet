/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

/**
 * Created by wilsonsouza on 12/01/17.
 */

public class BorderWidget extends GradientDrawable
{
   //-----------------------------------------------------------------------------------------------------------------//
   public BorderWidget()
   {}
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
   public BorderWidget(View pView, int nColor, int nWidth, int nStrokeColor)
   {
      super();
      this.setColor(nColor);
      this.setStroke(nWidth, nStrokeColor);
      pView.setBackground(this);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
   public static BorderWidget Builder(View pView, int nBkgClr, int nStrokeClr)
   {
      BorderWidget p = new BorderWidget();
      {
         p.setColor(nBkgClr);
         p.setStroke(1, nStrokeClr);
         pView.setBackground(p);
      }
      return p;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static BorderWidget Builder(View pView, int nBkgClr, int nStrokeClr, int nWidth)
   {
      return new BorderWidget(pView, nBkgClr, nWidth, nStrokeClr);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
   public static BorderWidget Builder(View pView)
   {
      BorderWidget p = new BorderWidget();
      {
         p.setColor(android.graphics.Color.WHITE);
         p.setStroke(1, android.graphics.Color.BLACK);
         pView.setBackground(p);
      }
      return p;
   }
}
