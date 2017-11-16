/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import br.com.tk.mcs.Generic.Network;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 01/02/17.
 */

public class TaskDownloadBitmap extends AsyncTask<String, Void, Bitmap>
{
   protected ImageViewEx m_bitmap = null;
   protected Point m_point = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public TaskDownloadBitmap(ImageViewEx bmp, Point point)
   {
      super();
      this.m_bitmap = bmp;
      this.m_point = point;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected Bitmap doInBackground(String... params)
   {
      Bitmap bmp = null;
      try
      {
         bmp = Network.DownloadBitmap(params[0]);
      }
      catch (final Exception e)
      {
         Log.e(getClass().getName(), e.getMessage());
      }
      return bmp;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onPostExecute(Bitmap bmp)
   {
      if(bmp != null)
      {
         m_bitmap.Resize(bmp, m_point);
      }
      else
      {
         m_bitmap.Data.setImageResource(R.drawable.box_error);
      }
   }
}
