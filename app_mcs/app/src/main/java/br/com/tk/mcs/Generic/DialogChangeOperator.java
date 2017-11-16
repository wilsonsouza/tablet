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

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogChangeOperator extends DialogEx
{
   protected View m_pView = null;
   protected Lane m_pLane = null;
   protected boolean m_bChangeOperator = false;
   protected LaneState m_state = LaneState.NoSync;
   protected DisplayMessage m_pDlg = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher extends Thread
   {
      public Launcher()
      {
         this.setName(getClass().getName());
      }
      @Override
      public void run()
      {
         try
         {
            ShowDialog(m_pDlg);
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pView.setEnabled(false);
               }
            });
            final Operations op = m_pLane.getOperations();
            {
               op.setLaneOperator();
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_pDlg);
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  new MessageBox(m_pWnd, R.string.manager_resp_title, R.string.manager_response_error, MessageBox.IDERROR);
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
            dismiss();
            CloseDialog(m_pDlg);
            m_bChangeOperator = true;
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogChangeOperator(Context pWnd, View pView, Lane pLane, LaneState state)
   {
      super(pWnd, R.string.manager_resp_title, LM_HORIZONTAL);
      try
      {
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
      this.m_pDlg = new DisplayMessage(this.getContext(), R.string.ids_wait, R.string.verify_operator);
      this.SetIcon(R.drawable.ic_operator);
      this.SetMessage(R.string.manager_resp_response);
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.SetControlById(R.string.manager_button_confirm, true);
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public boolean IsChangeOperator()
   {
      return this.m_bChangeOperator;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      int nID = v.getId();
      switch (nID)
      {
         case R.string.manager_button_confirm:
            new Launcher().start();
            break;
         case R.string.manager_button_cancel:
            this.dismiss();
            break;
      }
   }
}
