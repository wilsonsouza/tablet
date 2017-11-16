/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class ButtonImage extends LinearLayoutImpl
{
   public static final int UP = 0xff00;
   public static final int DOWN = 0xf65a;
   public static final int LEFT = 0xf55a;
   public static final int RIGHT = 0xfff1;
   public static Point m_offset = ConfigDisplayMetrics.ButtonImageIcon;
   private ImageViewEx Data = null;
   private TextViewEx Text = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public ButtonImage(Context context, int nCaption, Point point, int nResourceID, boolean bBorder, int nMode)
   {
      super(context, null, ConfigDisplayMetrics.ButtonStyle);
      this.Data = new ImageViewEx(this.getContext(), m_offset, nResourceID, false, null, null);
      this.Text = new TextViewEx(this.getContext(), nCaption, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Invalidate(this.Text, true);
      this.SetDetails(point, nMode);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ButtonImage(Context context, String szCaption, Point point, int nResourceID, boolean bBorder, int nMode)
   {
      super(context, null, ConfigDisplayMetrics.ButtonStyle);
      this.Data = new ImageViewEx(this.getContext(), m_offset, nResourceID, false, null, null);
      this.Text = new TextViewEx(this.getContext(), szCaption, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);
      this.Invalidate(this.Text, true);
      this.SetDetails(point, nMode);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetDetails(Point point, int nMode)
   {
      if(this.Text.getWidth() > 0)
      {
         point.x = this.Text.getWidth() + 0x10;
         point.y = m_offset.y + this.Text.getHeight() + 0x10;
         this.Params.SetDimension(new Point(point));
      }

      switch (nMode)
      {
         case UP:
         case DOWN:
            this.setOrientation(VERTICAL);
            break;
         case LEFT:
         case RIGHT:
            this.setOrientation(HORIZONTAL);
            break;
      }

      switch (nMode)
      {
         case UP:
            this.addView(this.Data, this.Data.Params);
            this.addView(this.Text, this.Text.Params);
            break;
         case DOWN:
            this.addView(this.Data, this.Data.Params);
            this.addView(this.Text, this.Text.Params);
            break;
         case LEFT:
            this.addView(this.Data, this.Data.Params);
            this.addView(this.Text, this.Text.Params);
            break;
         case RIGHT:
            this.addView(this.Text, this.Text.Params);
            this.addView(this.Data, this.Data.Params);
            break;
      }

      this.setGravity(Gravity.CENTER);
      this.Text.Data.setTextColor(Color.WHITE);
      this.Data.setBackgroundResource(R.drawable.button_selector);
      this.setBackgroundResource(R.drawable.button_selector);
      this.setEnabled(false);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(boolean enabled)
   {
      this.Data.setEnabled(enabled);
      this.Text.setEnabled(enabled);
      super.setEnabled(enabled);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setBackgroundResource(int nResourceId)
   {
      this.Data.setBackgroundResource(nResourceId);
      super.setBackgroundResource(nResourceId);
   }
}
