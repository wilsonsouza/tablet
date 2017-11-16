/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;

import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 31/01/17.
 */

public class QuestionBox extends DialogEx
{
   public static int DEFAULT = 1;
   public static int CUSTOMER = 2;
   private int m_mode = 0;
   //-----------------------------------------------------------------------------------------------------------------//
   public QuestionBox(Context pWnd, int nID, int nMsg, int nMode)
   {
      super(pWnd, nID, LM_HORIZONTAL);
      try
      {
         this.SetButtons(nMode);
         this.SetIcon(R.drawable.box_question);
         this.SetMessage(nMsg);
         this.Create();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public QuestionBox(Context pWnd, String szCaption, String szMessage, int nMode)
   {
      super(pWnd, szCaption, LM_HORIZONTAL);
      try
      {
         this.SetButtons(nMode);
         this.SetIcon(R.drawable.box_question);
         this.SetMessage(szMessage);
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
      super.Create();
      this.GetControlById(R.string.manager_button_confirm).setEnabled(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private QuestionBox SetButtons(int nMode)
   {
      switch (nMode)
      {
         case 1:
            this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
            break;
         case 2:
            break;
      }
      this.m_mode = nMode;
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      int which = v.getId();

      if(this.m_mode == DEFAULT)
      {
         switch (which)
         {
            case R.string.manager_button_confirm:
               this.m_pWnd.finish();
               break;
            case R.string.manager_button_cancel:
               dismiss();
               break;
         }
      }
   }
}
