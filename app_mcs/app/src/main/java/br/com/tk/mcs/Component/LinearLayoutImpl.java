/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import br.com.tk.mcs.Generic.CalculePixels;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class LinearLayoutImpl extends LinearLayout
{
   public static final int WRAP = LayoutParams.WRAP_CONTENT;
   public static final int MATCH = LayoutParams.MATCH_PARENT;
   public Parameters Params = new Parameters(WRAP, WRAP);
   //-----------------------------------------------------------------------------------------------------------------//
   public class Parameters extends LayoutParams
   {
      public DisplayMetrics Display = Resources.getSystem().getDisplayMetrics();
      public Configuration Config = Resources.getSystem().getConfiguration();
      //-----------------------------------------------------------------------------------------------------------------//
      public Parameters(int width, int height)
      {
         super(width, height);
         /* set real dimension */
         this.Update(new Point(width, height));
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public Parameters(Point point)
      {
         super(point.x, point.y);
         this.Update(point);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      private Parameters Update(Point point)
      {
         if(point.x == MATCH_PARENT)
         {
            this.width = Display.widthPixels;
         }
         if(point.y == MATCH_PARENT)
         {
            this.height = Display.heightPixels - this.GetActionBarHeight();
         }
         return this;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public Parameters SetMargins(Rect r)
      {
         if(r != null)
         {
            r = ToRect(r);
            this.setMargins(r.left, r.top, r.right, r.bottom);
         }
         return this;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public Parameters SetDimension(Point point)
      {
         if(point != null)
         {
            this.width = ToDP(point.x);
            this.height = ToDP(point.y);
         }
         return this;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public int GetActionBarHeight()
      {
         TypedValue tv = new TypedValue();
         //
         getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
         int nHeight = getResources().getDimensionPixelSize(tv.resourceId);
         //
         Log.i(this.getClass().getName(), String.format("ActionBar height %d", nHeight));
         return nHeight;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl(Context context)
   {
      super(context);
      this.setLayoutParams(this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      this.setLayoutParams(this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl(Context context, AttributeSet attrs, int defStyleAttr)
   {
      super(context, attrs, defStyleAttr);
      this.setLayoutParams(Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl SetMargins(Rect r)
   {
      this.Params.SetMargins(r);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl SetPadding(Rect padding)
   {
      if (padding != null)
      {
         Rect r = this.ToRect(padding);
         this.setPadding(r.left, r.top, r.right, r.bottom);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public int ToDP(int nValue)
   {
      return CalculePixels.ToDP(nValue);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Parameters Build(int w, int h)
   {
      return new LinearLayoutImpl.Parameters(w, h);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Parameters Build(Point point)
   {
      return new LinearLayoutImpl.Parameters(point);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl Invalidate(boolean bForce)
   {
      UpdateContent.Refresh(this, bForce);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl Invalidate(ViewGroup pView, boolean bUpdate)
   {
      UpdateContent.Refresh(pView, bUpdate);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl SetBorder(int nColor)
   {
      new BorderWidget(this, nColor, 1, Color.BLACK);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl SetBorder(boolean bDraw, int nColor, int nBorderColor)
   {
      if(bDraw)
      {
         new BorderWidget(this, nColor, 1, nBorderColor);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public LinearLayoutImpl SetBorder(boolean bDraw)
   {
      if(bDraw)
      {
         new BorderWidget(this, Color.WHITE, 1, Color.BLACK);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Point ToPoint(Point p)
   {
      return ToPoint(p.x, p.y);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Rect ToRect(Rect r)
   {
      return this.ToRect(r.left, r.top, r.right, r.bottom);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Point ToPoint(int x, int y)
   {
      return new Point(this.ToDP(x), this.ToDP(y));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Rect ToRect(int x, int y, int r, int b)
   {
      return new Rect(this.ToDP(x), this.ToDP(y), this.ToDP(r), this.ToDP(b));
   }
}
