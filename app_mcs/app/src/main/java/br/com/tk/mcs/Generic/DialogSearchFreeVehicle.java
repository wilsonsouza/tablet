/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import java.util.Vector;

import br.com.tk.mcs.Component.ButtonEx;
import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextEx;
import br.com.tk.mcs.Component.LinearHorizontal;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Component.TableWidget;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 24/02/17.
 */

public class DialogSearchFreeVehicle extends DialogEx
{
   protected final static int PLATE = 0x7;
   protected Lane m_pLane = null;
   protected LinearVertical m_pBody = null;
   protected TableView m_pTable = null;
   protected EditTextEx m_pSearch = null;
   protected ButtonEx m_pOK = null;
   protected ButtonEx m_pClear = null;
   protected DisplayMessage m_pDlg = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class TWatcher implements TextWatcher
   {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
         m_pOK.setEnabled(s.length() > 0);
         m_pClear.setEnabled(s.length() > 0);
      }

      @Override
      public void afterTextChanged(Editable s)
      {
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class TableView extends TableWidget implements View.OnClickListener
   {
      protected Vector<FieldData> m_queue = new Vector<>();
      public TableView(Context pWnd)
      {
         super(pWnd);
         this.m_queue.add(new FieldData(R.string.tbl_datetime, 0x96));
         this.m_queue.add(new FieldData(R.string.tbl_via, 0x96));
         this.m_queue.add(new FieldData(R.string.tbl_board, 0x96));

         super.CreateHeader(this.m_queue);
         super.Create(this);
      }

      @Override
      public void onClick(View v)
      {
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogSearchFreeVehicle(Context pWnd, int nID, final Lane pLane)
   {
      super(pWnd, nID, DialogEx.LM_HORIZONTAL);
      try
      {
         Rect padding = new Rect(8, 8, 8, 8);

         this.m_pLane = pLane;
         this.m_pTable = new TableView(getContext());
         this.m_pBody = new LinearVertical(getContext());
         this.m_pSearch = new EditTextEx(getContext(), R.string.tbl_board, PLATE, true, -1, -1, true);
         this.m_pOK = new ButtonEx(getContext(), R.string.button_ok, false, R.drawable.button_selector, padding, null);
         this.m_pClear = new ButtonEx(getContext(), R.string.button_clear, true, R.drawable.button_selector, padding, null);
         this.m_pDlg = new DisplayMessage(this.getContext(), -1, -1);
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
      LinearHorizontal pActions = new LinearHorizontal(getContext());
      {
         pActions.addView(this.m_pSearch, this.m_pSearch.Params);
         pActions.addView(this.m_pOK, this.m_pOK.Params);
         pActions.addView(this.m_pClear, this.m_pClear.Params);
         pActions.setGravity(Gravity.CENTER);
         pActions.SetMargins(new Rect(8, 8, 8, 8));

         this.m_pOK.SetMargins(new Rect(8, 8, 8, 8));
         this.m_pOK.Data.setOnClickListener(this);
         this.m_pClear.Data.setOnClickListener(this);

         this.m_pSearch.Data.setInputType(InputType.TYPE_CLASS_TEXT);
         this.m_pSearch.Data.addTextChangedListener(new TWatcher());
      }

      this.SetIcon(R.drawable.ic_free);
      this.m_pBody.addView(pActions, pActions.Params);
      this.m_pBody.addView(this.m_pTable, this.m_pTable.Params);
      this.m_pTable.Params.height = 0x100;
      this.View.addView(this.m_pBody, this.m_pBody.Params);
      //
      this.SetButtons(new int[]{R.string.button_ok, R.string.button_cancel});
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      int which = v.getId();

      switch (which)
      {
         case R.string.button_ok:
            break;
         case R.string.button_cancel:
            dismiss();
            break;
      }

      if(v == this.m_pClear.Data)
      {
         this.m_pSearch.Data.getText().clear();
         this.m_pSearch.Data.requestFocus();
      }
      else if(v == this.m_pOK.Data)
      {
         new MessageBox(this.m_pWnd, R.string.ids_warning, R.string.ids_unavailable, MessageBox.IDWARNING);
      }
   }
}
