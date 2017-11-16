/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class CheckBoxEx extends LinearHorizontal
{
   public final static int DEFAULT = -1;
   public final static int CAPTION = -2;
   public CheckBox Data = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public CheckBoxEx(Context context, int nID, boolean bEnabled, boolean bChecked, int nFontSize)
   {
      super(context, null, ConfigDisplayMetrics.CheckButtonStyle);
      this.Data = new CheckBox(this.getContext());
      this.SetFontSize(nFontSize);
      this.Data.setText(nID);
      this.setEnabled(bEnabled);
      this.setChecked(bChecked);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public CheckBoxEx(Context context, String szText, boolean bEnabled, boolean bChecked, int nFontSize)
   {
      super(context, null, ConfigDisplayMetrics.CheckButtonStyle);
      this.Data = new CheckBox(this.getContext());
      this.SetFontSize(nFontSize);
      this.Data.setText(szText);
      this.setEnabled(bEnabled);
      this.setChecked(bChecked);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void SetFontSize(int nFontSize)
   {
      switch (nFontSize)
      {
         case DEFAULT:
            Font.SetSize(this.Data, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            break;
         case CAPTION:
            Font.SetSize(this.Data, this.Data.getTextSize(), Font.CAPTION_SIZE);
            break;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(final boolean bValue)
   {
      this.Data.setEnabled(bValue);
      super.setEnabled(bValue);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setChecked(final boolean bValue)
   {
      if(!bValue)
      {
         this.Data.setTextColor(Color.CYAN);
      }
      this.Data.setChecked(bValue);
   }
}
