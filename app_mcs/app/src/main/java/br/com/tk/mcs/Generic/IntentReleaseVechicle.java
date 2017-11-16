package br.com.tk.mcs.Generic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import br.com.tk.mcs.Lane.Lane;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class IntentReleaseVechicle extends Intent
{
   public IntentReleaseVechicle(Context pWnd, Class<? extends AppCompatActivity> pClass, Lane pLane, String szVehicleBitmap)
   {
      super(pWnd, pClass);
      this.putExtra(Utils.LANE, pLane);
      this.putExtra(Utils.MSC_IMAGE, szVehicleBitmap);
      pWnd.startActivity(this);
   }
}
