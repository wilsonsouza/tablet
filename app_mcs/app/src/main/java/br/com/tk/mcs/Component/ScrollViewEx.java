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
import android.widget.ScrollView;

/**
 * Created by wilsonsouza on 17/02/17.
 */

public class ScrollViewEx extends LinearVertical
{
   public ScrollView Data = null;
   public LinearVertical Contaneir = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public ScrollViewEx(Context context, boolean bBorder, int nColor, Rect padding, Rect margins)
   {
      super(context);
      this.Data = new ScrollView(this.getContext());
      this.Contaneir = new LinearVertical(this.getContext());
      this.SetBorder(bBorder);
      this.Data.addView(this.Contaneir, this.Contaneir.Params);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder, nColor, Color.BLACK);
      this.SetPadding(padding);
      this.Params.SetMargins(margins);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ScrollViewEx SetPadding(Rect padding)
   {
      super.SetPadding(padding);
      return  this;
   }
}
