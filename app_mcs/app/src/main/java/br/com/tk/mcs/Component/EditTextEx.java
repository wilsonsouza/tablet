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
import android.widget.EditText;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class EditTextEx extends LinearHorizontal
{
   public final static int DEFAULT = -1;
   public EditText Data = null;
   public TextViewEx Text = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public EditTextEx(Context context, int nID, int nSize, boolean bEnabled, int nFontSize, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new EditText(this.getContext());
      this.Text = new TextViewEx(context, nID, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding( ConfigDisplayMetrics.EditRect).SetFontSize ( nFontSize ).SetEnabled ( bEnabled );
      this.SetDataWidth(nWidth).SetMargins(ConfigDisplayMetrics.EditRect);

      if(nID != DEFAULT)
      {
         this.addView(this.Text, this.Text.Params);
      }
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public EditTextEx(Context context, String szLabel, int nSize, boolean bEnabled, int nFontSize, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new EditText(this.getContext());
      this.Text = new TextViewEx(context, szLabel, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding(ConfigDisplayMetrics.EditRect).SetFontSize(nFontSize).SetEnabled(bEnabled);
      this.SetDataWidth(nWidth).SetMargins(ConfigDisplayMetrics.EditRect);

      if(!szLabel.isEmpty())
      {
         this.addView(this.Text, this.Text.Params);
      }

      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public EditTextEx SetBorder(boolean bDraw)
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
   public EditTextEx SetMargins(Rect margins)
   {
      super.SetMargins(margins);
      this.Text.SetMargins(margins);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public EditTextEx SetPadding(Rect padding)
   {
      this.Data.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private EditTextEx SetFontSize(int nFontSize)
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
   public EditTextEx SetEnabled(boolean bEnabled)
   {
      this.setEnabled(bEnabled);
      this.Data.setEnabled(bEnabled);
      this.Text.setEnabled(bEnabled);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private EditTextEx SetDataWidth(int nWidth)
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
}
