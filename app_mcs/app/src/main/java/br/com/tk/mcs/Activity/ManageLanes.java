/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Activity;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;

import br.com.tk.mcs.Component.QuestionBox;
import br.com.tk.mcs.Database.PersistenceController;
import br.com.tk.mcs.Generic.BuilderManager;
import br.com.tk.mcs.Generic.Utils;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 18/01/17.
 */

public class ManageLanes extends AppCompatActivity
{
   /* main variables */
   public BuilderManager Manager = null;
   protected ArrayList<Lane> Lanes = null;
   protected String UserNameLogged = null;
   public PersistenceController Db = null;
   //-----------------------------------------------------------------------------------------------------------------//
   void PrepareLanes()
   {
      int i = 0;
      this.Lanes = new ArrayList<>();
      this.Db = new PersistenceController(this);

      for (final String szName : Db.getLanesNames())
      {
               /* put on message on main thread */
         final String szAddress = Db.getLaneAddressByName(szName);
         //Operations pOperation = new Operations("http://" + szAddress + ":8000/");
         final String szUrl = String.format(getString(R.string.ids_url_lane), szAddress);
         final Lane pLane = new Lane(i++, szName, UserNameLogged, new Operations(szUrl, UserNameLogged));
               /**/
         Log.i(this.getClass().getName(), "Lane Url " + szUrl);
         Lanes.add(pLane);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   @TargetApi(23)
   public void onCreate(Bundle pSavedState)
   {
      try
      {
         super.onCreate(pSavedState);
         this.setTitle(R.string.app_name);
         this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         //
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
         {
            try
            {
               if ( this.checkSelfPermission (
                  android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED )
               {
                  this.requestPermissions ( new String[]{ android.Manifest.permission.CAMERA }, 0 );
               }
            }
            catch(Exception e)
            {
               Log.e(this.getClass ().getName(), e.getMessage ());
            }
         }
         //
         this.UserNameLogged = getIntent().getStringExtra(Utils.OPERATOR);
         this.PrepareLanes();
         this.Manager = new BuilderManager(this, this.Lanes, this.UserNameLogged);
         {
            this.Manager.Start();
         }
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onDestroy()
   {
      try
      {
         super.onDestroy();
         this.Manager.Finalize();
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onResume()
   {
      super.onResume();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public boolean onOptionsItemSelected(MenuItem pItem)
   {
      int nId = pItem.getItemId();

      if(nId == android.R.id.home)
      {
         new QuestionBox(this, R.string.manager_exit_title, R.string.manager_exit_response, QuestionBox.DEFAULT);
      }
      return true;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
