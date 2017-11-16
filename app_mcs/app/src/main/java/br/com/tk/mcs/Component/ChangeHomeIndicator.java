/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 16/02/17.
 */

public class ChangeHomeIndicator extends ImageViewEx
{
   public ChangeHomeIndicator(AppCompatActivity pWnd, int nResourceID)
   {
      super(pWnd, ConfigDisplayMetrics.HomeIndicator, nResourceID, false, null, null);
      try
      {
         ActionBar pAction = pWnd.getSupportActionBar();
         this.Resize(this.Data, new Point(ConfigDisplayMetrics.HomeIndicator.x , this.Params.GetActionBarHeight() - 0x10));
         pAction.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_USE_LOGO);
         pAction.setIcon(this.ToDrawable());
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
