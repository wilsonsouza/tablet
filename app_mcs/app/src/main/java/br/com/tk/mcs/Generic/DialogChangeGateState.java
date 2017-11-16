/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.util.Log;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.Lane.State.BarreraState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogChangeGateState extends DialogEx
{
   protected Lane m_pLane = null;
   protected BarreraState m_state = BarreraState.SensorUnknown;
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
            final Operations p = m_pLane.getOperations();

            if (m_state == BarreraState.SensorON)
            {
               p.setBarrierOFF();
            }
            else if (m_state == BarreraState.SensorOFF)
            {
               p.setBarrierON();
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_pDlg);
            ShowMsgBox(R.string.manager_barrera_title, R.string.manager_response_error, MessageBox.IDERROR);
            Log.e(getClass().getName(), e.getMessage());
         }
         finally
         {
            dismiss();
            CloseDialog(m_pDlg);
            Log.i(getClass().getName(), m_state.toString());
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogChangeGateState(Context pWnd, Lane pLane)
   {
      super(pWnd, R.string.manager_barrera_title, LM_HORIZONTAL);
      try
      {
         this.m_pLane = pLane;
         this.m_state = pLane.getBarreraState();
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
      this.m_pDlg = new DisplayMessage(getContext(), R.string.ids_wait, R.string.change_gate_state);
      this.m_state = this.m_pLane.getBarreraState();

      if(this.m_state == BarreraState.SensorON)
      {
         this.SetIcon(R.drawable.ln_barrer_up_hd);
         this.SetMessage(String.format(getContext().getString(R.string.manager_barrera_close_response), m_pLane.getLaneName()));
      }
      else
      {
         this.SetIcon(R.drawable.ln_barrer_down_hd);
         this.SetMessage(String.format(getContext().getString(R.string.manager_barrera_open_response), m_pLane.getLaneName()));
      }

      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.SetControlById(R.string.manager_button_confirm, true);
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      int which = v.getId();

      switch (which)
      {
         case R.string.manager_button_confirm:
            new Launcher().start();
            break;
         case R.string.manager_button_cancel:
            dismiss();
            break;
      }
   }
}
