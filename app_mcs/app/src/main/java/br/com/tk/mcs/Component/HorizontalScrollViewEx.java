/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.HorizontalScrollView;

/**
 * Created by wilsonsouza on 17/02/17.
 */

public class HorizontalScrollViewEx extends LinearVertical
{
   public HorizontalScrollView Data = null;
   public LinearHorizontal Contaneir = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public HorizontalScrollViewEx(Context context, boolean bBorder, int nColor, Rect margins, Rect padding)
   {
      super(context);
      this.Data = new HorizontalScrollView(this.getContext());
      this.Contaneir = new LinearHorizontal(this.getContext());
      this.SetBorder(bBorder);
      this.Data.addView(this.Contaneir, this.Contaneir.Params);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder, nColor, Color.TRANSPARENT);
      this.SetPadding(padding);
      this.Params.SetMargins(margins);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public HorizontalScrollViewEx SetPadding(Rect padding)
   {
      if(padding != null)
      {
         this.Data.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      }
      return  this;
   }
}
