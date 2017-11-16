/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;

import java.util.Vector;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextCompleteView;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;
import br.com.tk.mcs.Remote.Response.TagPlateResponse;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogSearchTag extends DialogEx implements android.view.View.OnClickListener
{
   protected static int TAG = 0xA;
   protected static int BOARD = 0x7;
   protected Vector<Lane> m_pLanes = new Vector<>();
   protected DisplayMessage m_pDlg = null;
   protected EditTextCompleteView m_board = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class FieldTextWatcher implements TextWatcher
   {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
         SetControlById(R.string.button_ok, s.length() == TAG || s.length() == BOARD);
      }

      @Override
      public void afterTextChanged(Editable s)
      {
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher extends Thread
   {
      protected String m_szData = m_board.GetData().toUpperCase().trim();
      protected Context m_context = getContext();
      protected String m_szType = (m_szData.length() == BOARD ? "Placa" : "TAG");
      protected String m_szMsg = String.format(m_context.getString(R.string.ids_searching), m_szType + " " + m_szData);
      protected String m_szMessage;
      //-----------------------------------------------------------------------------------------------------------------//
      public Launcher()
      {
         super();
         setName(getClass().getName());
         m_board.AddQueueOfCompleteView(this.m_szData);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void run()
      {
         try
         {
            SetMessageTask(m_pDlg, this.m_szMsg);
            ShowDialog(m_pDlg);

            TagPlateResponse Result = TagPlateResponse.Error;
            //
            for (final Lane pLane : m_pLanes)
            {
               try
               {
                  SetMessageTask(m_pDlg, this.m_szMsg + " na " + pLane.getLaneName());

                  final Operations pCurrentOperations = pLane.getOperations();
                  Result = pCurrentOperations.tagPlateRequest(this.m_szData.trim());

                  if(Result != TagPlateResponse.Error)
                  {
                     break;
                  }
               }
               catch (final Exception e)
               {
                  e.printStackTrace();
               }
            }
            //
            Log.i(getClass().getName(), this.m_szMessage = TagPlateResponse.getText(this.m_context, Result));
            CloseDialog(m_pDlg);

            if (!this.m_szMessage.isEmpty())
            {
               String fmt = TagPlateResponse.FormatMessage(this.m_context, Result, m_board.GetData().trim());
               int ID = TagPlateResponse.FormatMessageID(Result);

               ShowMsgBox(R.string.ids_placa, fmt, ID);
            }
         }
         catch (final Exception e)
         {
            CloseDialog(m_pDlg);
            ShowMsgBox(R.string.ids_caption_error, e.getMessage(), MessageBox.IDERROR);
            //Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogSearchTag(Context pWnd, final Vector<Lane> pLanes)
   {
      super(pWnd, R.string.manager_tag_title, LM_HORIZONTAL);
      try
      {
         this.m_pLanes.addAll(pLanes);
         this.Create();
      }
      catch (final Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void Create() throws Exception
   {
      this.m_board = new EditTextCompleteView(getContext(), R.string.manager_tag_field, TAG, true, -1, -1, true);
      this.m_board.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
      this.m_board.Data.addTextChangedListener(new FieldTextWatcher());
      this.m_board.Data.setCursorVisible(true);

      this.m_board.Params.gravity = Gravity.CENTER;
      this.SetIcon(R.drawable.ic_search_plate);
      this.View.addView(this.m_board, this.m_board.Params);
      this.SetButtons(new int[]{R.string.button_ok, R.string.button_cancel});

      this.m_board.SetAdapter();
      this.m_pDlg = new DisplayMessage(getContext(), R.string.manager_tag_title, R.string.manager_tag_find);
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      int which = v.getId();

      switch (which)
      {
         case R.string.button_ok:
            new Launcher().start();
            break;
         case R.string.button_cancel:
            this.dismiss();
            break;
      }
   }
}
