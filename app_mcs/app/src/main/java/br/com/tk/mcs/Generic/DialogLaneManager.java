/*

   Sistema de Gestão de Pistas

   (C) 2016, 2017 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.util.Log;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 19/09/2017.
 */

public class DialogLaneManager extends DialogEx
{
   protected BuilderManager m_Builder = null;
   protected int m_IconId = -1;
   //-------------------------------------------------------------------------------------------//
   public DialogLaneManager ( Context pWnd, BuilderManager pBuilder, int nIconId )
   {
      super ( pWnd, R.string.dlg_lane_manager, DialogEx.LM_HORIZONTAL );
      try
      {
         this.m_Builder = pBuilder;
         this.m_IconId = nIconId;
      }
      catch( Exception e)
      {
         Log.e ( e.getClass ().getName (), e.getMessage () );
      }
   }
   //-------------------------------------------------------------------------------------------//
   @Override
   public void Create() throws Exception
   {

   }
   //-------------------------------------------------------------------------------------------//
   @Override
   public void onClick ( android.view.View view )
   {

   }
   //-------------------------------------------------------------------------------------------//
}
