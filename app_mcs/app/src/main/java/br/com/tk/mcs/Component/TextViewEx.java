/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.widget.TextView;

import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 27/01/17.
 */

public class TextViewEx extends LinearHorizontal
{
   public final static int DEFAULT = -2;
   public final static int CUSTOM = -4;
   public final static int CAPTION = -1;
   public final static int NL_COLOR = -1;
   public final static int RL_COLOR = -3;
   public final static int PS_TYPE = 0;
   public final static int PS_COLOR = 1;
   public final static int [] DEFCOLOR = new int[]{RL_COLOR, R.drawable.window_caption_bar};
   public final static int [] TRACOLOR = new int[]{NL_COLOR, Color.TRANSPARENT};
   public TextView Data = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewEx(Context pWnd)
   {
      super(pWnd);
      this.Data = new TextView(this.getContext());
      this.Params = this.Build(WRAP, WRAP);
      this.addView(this.Data, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewEx(Context pWnd, int nID, int nFontSize, int [] nColor, boolean bBorder)
   {
      super(pWnd);
      this.Data = new TextView(this.getContext());
      this.Params = this.Build(WRAP, WRAP);
      this.SetCaption(nID).SetFontSize(nFontSize);
      this.setGravity(Gravity.CENTER_HORIZONTAL);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder).SetColor(nColor);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewEx(Context pWnd, String szText, int nFontSize, int [] nColor, boolean bBorder)
   {
      super(pWnd);
      this.Data = new TextView(this.getContext());
      this.Params = this.Build(WRAP, WRAP);
      this.SetCaption(szText).SetFontSize(nFontSize);
      this.setGravity(Gravity.CENTER_HORIZONTAL);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder).SetColor(nColor);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public TextViewEx SetBorder(boolean bDraw)
   {
      if(bDraw)
      {
         BorderWidget.Builder(this);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private <T> TextViewEx SetCaption(T caption)
   {
      if (caption.getClass().getName().equals("java.lang.String"))
      {
         if (caption.toString() != null)
         {
            this.Data.setText(Html.fromHtml(caption.toString()));
         }
         else
         {
            this.Data.setText("");
         }
      }
      else if (caption.getClass().getName().equals("java.lang.Integer"))
      {
         int id = Integer.parseInt(caption.toString());

         if (id != -1)
         {
            this.Data.setText(Html.fromHtml(getContext().getString(id)));
         }
         else
         {
            this.Data.setText("");
         }
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewEx SetColor(int [] nColor)
   {
      switch (nColor[PS_TYPE])
      {
         case NL_COLOR:
            this.setBackgroundColor(nColor[PS_COLOR]);
            this.Data.setBackgroundColor(nColor[PS_COLOR]);
            break;
         case DEFAULT:
            this.setBackgroundResource(DEFCOLOR[PS_COLOR]);
            this.setBackgroundResource(DEFCOLOR[PS_COLOR]);
            break;
         case RL_COLOR:
            this.setBackgroundResource(nColor[PS_COLOR]);
            this.setBackgroundResource(nColor[PS_COLOR]);
            break;
         case CUSTOM:
            this.Data.setTextColor(nColor[1]);
            break;
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TextViewEx SetFontSize(int nFontSize)
   {
      switch (nFontSize)
      {
         case DEFAULT:
            Font.SetSize(this.Data, Font.DEFAULT_SIZE, Font.DEFAULT_SIZE);
            break;
         case CAPTION:
            Font.SetSize(this.Data, Font.CAPTION_SIZE, Font.CAPTION_SIZE);
            break;
      }
      return this;
   }
}
