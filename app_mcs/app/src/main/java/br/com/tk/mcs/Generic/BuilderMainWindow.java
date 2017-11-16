/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.graphics.Color;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.LinearVertical;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class BuilderMainWindow extends LinearVertical
{
   public BuilderMainWindow(ManageLanes pWnd)
   {
      super(pWnd);
      this.Params = this.Build(MATCH, MATCH);
      this.setBackgroundColor(Color.TRANSPARENT);
      pWnd.setContentView(this, this.Params);
   }
}
