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
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class ImageViewEx extends LinearVertical
{
   public ImageView Data = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageViewEx(Context context, Point point, int nResourceID, boolean bBorder, @Nullable Rect padding, @Nullable Rect margins)
   {
      super(context);
      this.Data = new ImageView(this.getContext());
      this.Params.SetDimension(point);
      this.SetImage(nResourceID);
      this.SetMargins(margins);
      this.SetPadding(padding);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetImage(int nResourceID)
   {
      try
      {
         if (nResourceID != -1)
         {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), nResourceID);
            this.setId(nResourceID);
            bmp = Bitmap.createScaledBitmap(bmp, this.Params.width, this.Params.height, true);
            this.Data.setImageBitmap(bmp);
         }
      }
      catch (Exception e)
      {
         Log.e(getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Resize(Bitmap bmp, Point p)
   {
      try
      {
         Point np = this.ToPoint(p);
         Bitmap bitbmp = Bitmap.createScaledBitmap(bmp, np.x, np.y, true);
         this.Data.setImageBitmap(bitbmp);
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Resize(ImageView pView, Point p)
   {
      try
      {
         Point np = this.ToPoint(p);
         Bitmap bmp = ((BitmapDrawable) pView.getDrawable()).getBitmap();
         bmp = Bitmap.createScaledBitmap(bmp, np.x, np.y, true);
         pView.setImageBitmap(bmp);
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageViewEx SetPadding(Rect padding)
   {
      if(padding != null)
      {
         padding = this.ToRect(padding);
         this.Data.setPadding(padding.left, padding.top, padding.right, padding.bottom);
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageViewEx SetMargins(Rect margins)
   {
      this.Params.SetMargins(margins);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ImageViewEx SetColor(int nColor)
   {
      this.setBackgroundColor(nColor);
      this.Data.setBackgroundColor(nColor);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Drawable ToDrawable()
   {
      return this.Data.getDrawable();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(boolean enabled)
   {
      this.Data.setEnabled(enabled);
      super.setEnabled(enabled);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public boolean isEnabled()
   {
      return this.Data.isEnabled();
   }
}
