package br.com.tk.mcs.Generic;

import android.content.Context;
import android.view.View;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 4/18/17.
 */

public class DialogSimulatePassenger extends DialogEx
{
   private Lane m_pLane = null;
   private DisplayMessage m_pDlg = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher extends Thread
   {
      public Launcher()
      {
         this.setName(getClass().getName());
         SetControlById(R.string.manager_button_confirm, false);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void run()
      {
         try
         {
            ShowDialog(m_pDlg);
            if(!m_pLane.getOperations().SimulePassager())
            {
               ShowMsgBox(R.string.ids_caption_error, R.string.dlg_simulation_error, MessageBox.IDERROR);
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }

         CloseDialog(m_pDlg);
         dismiss();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogSimulatePassenger(Context pWnd, int nID, final Lane pLane)
   {
      super(pWnd, nID, DialogEx.LM_HORIZONTAL);
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
      String szMsg = String.format(this.getContext().getString(R.string.dlg_simulation_question), this.m_pLane.getLaneName() );
      String szDlg = String.format(this.getContext().getString(R.string.dlg_simulation_running), this.m_pLane.getLaneName());

      this.SetMessage(szMsg);
      this.SetIcon(R.drawable.ic_passenger_simulation);
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.m_pDlg = new DisplayMessage(this.getContext(), R.string.ids_wait, szDlg );
      this.SetControlById(R.string.manager_button_confirm, true);
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View pView)
   {
      final int which = pView.getId();

      switch (which)
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
