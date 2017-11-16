/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wilsonsouza on 13/12/16.
 */

public class Network
{
   static public boolean IsAvailable(Context context)
   {
      ConnectivityManager pcm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
      NetworkInfo pni = pcm.getActiveNetworkInfo();
      //if no network is available pni will be null
      try
      {
         if (pni.isConnected())
         {
            return true;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return false;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   static public Bitmap DownloadBitmap(String szUrl) throws Exception
   {
      Bitmap bmp = null;
      InputStream is = null;
      //
      try
      {
         URL pLink = new URL(szUrl);
         HttpURLConnection conn = (HttpURLConnection)pLink.openConnection();
         //
         conn.connect();
         is = conn.getInputStream();
         bmp = BitmapFactory.decodeStream(is);
      }
      catch (Exception e)
      {
         Log.e(Network.class.getClass().getName(), e.getMessage());
         throw e;
      }
      finally
      {
         if(is != null)
         {
            is.close();
         }
      }
      return bmp;
   }
}
