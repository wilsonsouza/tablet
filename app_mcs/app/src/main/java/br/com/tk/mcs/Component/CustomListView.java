/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.widget.ListView;

/**
 * Created by wilsonsouza on 3/2/17.
 */

public class CustomListView extends LinearVertical
{
   public ListView Data = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public CustomListView(Context context)
   {
      super(context);
      this.Data = new ListView(this.getContext());
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
