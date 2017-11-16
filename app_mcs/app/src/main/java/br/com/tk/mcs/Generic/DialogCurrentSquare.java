/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
*/
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.view.View;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 5/8/17.
 */
public class DialogCurrentSquare extends DialogEx
{
   protected BuilderManager m_pManager = null;
   public DialogCurrentSquare(Context pWnd, final BuilderManager pOwner)
   {
      super(pWnd, R.string.dlg_current_square, LM_HORIZONTAL);
      try
      {
         this.m_pManager = pOwner;
         this.Create();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void Create() throws Exception
   {
      LinearVertical body = new LinearVertical(this.getContext());
      {
      }
      //
      this.SetIcon(R.drawable.cctv_camera_icon);
      this.SetButtons(new int[]{R.string.button_ok});
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View pView)
   {

   }
}
