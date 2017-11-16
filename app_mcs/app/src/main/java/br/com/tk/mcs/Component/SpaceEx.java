/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Point;
import android.widget.Space;

/**
 * Created by wilsonsouza on 31/01/17.
 */

public class SpaceEx extends LinearVertical
{
   public SpaceEx(Context context, int nWidth, int nHeight, boolean bBorder)
   {
      super(context);
      this.Params.SetDimension(new Point(nWidth, nHeight));
      this.addView(new Space(this.getContext()), this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public SpaceEx(Context context, Point area, boolean bBorder)
   {
      super(context);
      this.Params.SetDimension(area);
      this.addView(new Space(this.getContext()), this.Params);
      this.SetBorder(bBorder);
   }
}
