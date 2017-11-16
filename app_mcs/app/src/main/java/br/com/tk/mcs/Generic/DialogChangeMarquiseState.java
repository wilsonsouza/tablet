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
import br.com.tk.mcs.Lane.State.TrafficLightState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogChangeMarquiseState extends DialogEx
{
   protected Lane m_pLane = null;
   protected TrafficLightState m_state;
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
            final String szValue = m_pLane.getOperations().getLongStatus().getDevice().getLightStateCanopy();
            final int nBit = Integer.parseInt(szValue);

            switch (nBit)
            {
               case 1:
                  m_pLane.getOperations().setTrafficLightOFF();
                  break;
               case 2:
                  m_pLane.getOperations().setTrafficLightON();
                  break;
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_pDlg);
            ShowMsgBox(R.string.manager_marq_title, R.string.manager_response_error, MessageBox.IDERROR);
            Log.e(getClass().getName(), e.getMessage());
         }
         finally
         {
            dismiss();
            CloseDialog(m_pDlg);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogChangeMarquiseState(Context pWnd, final Lane pLane)
   {
      super(pWnd, R.string.manager_marq_title, LM_HORIZONTAL);
      try
      {
         this.m_pLane = pLane;
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
      String szMsg = String.format(getContext().getString(R.string.manager_marq_response), m_pLane.getLaneName());

      this.SetIcon(R.drawable.box_question);
      this.m_pDlg = new DisplayMessage(this.getContext(), R.string.ids_wait, R.string.change_marquise_state);
      this.SetMessage(szMsg);
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
