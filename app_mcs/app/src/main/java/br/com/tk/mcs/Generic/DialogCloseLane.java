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
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogCloseLane extends DialogEx
{
   protected View m_pView = null;
   protected Lane m_pLane = null;
   protected LaneState m_state = LaneState.Closed;
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
                  m_pView.setEnabled(false);
               }
            });

            m_pLane.getOperations().setLaneOFF();
            Log.i(getClass().getName(), m_pLane.getOperations().getLongStatus().getLaneState().toString());
         }
         catch (Exception e)
         {
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pDlg.dismiss();
                  new MessageBox(m_pWnd, R.string.manager_close_title, R.string.manager_response_error, R.drawable.box_error);
                  //
                  if (m_state != LaneState.NoSync && m_state != LaneState.Starting)
                  {
                     m_pView.setEnabled(true);
                  }
               }
            });

            Log.e(getClass().getName(), e.getMessage());
         }
         finally
         {
            CloseDialog(m_pDlg);
            dismiss();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogCloseLane(Context pWnd, View pView, Lane pLane, LaneState state)
   {
      super(pWnd, R.string.manager_close_title, LM_HORIZONTAL);
      try
      {
         this.m_pView = pView;
         this.m_pLane = pLane;
         this.m_state = state;
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
      String szMsg = String.format(m_pWnd.getString(R.string.close_lane), this.m_pLane.getLaneName());

      this.SetIcon(R.drawable.ic_closelane);
      this.SetMessage(String.format(m_pWnd.getString(R.string.closelane_message), this.m_pLane.getLaneName()));
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.m_pDlg = new DisplayMessage(getContext(), R.string.ids_wait, szMsg);
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
