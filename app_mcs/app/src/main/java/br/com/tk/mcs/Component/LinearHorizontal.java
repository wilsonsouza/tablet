/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class LinearHorizontal extends LinearLayoutImpl
{
   public LinearHorizontal(Context context)
   {
      super(context);
      this.setOrientation(HORIZONTAL);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearHorizontal(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      this.setOrientation(HORIZONTAL);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearHorizontal(Context context, AttributeSet attrs, int defStyleAttr)
   {
      super(context, attrs, defStyleAttr);
      this.setOrientation(HORIZONTAL);
   }
}
