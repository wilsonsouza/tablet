/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class EditTextCompleteView extends LinearHorizontal
{
   public static ArrayList<String> QueueOfCompleteView = new ArrayList<>();
   public final static int DEFAULT = -1;
   public AutoCompleteTextView Data = null;
   public TextViewEx Text = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public EditTextCompleteView(Context context, int nID, int nSize, boolean bEnabled, int nFontSize, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new AutoCompleteTextView(this.getContext());
      this.Text = new TextViewEx(this.getContext(), nID, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding(new Rect(8, 8, 8, 8)).SetFontSize(nFontSize).SetEnabled(bEnabled);
      this.SetDataWidth(nWidth).SetMargins(new Rect(8, 8, 8, 8));

      if(nID != DEFAULT)
      {
         this.addView(this.Text, this.Text.Params);
      }
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public EditTextCompleteView(Context context, String szLabel, int nSize, boolean bEnabled, int nFontSize, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new AutoCompleteTextView(this.getContext());
      this.Text = new TextViewEx(this.getContext(), szLabel, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding(new Rect(8, 8, 8, 8)).SetFontSize(nFontSize).SetEnabled(bEnabled);
      this.SetDataWidth(nWidth).SetMargins(new Rect(8, 8, 8, 8));

      if(!szLabel.isEmpty())
      {
         this.addView(this.Text, this.Text.Params);
      }

      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public EditTextCompleteView SetBorder(boolean bDraw)
   {
      if(bDraw)
      {
         BorderWidget.Builder(this.Data);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String GetData()
   {
      return this.Data.getText().toString();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public EditTextCompleteView SetMargins(Rect margins)
   {
      super.SetMargins(margins);
      this.Text.SetMargins(margins);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public EditTextCompleteView SetPadding(Rect padding)
   {
      this.Data.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private EditTextCompleteView SetFontSize(int nFontSize)
   {
      switch (nFontSize)
      {
         case DEFAULT:
            Font.SetSize(this.Data, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            Font.SetSize(this.Text.Data, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            break;
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public EditTextCompleteView SetEnabled(boolean bEnabled)
   {
      this.setEnabled(bEnabled);
      this.Data.setEnabled(bEnabled);
      this.Text.setEnabled(bEnabled);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private EditTextCompleteView SetDataWidth(int nWidth)
   {
      switch (nWidth)
      {
         case -1:
            nWidth = ToDP(250);
            break;
         default:
            nWidth = ToDP(nWidth);
            break;
      }
      this.Data.setWidth(nWidth);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void AddQueueOfCompleteView(final String szValue)
   {
      /* warranty all tag and plate don't duplicate */
      if (QueueOfCompleteView.indexOf(szValue.toUpperCase().trim()) == -1)
      {
         QueueOfCompleteView.add(szValue.toUpperCase().trim());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetAdapter()
   {
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, QueueOfCompleteView);
      {
         this.Data.setThreshold(1);
         this.Data.setAdapter(adapter);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void RemoveAdapter()
   {
   }
}
