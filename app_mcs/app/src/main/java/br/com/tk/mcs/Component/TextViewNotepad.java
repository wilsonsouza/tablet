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

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class TextViewNotepad extends LinearHorizontal
{
   public final static int DEFAULT = -1;
   public EditText Data = null;
   public TextViewEx Text = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewNotepad(Context context, int nID, int nSize, boolean bEnabled, int nHeight, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new EditText(this.getContext());
      this.Text = new TextViewEx(context, nID, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding(new Rect(8, 8, 8, 8));
      this.SetFontSize(DEFAULT).SetEnabled(bEnabled);
      this.SetDataWidth(nWidth).SetMargins(new Rect(8, 8, 8, 8));
      this.SetDataHeight(nHeight);

      if(nID != DEFAULT)
      {
         this.addView(this.Text, this.Text.Params);
      }
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewNotepad(Context context, String szLabel, int nSize, boolean bEnabled, int nHeight, int nWidth, boolean bBorder)
   {
      super(context);
      this.Data = new EditText(this.getContext());
      this.Text = new TextViewEx(context, szLabel, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Text.setGravity(Gravity.LEFT);
      this.Data.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nSize)});
      this.SetPadding(new Rect(8, 8, 8, 8));
      this.SetFontSize(DEFAULT).SetEnabled(bEnabled);
      this.SetDataWidth(nWidth).SetMargins(new Rect(8, 8, 8, 8));
      this.SetDataHeight(nHeight);

      if(!szLabel.isEmpty())
      {
         this.addView(this.Text, this.Text.Params);
      }

      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public TextViewNotepad SetBorder(boolean bDraw)
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
   public TextViewNotepad SetMargins(Rect margins)
   {
      super.SetMargins(margins);
      this.Text.SetMargins(margins);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public TextViewNotepad SetPadding(Rect padding)
   {
      this.Data.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private TextViewNotepad SetFontSize(int nFontSize)
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
   public TextViewNotepad SetEnabled(boolean bEnabled)
   {
      this.setEnabled(bEnabled);
      this.Data.setEnabled(bEnabled);
      this.Text.setEnabled(bEnabled);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewNotepad SetDataWidth(int nWidth)
   {
      switch (nWidth)
      {
         case -1:
            nWidth = ToDP(0x100);
            break;
         default:
            nWidth = ToDP(nWidth);
            break;
      }
      this.Data.setWidth(nWidth);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewNotepad SetDataHeight(int nHeight)
   {
      switch (nHeight)
      {
         case DEFAULT:
            nHeight = ToDP(0x64);
            break;
         default:
            nHeight = ToDP(nHeight);
            break;
      }
      this.Data.setHeight(nHeight);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
