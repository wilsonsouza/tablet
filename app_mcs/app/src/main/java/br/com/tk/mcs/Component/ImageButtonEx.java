/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class ImageButtonEx extends LinearVertical
{
   public ImageButton Data = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageButtonEx(Context context, Point point, int nResourceID, boolean bBorder, Rect padding, Rect margins)
   {
      super(context);
      this.Data = new ImageButton(this.getContext());
      this.SetImage(nResourceID);
      this.SetMargins(margins);
      this.SetPadding(padding);
      this.Params.SetDimension(new Point(point.x, point.y));
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetImage(int nResourceID)
   {
      if(nResourceID != -1)
      {
         Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), nResourceID);
         this.Data.setId(nResourceID);
         this.Resize(bmp, new Point(this.Params.width, this.Params.height));
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Resize(Bitmap bmp, Point p)
   {
      Bitmap bitbmp = Bitmap.createScaledBitmap(bmp, ToDP(p.x), ToDP(p.y), true);
      this.Data.setImageBitmap(bitbmp);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Resize(ImageButton pView, Point p)
   {
      try
      {
         Bitmap bmp = ((BitmapDrawable) pView.getDrawable()).getBitmap();
         bmp = Bitmap.createScaledBitmap(bmp, p.x, p.y, true);
         pView.setImageBitmap(bmp);
      }
      catch (Exception e)
      {
         Log.e(ImageButtonEx.class.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageButtonEx SetPadding(Rect padding)
   {
      if(padding != null)
      {
         this.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageButtonEx SetMargins(Rect margins)
   {
      if(margins != null)
      {
         this.Params.setMargins(margins.left, margins.top, margins.right, margins.bottom);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageButtonEx SetColor(int nColor)
   {
      this.setBackgroundColor(nColor);
      this.Data.setBackgroundColor(nColor);
      return this;
   }
}
