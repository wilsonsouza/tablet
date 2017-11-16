package br.com.tk.mcs.Component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 31/01/17.
 */

public class SpinnerEx extends LinearHorizontal
{
   public final static int DEFAULT = -1;
   public Spinner Data = null;
   protected TextView Text = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public SpinnerEx(Context context, int nId, int nFontSize, int nWidth, boolean bEnabled, boolean bBorder)
   {
      super(context, null, ConfigDisplayMetrics.SpinnerStyle);
      this.Text = new TextView(getContext());
      this.Data = new Spinner(getContext());
      this.Text.setGravity(Gravity.LEFT);
      this.Text.setText(nId);
      this.setEnabled(bEnabled);
      this.SetFontSize(nFontSize);
      this.SetDataWidth(nWidth);
      this.SetMargins(new Rect(8, 8, 8, 8));

      if(bBorder)
      {
         BorderWidget.Builder(Data);
      }

      this.addView(this.Text, this.Params);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public SpinnerEx(Context context, String szCaption, int nFontSize, int nWidth, boolean bEnabled, boolean bBorder)
   {
      super(context, null, ConfigDisplayMetrics.SpinnerStyle);
      this.Text = new TextView(getContext());
      this.Data = new Spinner(getContext());
      this.Text.setGravity(Gravity.LEFT);
      this.Text.setText(szCaption);
      this.setEnabled(bEnabled);
      this.SetFontSize(nFontSize);
      this.SetDataWidth(nWidth);
      this.SetMargins(new Rect(8, 8, 8, 8));

      if(bBorder)
      {
         BorderWidget.Builder(Data);
      }

      this.addView(this.Text, this.Params);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void SetFontSize(int nFontSize)
   {
      switch (nFontSize)
      {
         case DEFAULT:
            Font.SetSize(this.Data, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            Font.SetSize(this.Text, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            break;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(boolean bEnabled)
   {
      this.Data.setEnabled(bEnabled);
      this.Text.setEnabled(bEnabled);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public boolean isEnabled()
   {
      return this.Data.isEnabled();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void SetDataWidth(int nWidth)
   {
      switch (nWidth)
      {
         case -1:
            nWidth = ToDP(200);
            break;
         default:
            nWidth = ToDP(nWidth);
      }
      this.Data.setMinimumWidth(nWidth);
   }
}
