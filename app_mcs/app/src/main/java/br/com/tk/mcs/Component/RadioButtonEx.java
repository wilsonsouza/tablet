/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.RadioButton;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class RadioButtonEx extends LinearHorizontal
{
   public final static int DEFAULT = -1;
   public final static int CAPTION = -2;
   public RadioButton Data;
   //-----------------------------------------------------------------------------------------------------------------//
   public RadioButtonEx(Context context, int nID, boolean bEnabled, boolean bChecked, int nFontSize)
   {
      super(context, null, ConfigDisplayMetrics.RadioButtonStyle);
      this.Data = new RadioButton(this.getContext());
      this.SetFontSize(nFontSize);
      this.Data.setText(nID);
      this.setEnabled(bEnabled);
      this.setChecked(bChecked);
      this.Data.setTextColor(Color.BLACK);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public RadioButtonEx(Context context, String szText, boolean bEnabled, boolean bChecked, int nFontSize)
   {
      super(context, null, ConfigDisplayMetrics.RadioButtonStyle);
      this.Data = new RadioButton(this.getContext());
      this.SetFontSize(nFontSize);
      this.Data.setText(szText);
      this.setEnabled(bEnabled);
      this.setChecked(bChecked);
      this.Data.setTextColor(Color.BLACK);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private RadioButtonEx SetFontSize(int nFontSize)
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
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setBackgroundResource(int nResourceId)
   {
      this.Data.setBackgroundResource(nResourceId);
      super.setBackgroundResource(nResourceId);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(boolean enabled)
   {
      this.Data.setEnabled(enabled);
      super.setEnabled(enabled);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setChecked(boolean checked)
   {
      this.Data.setChecked(checked);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public boolean isChecked()
   {
      return this.Data.isChecked();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setOnClickListener(View.OnClickListener pClick)
   {
      this.Data.setOnClickListener(pClick);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setId(final int id)
   {
      this.Data.setId(id);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public int getId()
   {
      return this.Data.getId();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setText(final String data)
   {
      this.Data.setText(data);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setText(final int id)
   {
      this.Data.setText(id);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String getText()
   {
      return this.Data.getText().toString();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setBackgroundColor(final int color)
   {
      this.Data.setBackgroundColor(color);
      super.setBackgroundColor(color);
   }
}
