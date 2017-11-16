/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Executors;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 23/01/17.
 *
 * Quando pista mista alterada para avi nao abre somente reinicia.
 */

public class DialogOpenLane extends DialogEx
{
   protected Lane m_pLane = null;
   protected String m_szOperator = null;
   protected LaneState m_state = LaneState.Closed;
   protected View m_pView = null;
   protected DisplayMessage m_pDlg = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher implements Runnable
   {
      public Launcher()
      {
         //this.setName(getClass().getName());
      }
      @Override
      public void run()
      {
         try
         {
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pDlg.show();
                  m_pView.setEnabled(true);
               }
            });

            final Operations p = m_pLane.getOperations();
            {
               p.SetOperatorID(m_szOperator);
               p.setLaneON();
            }
         }
         catch (Exception e)
         {
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  CloseDialog(m_pDlg);
                  new MessageBox(m_pWnd, R.string.manager_open_title, R.string.manager_response_error, MessageBox.IDERROR);
                  //
                  if (m_state != LaneState.NoSync && m_state != LaneState.Starting)
                  {
                     m_pView.setEnabled(true);
                  }
               }
            });

            Log.e(getClass().getName(), e.getMessage());
         }

         CloseDialog(m_pDlg);
         dismiss();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogOpenLane(Context pWnd, View pView, Lane pLane, String szOperator, LaneState state)
   {
      super(pWnd, R.string.manager_open_title, LM_HORIZONTAL);
      try
      {
         this.m_szOperator = szOperator;
         this.m_pLane = pLane;
         this.m_state = state;
         this.m_pView = pView;
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
      String szMessage = String.format(m_pWnd.getString(R.string.open_lane), m_pLane.getLaneName());

      this.SetMessage(String.format(this.m_pWnd.getString(R.string.openlane_message), this.m_pLane.getLaneName()));
      this.SetIcon(R.drawable.ic_openlane);
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.m_pDlg = new DisplayMessage(getContext(), R.string.ids_wait, szMessage);
      super.Create();
      this.SetControlById(R.string.manager_button_confirm, true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      int which = v.getId();

      switch (which)
      {
         case R.string.manager_button_confirm:
         {
            Thread p = Executors.defaultThreadFactory().newThread(new Launcher());
            {
               p.setName(getClass().getName());
               p.start();
            }
            break;
         }
         case R.string.manager_button_cancel:
            dismiss();
            break;
      }
   }
}
