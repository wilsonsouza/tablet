/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import java.util.Vector;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.TableWidget;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 24/02/17.
 */

public class DialogViolation extends DialogEx
{
   protected Lane m_pLane = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class TableView extends TableWidget implements android.view.View.OnClickListener
   {
      public TableView(Context pWnd) throws Exception
      {
         super(pWnd);
         Vector<FieldData> Items = new Vector<>();
         {
            Items.add(new FieldData(R.string.tbl_datetime, DEFAULT));
            Items.add(new FieldData(R.string.tbl_via, DEFAULT));
            Items.add(new FieldData(R.string.tbl_photo, DEFAULT));
         }
         this.CreateHeader(Items);
         this.SetBorder(true);
         this.SetMargins(new Rect(8, 8, 8, 8));
      }

      @Override
      public void onClick(View v)
      {
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogViolation(Context pWnd, int nID, final Lane pLane)
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
      TableView pTable = new TableView(this.getContext());
      this.SetIcon(R.drawable.ic_violation);
      super.View.addView(pTable, pTable.Params);
      this.SetButtons(new int[]{R.string.button_ok, R.string.button_cancel});
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      int nWhich = v.getId();

      switch (nWhich)
      {
         case R.string.button_ok:
            break;
         case R.string.button_cancel:
            dismiss();
            break;
      }
   }
}
